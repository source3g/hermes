package com.source3g.hermes.dto.dictionary;

import java.util.List;

import com.source3g.hermes.entity.dictionary.MerchantTagNode;

public class MerchantTagDto {
	private List<MerchantTagNode> nodes;
	public List<MerchantTagNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<MerchantTagNode> nodes) {
		this.nodes = nodes;
	}
}
