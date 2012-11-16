package com.source3g.hermes.config;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

public class MyWebBindingInitializer implements WebBindingInitializer {

	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DatePropertyEditor());
		binder.registerCustomEditor(ObjectId.class, new ObjectIdPropertyEditor());
	}

}
