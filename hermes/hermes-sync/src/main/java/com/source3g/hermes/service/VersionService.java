package com.source3g.hermes.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.sourse3g.hermes.apkVersion.ApkVersion;

@Service
public class VersionService {
	@Autowired
	private MongoTemplate mongoTemplate;

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

	public ApkVersion getLastVersion() {
		return mongoTemplate.findOne(new Query().with(new Sort(Direction.DESC, "uploadTime")).limit(1), ApkVersion.class);
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}

	public List<ApkVersion> versionList() {
		Query query=new Query();
		query.with(new Sort(Direction.DESC, "_id"));
		 List<ApkVersion> list= mongoTemplate.find(query, ApkVersion.class);
		return list;
	}

}
