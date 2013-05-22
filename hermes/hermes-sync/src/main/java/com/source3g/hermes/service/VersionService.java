package com.source3g.hermes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.device.Device;
import com.source3g.hermes.utils.Page;
import com.sourse3g.hermes.apkVersion.ApkVersion;
import com.sourse3g.hermes.apkVersion.OnlineVersion;

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

	public ApkVersion getOnlineVersion() {
		OnlineVersion onlineVersion = super.findOne(new Query(), OnlineVersion.class);
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

	public void changeVersion(String version) {
		mongoTemplate.upsert(new Query(), new Update().set("apkVersion", version), OnlineVersion.class);
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
}
