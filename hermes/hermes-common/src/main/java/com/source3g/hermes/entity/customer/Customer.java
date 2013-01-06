package com.source3g.hermes.entity.customer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.customer.dto.CustomerDto;
import com.source3g.hermes.entity.AbstractEntity;
import com.source3g.hermes.enums.Sex;
import com.source3g.hermes.utils.DateFormateUtils;
import com.source3g.hermes.utils.EntityUtils;

@Document
@CompoundIndexes({ @CompoundIndex(name = "merchant_phone", def = "{'phone': 1, 'merchantId': -1}", unique = true) })
public class Customer extends AbstractEntity {
	private static final long serialVersionUID = 6014996097125743375L;
	@Indexed(name = "CUSTOMER_NAME_INDEX")
	private String name;
	private Sex sex;
	private String birthday;
	private String phone;
	private boolean blackList;
	private String address;
	private List<String> otherPhones;
	private String qq;
	private String email;
	private String note;

	private List<CallRecord> callRecords;

	private List<Remind> reminds;
	private ObjectId merchantId;
	private Date lastCallInTime; // 最后通电话时间
	
	private ObjectId customerGroupId;
	private Date operateTime;

	// @JsonIgnore
	public int getCallInCountToday() {
		int count = 0;
		Date date = new Date();
		Date startDate = DateFormateUtils.getStartDateOfDay(date);
		if (CollectionUtils.isEmpty(callRecords)) {
			return count;
		}
		for (CallRecord callRecord : callRecords) {
			if (startDate.getTime() < callRecord.getCallTime().getTime()) {
				count++;
			}
		}
		return count;
	}

	public void setCallInCountToday(int count) {
		// 空，为了对应反序列化
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isBlackList() {
		return blackList;
	}

	public void setBlackList(boolean blackList) {
		this.blackList = blackList;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getOtherPhones() {
		return otherPhones;
	}

	public void setOtherPhones(List<String> otherPhones) {
		this.otherPhones = otherPhones;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(ObjectId merchantId) {
		this.merchantId = merchantId;
	}

	public List<Remind> getReminds() {
		return reminds;
	}

	public void setReminds(List<Remind> reminds) {
		this.reminds = reminds;
	}

	public List<CallRecord> getCallRecords() {
		return callRecords;
	}

	public void setCallRecords(List<CallRecord> callRecords) {
		this.callRecords = callRecords;
	}

	public Date getLastCallInTime() {
		return lastCallInTime;
	}

	public void setLastCallInTime(Date lastCallInTime) {
		this.lastCallInTime = lastCallInTime;
	}

	public ObjectId getCustomerGroupId() {
		return customerGroupId;
	}

	public void setCustomerGroupId(ObjectId customerGroupId) {
		this.customerGroupId = customerGroupId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@JsonIgnore
	public String toInsertOrUpdateSql() throws JsonGenerationException, JsonMappingException, IOException {
		// FilterProvider filterProvider = new
		// SimpleFilterProvider().addFilter("filterPropreties",
		// SimpleBeanPropertyFilter.serializeAllExcept("otherPhones",
		// "callRecords", "operateTime"));
		// SimpleBeanPropertyFilter filter =
		// SimpleBeanPropertyFilter.serializeAllExcept("otherPhones",
		// "callRecords", "operateTime");
		// FilterProvider fp = new
		// SimpleFilterProvider().addFilter("filterForSync", filter);
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("dateModule", new Version(0, 0, 1, null));
		module.addSerializer(ObjectId.class, new ObjectIdSerializer());
		module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
		module.addSerializer(Date.class, new CustomDateSerializer());
		module.addDeserializer(Date.class, new CustomDateDeserializer());

		objectMapper.getSerializationConfig().setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
		objectMapper.registerModule(module);
		SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
		serializationConfig.addMixInAnnotations(Customer.class, CustomerForSyncIntf.class);
		CustomerDto customerDto = new CustomerDto();
		EntityUtils.copyCustomerEntityToDto(this, customerDto);
		String strJson = objectMapper.writer().writeValueAsString(customerDto);

		// {"otherPhones":null,"merchantId":{"timeSecond":1353907038,"machine":-748133974,"inc":-1763053068,"time":1353907038000,"new":false},"callRecords":null,"lastCallInTime":1353986159566,"customerGroupId":{"timeSecond":1353907038,"machine":-748133974,"inc":-1763053068,"time":1353907038000,"new":false},"operateTime":null,"birthday":"80-15","phone":"13644058759","blackList":false,"qq":"123456","email":"123@123.com","note":"爱吃红烧肉","sex":"MALE","reminds":[{"alreadyRemind":true,"advancedTime":"1","remindTime":1353986159568,"name":"红酒到期"},{"alreadyRemind":true,"advancedTime":"1","remindTime":1353986159568,"name":"红酒到期"}],"name":"张三","address":"北京市","id":{"timeSecond":1353907038,"machine":-748133974,"inc":-1763053068,"time":1353907038000,"new":false}}
		// StringBuffer stringBuffer=new StringBuffer();
		// String quote="\"";
		// stringBuffer.append("{name:");
		// stringBuffer.append(quote);
		// stringBuffer.append(name);
		// stringBuffer.append(quote);

		return "REPLACE INTO CUSTOMER (phone,content) values('" + phone + "','" + strJson + "'); ";
	}

	@JsonIgnore
	public String toDeleteSql() {
		return "delete from  CUSTOMER where phone='" + phone + "'";
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

}
