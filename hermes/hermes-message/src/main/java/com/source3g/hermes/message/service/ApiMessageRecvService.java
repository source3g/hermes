package com.source3g.hermes.message.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.source3g.hermes.enums.MessageStatus;
import com.source3g.hermes.message.utils.ReportStatus;

@Component
public class ApiMessageRecvService extends AbstractRecvService {

	private static final Logger logger = LoggerFactory.getLogger(ApiMessageRecvService.class);

	@Value(value = "${message.api.youzhi.username}")
	private String username;
	@Value(value = "${message.api.youzhi.password}")
	private String password;

	@Value(value = "${message.api.dichan.username}")
	private String dcUsername;
	@Value(value = "${message.api.dichan.password}")
	private String dcPassword;

	@Value(value = "${message.api.recv.url}")
	private String recvUrl;
	@Autowired
	private org.codehaus.jackson.map.ObjectMapper objectMapper;

	@Override
	public void recv() throws Exception {
		recvByUser(username, password);
		recvByUser(dcUsername, dcPassword);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void recvByUser(String u, String p) throws Exception {
		PostMethod postMethod = new PostMethod(recvUrl);
		try {
			postMethod.addParameter("username", u);
			postMethod.addParameter("password", p);
			HttpClient httpClient = new HttpClient();
			httpClient.executeMethod(postMethod);
			String result = postMethod.getResponseBodyAsString();
			boolean bool = objectMapper.canDeserialize(TypeFactory.type(List.class));
			if (bool) {
				List<Map> l = objectMapper.readValue(result, List.class);
				for (Map m : l) {
					ReportStatus reportStatus = ReportStatus.valueOf(m.get("status").toString());
					MessageStatus messageStatus = MessageStatus.发送成功;
					if (ReportStatus.FAILED.equals(reportStatus)) {
						messageStatus = MessageStatus.发送失败;
					}
					updateLog(m.get("msgId").toString(), messageStatus);
				}
			} else {
				logger.debug("短信接收失败" + result);
			}

			// postMethod.releaseConnection();
			// postMethod = new PostMethod(recvUrl);
			// postMethod.addParameter("username", dcUsername);
			// postMethod.addParameter("password", dcPassword);
			// httpClient.executeMethod(postMethod);
			// String result = postMethod.getResponseBodyAsString();
			// List l = objectMapper.readValue(result, List.class);
			// System.out.println(l);
		} catch (Exception e) {
			logger.debug("接收失败" + e.getLocalizedMessage());
		} finally {
			postMethod.releaseConnection();
		}
	}

}
