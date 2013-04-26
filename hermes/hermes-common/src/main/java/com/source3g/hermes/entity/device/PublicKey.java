package com.source3g.hermes.entity.device;

import com.source3g.hermes.entity.AbstractEntity;

public class PublicKey extends AbstractEntity {
	private static final long serialVersionUID = -4366797209994573601L;
	private String publickey;
	public String getPublickey() {
		return publickey;
	}
	public void setPublickey(String publickey) {
		this.publickey = publickey;
	}
}
