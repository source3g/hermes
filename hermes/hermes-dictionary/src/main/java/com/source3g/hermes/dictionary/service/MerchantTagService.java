package com.source3g.hermes.dictionary.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.dictionary.MerchantTagNode;
import com.source3g.hermes.service.BaseService;

@Service
public class MerchantTagService extends BaseService {

	public List<MerchantTagNode> findTopNodes() {
		Query query = new Query();
		query.addCriteria(Criteria.where("parent").is(null));
		return mongoTemplate.find(query, MerchantTagNode.class);
	}

	public void save(List<MerchantTagNode> nodes) {
		for (MerchantTagNode node : nodes) {
			mongoTemplate.save(node);
		}
	}

	public List<MerchantTagNode> findAllNodes() {
		List<MerchantTagNode> topNodes = findTopNodes();
		fillTree(topNodes);
		return topNodes;
	}

	public List<MerchantTagNode> findByParent(ObjectId parentId) {
		return mongoTemplate.find(new Query(Criteria.where("parentId").is(parentId)), MerchantTagNode.class);
	}

	public void fillTree(List<MerchantTagNode> nodes) {
		if (CollectionUtils.isEmpty(nodes)) {
			return;
		}
		for (MerchantTagNode node : nodes) {
			List<MerchantTagNode> children = findByParent(node.getId());
			if (CollectionUtils.isEmpty(children)) {
				node.setChildren(null);
			} else {
				node.setChildren(children);
			}
			fillTree(children);
		}
	}

}
