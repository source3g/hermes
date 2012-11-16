package com.source3g.hermes.api.config;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;

public class ObjectIdPropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		ObjectId objectId = (ObjectId) getValue();
		return objectId.toString();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isEmpty(text)) {
			return;
		}
		setValue(new ObjectId(text));
	}

}
