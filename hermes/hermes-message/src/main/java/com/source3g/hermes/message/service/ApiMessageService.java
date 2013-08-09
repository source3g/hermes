package com.source3g.hermes.message.service;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.enums.MerchantMessageType;

@Service
@Lazy(value = false)
public class ApiMessageService extends AbstractPositiveMessageService {
	@Value(value = "${message.api.url}")
	private String apiUrl;
	@Value(value = "${message.api.youzhi.username}")
	private String youzhiUsername;

	@Value(value = "${message.api.youzhi.password}")
	private String youzhiPassword;
	@Value(value = "${message.api.dichan.username}")
	private String dcUsername;
	@Value(value = "${message.api.dichan.password}")
	private String dcPassword;

	@Override
	protected String send(ShortMessage shortMessage) throws Exception {
		shortMessage.setMsgId(ObjectId.get().toString());
		String username = youzhiUsername;
		String password = youzhiPassword;
		if (MerchantMessageType.地产类商户.equals(shortMessage.getMerchantType())) {
			username = dcUsername;
			password = dcPassword;
		}
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(apiUrl);
		postMethod.addParameter("username", username);
		postMethod.addParameter("password", password);
		postMethod.addParameter("phone", shortMessage.getPhone());
		postMethod.addParameter("content", shortMessage.getContent());
		postMethod.getParams().setContentCharset("UTF-8");
		try {
			httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			postMethod.releaseConnection();
		}
		return shortMessage.getMsgId();
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getYouzhiUsername() {
		return youzhiUsername;
	}

	public void setYouzhiUsername(String youzhiUsername) {
		this.youzhiUsername = youzhiUsername;
	}

	public String getYouzhiPassword() {
		return youzhiPassword;
	}

	public void setYouzhiPassword(String youzhiPassword) {
		this.youzhiPassword = youzhiPassword;
	}

	public String getDcUsername() {
		return dcUsername;
	}

	public void setDcUsername(String dcUsername) {
		this.dcUsername = dcUsername;
	}

	public String getDcPassword() {
		return dcPassword;
	}

	public void setDcPassword(String dcPassword) {
		this.dcPassword = dcPassword;
	}

	public static void main(String[] args) throws HttpException, IOException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod("http://58.68.229.179/sms-platform/api/sms/send.htm");
		postMethod.addParameter("username", "wcb");
		postMethod.addParameter("password", "wcb123");
		postMethod.addParameter("phone", "18600217379");
		postMethod.addParameter("content", "尊敬的肇斌先生: \n测试11");
		postMethod.getParams().setContentCharset("UTF-8");
		httpClient.executeMethod(postMethod);
	}
}
