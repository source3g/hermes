package com.source3g.hermes.entity.customer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.source3g.hermes.constants.Constants;
import com.source3g.hermes.dto.customer.CustomerDto;
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
	
	private Boolean favorite;//常用联系人

	private List<CallRecord> callRecords;

	private List<Remind> reminds;
	private ObjectId merchantId;
	private Date lastCallInTime; // 最后通电话时间
	@DBRef
	private CustomerGroup customerGroup;
	private Date operateTime;

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

	public CustomerGroup getCustomerGroup() {
		return customerGroup;
	}

	public void setCustomerGroup(CustomerGroup customerGroup) {
		this.customerGroup = customerGroup;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	@JsonIgnore
	public String toInsertOrUpdateSql() throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		SimpleModule module = new SimpleModule("dateModule", new Version(0, 0, 1, null));
		module.addSerializer(ObjectId.class, new ObjectIdSerializer());
		module.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
		module.addSerializer(Date.class, new CustomDateSerializer());
		module.addDeserializer(Date.class, new CustomDateDeserializer());

		objectMapper.getSerializationConfig().setSerializationInclusion(org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion.NON_NULL);
		objectMapper.registerModule(module);
		SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
		serializationConfig.addMixInAnnotations(CustomerDto.class, CustomerForSyncIntf.class);
		CustomerDto customerDto = new CustomerDto();
		EntityUtils.copyCustomerEntityToDto(this, customerDto);
		String strJson = objectMapper.writer().writeValueAsString(customerDto);
		SimpleDateFormat sdf=new SimpleDateFormat(Constants.DATE_FORMAT);
		String lastCallInTimeStr="";
		if(lastCallInTime!=null){
			lastCallInTimeStr=sdf.format(lastCallInTime);
		}
		return "REPLACE INTO CUSTOMER (phone,name,sex,lastCallInTime ,callInCount,favorite,groupId ,content) values('" + phone + "','"+name+"','"+sex+"','"+lastCallInTimeStr+"','"+customerDto.getCallInCount()+"','"+(favorite==null?"false":favorite)+"','"+(customerGroup==null?"":customerGroup.getId())+"','" + strJson + "'); ";
	}

	@JsonIgnore
	public String toDeleteSql() {
		return "delete from  CUSTOMER where phone='" + phone + "'";
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
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
