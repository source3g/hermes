package com.source3g.hermes.service;

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
			List<GrayUpdateDevices> grayUpdateDevices=mongoTemplate.find(new Query(Criteria.where("apkVersion").ne(version)), GrayUpdateDevices.class);
			for(int i=0;i<grayUpdateDevices.size();i++){
				mongoTemplate.remove(grayUpdateDevices.get(i));
			}
			mongoTemplate.remove(bateOnlineVersion);
		}
	}
	//更新测试版版本号
	public void changeBetaVersion(String version) {
		mongoTemplate.upsert(new Query(Criteria.where("versionType").is(VersionType.BETA)), new Update().set("apkVersion", version), OnlineVersion.class);
	}
	
	//灰色设备列表盒子升级 比较是否为最新版本
	public void updateGrayUpdateDevices(String sn, String version) {
		OnlineVersion onlineVersion = super.findOne(new Query(Criteria.where("versionType").is(VersionType.BETA)), OnlineVersion.class);
		if(!onlineVersion.getApkVersion().equals(version)){
		mongoTemplate.upsert(new Query(Criteria.where("sn").is(sn)),new Update().set("apkVersion", version),GrayUpdateDevices.class);
	}
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
		Boolean result=true;
		List<ApkVersion> list=mongoTemplate.find(new Query(Criteria.where("code").is(codeInt)), ApkVersion.class);
		if(list.size()>0){
			result=false;
			return result;
		}
		return result;
	}
	public Page GrayUpdateDevicesList(int pageNoInt) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		Page page = new Page();
		Long totalCount = mongoTemplate.count(query, GrayUpdateDevices.class);
		page.setTotalRecords(totalCount);
		page.gotoPage(pageNoInt);
		List<GrayUpdateDevices> list = mongoTemplate.find(query.skip(page.getStartRow()).limit(page.getPageSize()), GrayUpdateDevices.class);
		page.setData(list);
		return page;
	}
	public void addToGrayUpdateDevicesList(String sn) {
		Device device=mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if(device==null){
			return ;
		}
		GrayUpdateDevices g=mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), GrayUpdateDevices.class);
		if(g!=null){
			return;
		}
		GrayUpdateDevices grayUpdateDevices=new GrayUpdateDevices();
		grayUpdateDevices.setApkVersion(device.getApkVersion());
		grayUpdateDevices.setSn(sn);
		grayUpdateDevices.setId(new ObjectId());
		mongoTemplate.insert(grayUpdateDevices);
	}

	public Boolean DeviceSnValidate(String sn) {
		Device device=mongoTemplate.findOne(new Query(Criteria.where("sn").is(sn)), Device.class);
		if(device!=null){
			return true;
		}
		return false;
	}

	public void deleteGrayUpdateDevice(ObjectId id) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(id)), GrayUpdateDevices.class);
	}


}
