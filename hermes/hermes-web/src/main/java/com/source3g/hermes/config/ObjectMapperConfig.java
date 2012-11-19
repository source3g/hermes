package com.source3g.hermes.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.source3g.hermes.utils.DateFormateUtils;

@Configuration
public class ObjectMapperConfig {

	/**
	 * 去掉json中的空
	 * 
	 * @return
	 * @throws Exception
	 */
	public @Bean
	ObjectMapper objectMapper() throws Exception {
		ObjectMapper obj = new ObjectMapper();
		SimpleModule module = new SimpleModule("dateModule", new Version(0, 0, 1, null));
		module.addSerializer(Date.class, new CustomDateSerializer());
		module.addDeserializer(Date.class, new CustomDateDeserializer());
		module.addSerializer(ObjectId.class, new ObjectIdSerializer());
		module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
		obj.registerModule(module);
		// obj.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
		obj.getSerializationConfig().setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
		return obj;
	}

	public class CustomDateSerializer extends JsonSerializer<Date> {
		@Override
		public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			SimpleDateFormat formatterLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = formatterLong.format(value);
			jgen.writeString(formattedDate);
		}
	}

	public class CustomDateDeserializer extends JsonDeserializer<Date> {
		@Override
		public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			String unformatedDate = jp.getText();
			return DateFormateUtils.getDate(unformatedDate);
		}
	}

	public class ObjectIdSerializer extends JsonSerializer<ObjectId> {
		@Override
		public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(value.toString());
		}
	}

	public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
		@Override
		public ObjectId deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
			String objectIdStr = jp.getText();
			if (StringUtils.isEmpty(objectIdStr)) {
				return null;
			}
			return new ObjectId(objectIdStr);
		}
	}
}
