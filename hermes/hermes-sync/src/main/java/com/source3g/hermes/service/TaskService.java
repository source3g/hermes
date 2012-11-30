package com.source3g.hermes.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.TaskConstants;
import com.source3g.hermes.entity.Device;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.enums.TaskStatus;
import com.source3g.hermes.sync.entity.DeviceStatus;
import com.source3g.hermes.sync.entity.TaskLog;
import com.source3g.hermes.sync.entity.TaskPackage;
import com.source3g.hermes.sync.utils.TarGZipUtils;
import com.source3g.hermes.utils.MD5;

@Service
public class TaskService extends BaseService {

	@Value(value = "${local.url}")
	private String localUrl;
	@Value(value = "${task.package.dir}")
	private String taskPackagePath;
	@Value(value = "${task.redelivery.count}")
	private int redeliveryCount;

	public DeviceStatus findStatus(String sn) {
		return mongoTemplate.findOne(new Query(Criteria.where("deviceSn").is(sn)), DeviceStatus.class);
	}

	@Deprecated
	public List<TaskPackage> genTasksByCreateTime(String sn) {
		DeviceStatus deviceStatus = findStatus(sn);
		if (deviceStatus == null) {
			deviceStatus = new DeviceStatus();
			deviceStatus.setId(ObjectId.get());
			deviceStatus.setDeviceSn(sn);
		}
		deviceStatus.setRequestTime(new Date());
		mongoTemplate.save(deviceStatus);
		List<TaskPackage> tasks = new ArrayList<TaskPackage>();
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		if (deviceStatus == null || deviceStatus.getLastUpdateTime() == null) {
			tasks = findAllPackages(merchant.getId());
		} else {
			tasks = findIncrementPackages(merchant.getId(), deviceStatus.getLastUpdateTime());
		}
		handleTasksUrl(tasks);
		return tasks;
	}

	public List<TaskPackage> genTasks(String sn) {
		List<TaskPackage> result = new ArrayList<TaskPackage>();
		TaskPackage taskPackage;
		Device device = mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		Merchant merchant = mongoTemplate.findOne(new Query(Criteria.where("deviceIds").is(device.getId())), Merchant.class);
		DeviceStatus deviceStatus = findStatus(sn);
		if (deviceStatus == null) {
			deviceStatus = new DeviceStatus();
			deviceStatus.setId(ObjectId.get());
			deviceStatus.setDeviceSn(sn);
		}
		if (deviceStatus.getLastTaskId() == null) {
			taskPackage = findFirstPackage(merchant.getId());
		} else {
			taskPackage = findIncrementPackage(merchant.getId(), deviceStatus.getLastTaskId());
		}
		if (taskPackage != null) {
			deviceStatus.setRequestTaskId(taskPackage.getTaskId());
			mongoTemplate.save(deviceStatus);
			handleTaskUrl(taskPackage);
			result.add(taskPackage);
		}
		return result;
	}

	private TaskPackage findIncrementPackage(ObjectId merchantId, Long lastTaskId) {
		Query findIncrementPackage = new Query();
		// 按创建时间降序排序
		Sort sort = new Sort(new Order(Direction.ASC, "taskId"));
		findIncrementPackage.addCriteria(Criteria.where("merchantId").is(merchantId).and("type").is(TaskConstants.INCREMENT_PACKAGE).and("taskId").gt(lastTaskId));
		return mongoTemplate.findOne(findIncrementPackage.with(sort), TaskPackage.class);
	}

	private void handleTasksUrl(List<TaskPackage> tasks) {
		for (TaskPackage task : tasks) {
			handleTaskUrl(task);
		}
	}

	/**
	 * 处理任务信息 自动拼remoteUrl
	 * 
	 * @param task
	 */
	private void handleTaskUrl(TaskPackage task) {
		task.setRemoteUrl(localUrl + "package/" + task.getRemoteUrl() + "/");
	}

	/**
	 * 报告任务执行结果
	 * 
	 * @return
	 */
	public void reportResult(String sn, boolean result) {
		DeviceStatus deviceStatus = findStatus(sn);
		if (result == true) {
			deviceStatus.setLastTaskId(deviceStatus.getRequestTaskId());
			deviceStatus.setFailedCount(0);
			TaskLog taskLog = new TaskLog();
			taskLog.setDeviceSn(deviceStatus.getDeviceSn());
			taskLog.setReportTime(new Date());
			taskLog.setTaskStatus(TaskStatus.成功);
			mongoTemplate.save(taskLog);
			// deviceStatus.setLastUpdateTime(deviceStatus.getRequestTime());
		} else {
			if (deviceStatus.getFailedCount() > redeliveryCount) {
				deviceStatus.setLastTaskId(deviceStatus.getRequestTaskId());
				deviceStatus.setFailedCount(0);
				TaskLog taskLog = new TaskLog();
				taskLog.setDeviceSn(deviceStatus.getDeviceSn());
				taskLog.setReportTime(new Date());
				taskLog.setTaskStatus(TaskStatus.失败);
			} else {
				deviceStatus.setFailedCount(deviceStatus.getFailedCount() + 1);
			}
		}
		mongoTemplate.save(deviceStatus);
	}

	/**
	 * 拿第一个合适的包
	 * 
	 * @param merchantId
	 * @return
	 */
	public TaskPackage findFirstPackage(ObjectId merchantId) {
		// 找到最新的全包
		Query findLastAllPackage = new Query();
		findLastAllPackage.addCriteria(Criteria.where("merchantId").is(merchantId).and("type").is(TaskConstants.ALL_PACKAGE));
		// 按任务序号降序排序，取第一个即为最新的全包
		Sort sort = new Sort(new Order(Direction.DESC, "taskId"));
		// 拿一个全包
		TaskPackage result = mongoTemplate.findOne(findLastAllPackage.with(sort), TaskPackage.class);
		// 没有全包，则拿第一个增量包
		if (result == null) {
			// 按ID升级排序，取第一个增量包
			Sort sortByIdAsc = new Sort(new Order(Direction.ASC, "taskId"));
			Query findFirstIncrementPackage = new Query();
			findFirstIncrementPackage.addCriteria(Criteria.where("type").is(TaskConstants.INCREMENT_PACKAGE));
			result = mongoTemplate.findOne(findFirstIncrementPackage.with(sortByIdAsc), TaskPackage.class);
		}
		return result;
	}

	@Deprecated
	public List<TaskPackage> findAllPackages(ObjectId merchantId) {
		List<TaskPackage> result = new ArrayList<TaskPackage>();
		// 找到最新的全包
		Query findLastAllPackage = new Query();
		findLastAllPackage.addCriteria(Criteria.where("merchantId").is(merchantId).and("type").is(TaskConstants.ALL_PACKAGE));
		// 按创建时间降序排序
		Sort sort = new Sort(new Order(Direction.DESC, "createTime"));
		TaskPackage lastAllPackage = mongoTemplate.findOne(findLastAllPackage.with(sort), TaskPackage.class);
		if (lastAllPackage == null) {
			result = mongoTemplate.findAll(TaskPackage.class);
		} else {
			Query query = new Query();
			query.addCriteria(Criteria.where("merchantId").is(merchantId).and("createTime").gte(lastAllPackage.getCreateTime()));
			result.add(lastAllPackage);
			result.addAll(mongoTemplate.find(query, TaskPackage.class));
		}
		return result;
	}

	@Deprecated
	public List<TaskPackage> findIncrementPackages(ObjectId merchantId, Date time) {
		List<TaskPackage> result = new ArrayList<TaskPackage>();
		Query findIncrementPackage = new Query();
		findIncrementPackage.addCriteria(Criteria.where("merchantId").is(merchantId).and("type").is(TaskConstants.INCREMENT_PACKAGE));
		result = mongoTemplate.find(findIncrementPackage, TaskPackage.class);
		return result;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public String getTaskPackagePath() {
		return taskPackagePath;
	}

	public void setTaskPackagePath(String taskPackagePath) {
		this.taskPackagePath = taskPackagePath;
	}

	public Date findLastPackageTime(Merchant merchant) {
		TaskPackage taskPackage = mongoTemplate.findOne(new Query().with(new Sort(new Order(Direction.DESC, "createTime"))), TaskPackage.class);
		if (taskPackage != null) {
			return taskPackage.getCreateTime();
		} else {
			return null;
		}
	}

	public void packageIncrement(Merchant merchant) throws IOException {
		Date date = findLastPackageTime(merchant);
		List<Customer> list;
		if (date == null) {
			list = mongoTemplate.findAll(Customer.class);
		} else {
			list = mongoTemplate.find(new Query(Criteria.where("operateTime").gte(date)), Customer.class);
		}
		if (list.size() == 0) {
			return;
		}
		Date createTime = new Date();
		TaskPackage taskPackage = new TaskPackage();
		taskPackage.setCreateTime(createTime);
		taskPackage.setTaskId(createTime.getTime());
		taskPackage.setId(ObjectId.get());
		taskPackage.setMerchantId(merchant.getId());
		taskPackage.setType(TaskConstants.INCREMENT_PACKAGE);
		// 文件名
		String fileName = String.valueOf(createTime.getTime());
		// 产生文件路径
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// 所在商户的相对路径
		String merchantPath = dateFormat.format(createTime) + "/" + merchant.getId().toString() + "/";
		// 所在商户绝对路径
		String folderPath = taskPackagePath + merchantPath;
		// 文件的相对路径
		String relativeFilePath = merchantPath + fileName;
		// 文件的绝对路径
		String absoluteFilePath = taskPackagePath + relativeFilePath;

		String txtFilePath = absoluteFilePath + ".sql";
		File taskFolder = new File(folderPath);
		if (!taskFolder.exists()) {
			taskFolder.mkdirs();
		}
		File file = new File(txtFilePath);
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		for (Customer c : list) {
			bw.write(c.toInsertOrUpdateSql());
			bw.newLine();
			bw.flush();
		}
		bw.close();
		// File gzipFile = new File(gzipFilePath);
		Resource resource = new ClassPathResource("taskfiles/customer/task.sh");

		// gzipFile.createNewFile();
		// // 建立压缩文件输出流
		// FileOutputStream fout = new FileOutputStream(gzipFile);
		// // 建立gzip压缩输出流
		// GZIPOutputStream gzout = new GZIPOutputStream(fout);
		// byte[] buf = new byte[1024];// 设定读入缓冲区尺寸
		// int num;
		// FileInputStream fin = new FileInputStream(file);
		// while ((num = fin.read(buf)) != -1) {
		// gzout.write(buf, 0, num);
		// }
		// gzout.finish();
		// gzout.close();// !!!关闭流,必须关闭所有输入输出流.保证输入输出完整和释放系统资源.
		// fout.close();
		// fin.close();
		try {
			File[] files = { new File(txtFilePath), resource.getFile() };
			File gzipFile = TarGZipUtils.tarGzip(fileName + "/", files, absoluteFilePath);

			String relativeGzipPath = merchantPath + gzipFile.getName();
			taskPackage.setRemoteUrl(relativeGzipPath);
			taskPackage.setMd5(MD5.md5sum(gzipFile.getAbsolutePath()));
			mongoTemplate.save(taskPackage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
