package com.source3g.hermes.entity.merchant;

import java.io.Serializable;
import java.util.List;

public class MerchantResource implements Serializable {

	private static final long serialVersionUID = 4076472085155596973L;
	private String prefix;
	private String suffix;
	private List<String> list;

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

}
