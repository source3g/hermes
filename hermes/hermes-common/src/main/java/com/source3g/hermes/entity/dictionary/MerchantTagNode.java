package com.source3g.hermes.entity.dictionary;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.entity.AbstractEntity;

@Document
public class MerchantTagNode extends AbstractEntity {
	private static final long serialVersionUID = 6126138616863825251L;

	private String name;
	private ObjectId parentId;

	@Transient
	private List<MerchantTagNode> children;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getParentId() {
		return parentId;
	}

	public void setParentId(ObjectId parentId) {
		this.parentId = parentId;
	}

	public List<MerchantTagNode> getChildren() {
		return children;
	}

	public void setChildren(List<MerchantTagNode> children) {
		this.children = children;
	}

	@Override
	public boolean equals(Object nodeObj) {
		if(nodeObj.getClass()!=this.getClass()){
			return false;
		}
		MerchantTagNode node=(MerchantTagNode)nodeObj;
		if(node.getId()==null){
			return false;
		}
		return node.getId().equals(this.getId());
	}

	@Override
	public int hashCode() {
		if(this.getId()==null){
			return super.hashCode();
		}
		return this.getId().hashCode();
	}

}
