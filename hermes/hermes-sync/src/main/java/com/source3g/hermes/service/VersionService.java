package com.source3g.hermes.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.entity.device.GrayUpdateDevices;
import com.source3g.hermes.utils.Page;
import com.sourse3g.hermes.apkVersion.ApkVersion;
import com.sourse3g.hermes.apkVersion.OnlineVersion;
import com.sourse3g.hermes.apkVersion.OnlineVersion.VersionType;

@Service
public class VersionService extends BaseService {

	@Value(value = "${version.upload.dir}")
	private String uploadDir;
	@Value(value = "${local.url}")
	private String localUrl;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public void addVersion(ApkVersion apkVersion) {
		mongoTemplate.insert(apkVersion);
	}
	//测试版apk信息
	public OnlineVersion getBetaVersion() {
		OnlineVersion betaVersionsuper=mongoTemplate.findOne(new Query(Criteria.where("versionType").is(VersionType.BETA)), OnlineVersion.class);
		if(betaVersionsuper!=null){
			return betaVersionsuper;
		}
		return null;
	}
	//稳定版apk信息
	public OnlineVersion releaseVersion() {
		return super.findOne(new Query(Criteria.where("versionType").is(VersionType.RELEASE)), OnlineVersion.class);
	}
	//升级时返回最新测试版apk
	public ApkVersion getOnlineVersion() {
		OnlineVersion onlineVersion = super.findOne(new Query(Criteria.where("versionType").is(VersionType.BETA)), OnlineVersion.class);
		ApkVersion apkVersion = super.findOne(new Query(Criteria.where("apkVersion").is(onlineVersion.getApkVersion())), ApkVersion.class);
		return apkVersion;
	}
	
	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public Page versionList(int pageNoInt) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, ApkVersion.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<ApkVersion> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), ApkVersion.class);
		page.setData(list);
		return page;
	}
	//更改稳定版apk版本
	public void changeVersion(String version) {
		mongoTemplate.upsert(new Query(Criteria.where("versionType").is(VersionType.RELEASE)), new Update().set("apkVersion", version), OnlineVersion.class);
		OnlineVersion bateOnlineVersion=mongoTemplate.findOne(new Query(Criteria.where("versionType").is(VersionType.BETA)), OnlineVersion.class);
		//测试版与稳定版 版本号相同时 测试版清空 返回稳定版
		if(version.equals(bateOnlineVersion.getApkVersion())){
			mongoTemplate.remove(bateOnlineVersion);
		}
	}
	//更新测试版版本号
	public void changeBetaVersion(String version) {
		mongoTemplate.upsert(new Query(Criteria.where("versionType").is(VersionType.BETA)), new Update().set("apkVersion", version), OnlineVersion.class);
	}
	
	
	public void updateDeviceVersion(String sn, String version) {
		mongoTemplate.updateFirst(new Query(Criteria.where("sn").is(sn)), new Update().set("apkVersion", version), Device.class);
	}

	public Boolean versionValidate(String version) {
		Boolean result = true;
		List<ApkVersion> list = mongoTemplate.find(new Query(Criteria.where("apkVersion").is(version)), ApkVersion.class);
		if (list.size() > 0) {
			result = false;
			return result;
		}
		return result;
	}

	public Boolean codeValidate(int codeInt) {
		Boolean result = true;
		List<ApkVersion> list = mongoTemplate.find(new Query(Criteria.where("code").is(codeInt)), ApkVersion.class);
		if (list.size() > 0) {
			result = false;
			return result;
		}
		return result;
	}
	public Page GrayUpdateDevicesList(int pageNoInt) {
		GrayUpdateDevices grayUpdateDevices = super.findOne((new Query()), GrayUpdateDevices.class);
		Page page = new Page();
		if(grayUpdateDevices==null){
			GrayUpdateDevices gud=new GrayUpdateDevices();
			gud.setId(new ObjectId());
			gud.setDeviceIds(new ArrayList<ObjectId>());
			page.setTotalRecords(gud.getDeviceIds().size());
			mongoTemplate.insert(gud);
			page.gotoPage(pageNoInt);
			List<Device> devices=mongoTemplate.find(new Query(Criteria.where("_id").in(gud.getDeviceIds())).skip(page.getStartRow()).limit(page.getPageSize()), Device.class);
			page.setData(devices);
		}else{
			page.setTotalRecords(grayUpdateDevices.getDeviceIds().size());
			page.gotoPage(pageNoInt);
			List<Device> devices=mongoTemplate.find(new Query(Criteria.where("_id").in(grayUpdateDevices.getDeviceIds())).skip(page.getStartRow()).limit(page.getPageSize()), Device.class);
			page.setData(devices);
		}
		return page;
	}
	public void addToGrayUpdateDevicesList(String sn) {
		Device device=mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if(device==null){
			return ;
		}
		GrayUpdateDevices grayUpdateDevices=mongoTemplate.findOne(new Query(), GrayUpdateDevices.class);
		List<ObjectId> deviceIds=grayUpdateDevices.getDeviceIds();
			for(ObjectId o:deviceIds){
				if(o.equals(device.getId())){
					return;
				}
			}
			deviceIds.add(device.getId());
			mongoTemplate.updateFirst(new Query(), new Update().set("deviceIds",deviceIds ), GrayUpdateDevices.class);
	}

	public Boolean DeviceSnValidate(String sn) {
		Device device=mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if(device!=null){
			return true;
		}
		return false;
	}

	public void deleteGrayUpdateDevice(ObjectId id) {
		GrayUpdateDevices grayUpdateDevices=mongoTemplate.findOne(new Query(), GrayUpdateDevices.class);
		List<ObjectId> list=grayUpdateDevices.getDeviceIds();
		for(int i=0;i<list.size();i++){
			if(list.get(i).equals(id)){
				list.remove(i);
			}
		}
		mongoTemplate.updateFirst(new Query(), new Update().set("deviceIds", list),GrayUpdateDevices.class);
	}


}
