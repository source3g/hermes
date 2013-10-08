package com.source3g.hermes.config;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class MyWebBindingInitializer implements WebBindingInitializer {

	private Validator validator;

	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.setValidator(validator);
		binder.registerCustomEditor(Date.class, new DatePropertyEditor());
		binder.registerCustomEditor(ObjectId.class, new ObjectIdPropertyEditor());
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		//binder.registerCustomEditor(MerchantMessageType.class, new MerchantMessageTypePropertyEditor());
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

}
