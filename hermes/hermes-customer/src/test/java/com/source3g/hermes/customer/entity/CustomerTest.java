package com.source3g.hermes.customer.entity;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;

public class CustomerTest {

	private ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void init() {
	}

	@Test
	public void toJson() throws JsonGenerationException, JsonMappingException, IOException {
		Customer customer = new Customer();
		customer.setPhone("112233");
		customer.setReminds(new ArrayList<Remind>());
		System.out.println(objectMapper.writeValueAsString(customer));
	}

}
