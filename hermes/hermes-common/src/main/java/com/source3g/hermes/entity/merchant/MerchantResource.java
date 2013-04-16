package com.source3g.hermes.entity.merchant;

import java.io.Serializable;
import java.util.List;

public class MerchantResource implements Serializable {

	private static final long serialVersionUID = 4076472085155596973L;
	private String messageContent;
	private List<String> resourceList;



	public List<String> getResourceList() {
		return resourceList;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

}
