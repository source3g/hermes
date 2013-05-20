package com.source3g.hermes.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.constants.TaskConstants;
import com.source3g.hermes.entity.merchant.ElectricMenu;
import com.source3g.hermes.entity.merchant.ElectricMenuItem;
import com.source3g.hermes.entity.sync.ElectricMenuPackageStatus;
import com.source3g.hermes.entity.sync.TaskPackage;
import com.source3g.hermes.enums.ElectricMenuPackageStatusEnum;
import com.source3g.hermes.sync.utils.TarGZipUtils;
import com.source3g.hermes.utils.MD5;

@Service
public class ElectricMenuTaskService {

	private static Logger logger = LoggerFactory.getLogger(ElectricMenuTaskService.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private BaseService baseService;

	@Value(value = "${local.url}")
	private String localUrl;

	@Value(value = "${task.package.dir}")
	private String taskPackagePath;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void initMethod() {
		logger.debug("初始化电子菜单锁");
		mongoTemplate.updateMulti(new Query(), new Update().set("status", ElectricMenuPackageStatusEnum.FINISHED), ElectricMenuPackageStatus.class);
	}

	public void packageIncrement() {

	}

	public void packageAll() {

	}

	public void sync(ObjectId merchantId) {
		ElectricMenuPackageStatus electricMenuPackageStatus = mongoTemplate.findOne(new Query(Criteria.where("merchantId").is(merchantId).and("status").is(ElectricMenuPackageStatusEnum.PACKAGING)), ElectricMenuPackageStatus.class);
		if (electricMenuPackageStatus == null) {
			mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchantId)), new Update().set("status", ElectricMenuPackageStatusEnum.PACKAGING).set("lastChangeTime", new Date()), ElectricMenuPackageStatus.class);
			System.out.println("枷锁");
			try {
				genTask(merchantId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("生成任务");
			mongoTemplate.upsert(new Query(Criteria.where("merchantId").is(merchantId)), new Update().set("status", ElectricMenuPackageStatusEnum.FINISHED).set("lastChangeTime", new Date()), ElectricMenuPackageStatus.class);
			System.out.println("去锁");
		}
	}

	private void genTask(ObjectId merchantId) throws Exception {
		List<ElectricMenu> menus = mongoTemplate.find(new Query(Criteria.where("merchantId").is(merchantId)), ElectricMenu.class);

		Date createTime = new Date();
		// 文件名
		String fileName = String.valueOf(createTime.getTime());
		// 产生文件路径
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		// 商户相对目录
		String merchantPath = dateFormat.format(createTime) + "/" + merchantId.toString() + "/";
		// 任务文件包相对路径
		String packagePath = merchantPath + fileName + "/";

		// //任务包的绝对路径
		String folderPath = taskPackagePath + packagePath;

		// 任务文件的相对路径
		String relativeFilePath = packagePath + fileName;
		// 任务文件的绝对路径
		String absoluteFilePath = taskPackagePath + relativeFilePath;
		String txtFilePath = absoluteFilePath + ".json";
		File imgFolder = new File(folderPath + "img");
		if (!imgFolder.exists()) {
			imgFolder.mkdirs();
		}
		DefaultResourceLoader defaultResourceLoader = new DefaultResourceLoader();
		Resource resource = defaultResourceLoader.getResource("taskfiles/customer/task.sh");// new
		FileUtils.copyFileToDirectory(resource.getFile(), new File(folderPath));
		movePicAndProcessPicFileName(menus, imgFolder);
		File file = new File(txtFilePath);
		// System.out.println("===" + objectMapper.writeValueAsString(menus));
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(objectMapper.writeValueAsString(menus));
		bw.flush();
		bw.close();
		File gzipFile = TarGZipUtils.tarGzip(folderPath, true);
		TaskPackage taskPackage = new TaskPackage();
		taskPackage.setCreateTime(createTime);
		taskPackage.setTaskId(createTime.getTime());
		taskPackage.setId(ObjectId.get());
		taskPackage.setMerchantId(merchantId);
		taskPackage.setType(TaskConstants.INCREMENT_PACKAGE);
		taskPackage.setRemoteUrl(merchantPath + gzipFile.getName());
		taskPackage.setMd5(MD5.md5sum(gzipFile.getAbsolutePath()));
		mongoTemplate.save(taskPackage);
	}

	private void movePicAndProcessPicFileName(List<ElectricMenu> menus, File imgFolder) throws IOException {
		for (ElectricMenu menu : menus) {
			for (ElectricMenuItem item : menu.getItems()) {
				File file = new File(baseService.getPicPath() + item.getAbstractPicPath());
				FileUtils.copyFileToDirectory(file, imgFolder);
				item.setAbstractPicPath(file.getName());
			}
		}
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

}
