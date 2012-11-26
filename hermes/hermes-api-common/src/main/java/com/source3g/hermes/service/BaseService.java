package com.source3g.hermes.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.entity.AbstractEntity;

@Component
public abstract class BaseService {

	@Autowired
	protected MongoTemplate mongoTemplate;

	public <T> void deleteById(String id, Class<T> t) {
		ObjectId objId = new ObjectId(id);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(objId)), t);
	}

	public <T extends AbstractEntity> void add(T entity) {
		if (entity == null) {
			return;
		} else if (entity.getId() == null) {
			entity.setId(ObjectId.get());
		}
		mongoTemplate.insert(entity);
	}

	public <T extends AbstractEntity> void update(T entity) {
		if (entity == null || entity.getId() == null) {
			return;
		}
		// mongoTemplate.updateFirst(query, new Update()., collectionName)
		mongoTemplate.save(entity);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends AbstractEntity> void updateExcludeProperties(T entity, String... properties) {
		try {
			List list = Arrays.asList(properties);
			Class classType = entity.getClass();
			Field fields[] = classType.getDeclaredFields();
			Update update = new Update();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				if (!list.contains(fieldName)) {
					String firstLetter = fieldName.substring(0, 1).toUpperCase();
					// 获得和属性对应的getXXX()方法的名字
					String getMethodName;
					if (fields[i].getType() == boolean.class) {
						getMethodName = "is" + firstLetter + fieldName.substring(1);
					} else {
						getMethodName = "get" + firstLetter + fieldName.substring(1);
					}

					// 获得和属性对应的setXXX()方法的名字
					// String setMethodName = "set" + firstLetter +
					// fieldName.substring(1);
					// 下面是组装对应的get/set方法

					Method getMethod = classType.getMethod(getMethodName, new Class[] {});
					// Method setMethod = classType.getMethod(setMethodName, new
					// Class[] { fields[i].getType() });
					// 调用原对象的get方法
					Object value = getMethod.invoke(entity, new Object[] {});

					update.set(fieldName, value);
					// 调用赋值对象的set方法
					// setMethod.invoke(entityInDb, new Object[] { value });
				}
			}
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(entity.getId())), update, entity.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends AbstractEntity> void updateIncludeProperties(T entity, String... properties) {
		try {
			Update update = new Update();
			Class classType = entity.getClass();
			for (int i = 0; i < properties.length; i++) {
				String fieldName = properties[i];
				String firstLetter = fieldName.substring(0, 1).toUpperCase();
				Field field = classType.getDeclaredField(fieldName);
				// 获得和属性对应的getXXX()方法的名字
				String getMethodName;
				if (field.getType() == boolean.class) {
					getMethodName = "is" + firstLetter + fieldName.substring(1);
				} else {
					getMethodName = "get" + firstLetter + fieldName.substring(1);
				}

				// 下面是组装对应的get/set方法
				Method getMethod = classType.getMethod(getMethodName, new Class[] {});

				// 调用原对象的get方法
				Object value = getMethod.invoke(entity, new Object[] {});
				// 调用赋值对象的set方法
				// setMethod.invoke(entityInDb, new Object[] { value });
				update.set(fieldName, value);
			}
			mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(entity.getId())), update, entity.getClass());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
