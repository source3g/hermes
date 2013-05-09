package com.source3g.hermes.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.source3g.hermes.entity.AbstractEntity;

@Component
public class BaseService  implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;
	
	@Value(value = "${image.menu.dir}")
	private String picPath;
	
	@Value(value = "${local.url}")
	private String localUrl;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

		BaseService.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String beanName) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanName, clazz);
	}

	

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

	public <T extends AbstractEntity> void save(T entity) {
		if (entity == null) {
			return;
		} else if (entity.getId() == null) {
			entity.setId(ObjectId.get());
		}
		mongoTemplate.save(entity);
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
		List list = Arrays.asList(properties);
		Class classType = entity.getClass();
		Field fields[] = classType.getDeclaredFields();
		// Update update = new Update();
		Object entityInDb = mongoTemplate.findById(entity.getId(), entity.getClass());
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
				// 获得set方法
				String setMethodName = "set" + firstLetter + fieldName.substring(1);

				// 获得和属性对应的setXXX()方法的名字
				// String setMethodName = "set" + firstLetter +
				// fieldName.substring(1);
				// 下面是组装对应的get/set方法

				Method getMethod;
				Method setMethod;
				try {
					getMethod = classType.getMethod(getMethodName, new Class[] {});
					Object value = getMethod.invoke(entity, new Object[] {});
					setMethod = classType.getMethod(setMethodName, new Class[] { getMethod.getReturnType() });
					setMethod.invoke(entityInDb, new Object[] { value });
					// update.set(fieldName, value);

				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					continue;
				}
				// Method setMethod = classType.getMethod(setMethodName, new
				// Class[] { fields[i].getType() });
				// 调用原对象的get方法

				// 调用赋值对象的set方法
				// setMethod.invoke(entityInDb, new Object[] { value });
			}
		}
		mongoTemplate.save(entityInDb);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends AbstractEntity> void updateIncludeProperties(T entity, String... properties) {
		// Update update = new Update();
		Class classType = entity.getClass();
		Object entityInDb = mongoTemplate.findById(entity.getId(), entity.getClass());
		for (int i = 0; i < properties.length; i++) {
			String fieldName = properties[i];
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			Field field;
			try {
				field = classType.getDeclaredField(fieldName);

				// 获得和属性对应的getXXX()方法的名字
				String getMethodName;
				if (field.getType() == boolean.class) {
					getMethodName = "is" + firstLetter + fieldName.substring(1);
				} else {
					getMethodName = "get" + firstLetter + fieldName.substring(1);
				}
				// 获得set方法
				String setMethodName = "set" + firstLetter + fieldName.substring(1);

				// 下面是组装对应的get/set方法
				Method getMethod = classType.getMethod(getMethodName, new Class[] {});
				Method setMethod = classType.getMethod(setMethodName, new Class[] { getMethod.getReturnType() });

				// 调用原对象的get方法
				Object value = getMethod.invoke(entity, new Object[] {});
				// 调用赋值对象的set方法
				setMethod.invoke(entityInDb, new Object[] { value });
				// update.set(fieldName, value);
			} catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				continue;
			}
		}
		// mongoTemplate.updateFirst(new
		// Query(Criteria.where("_id").is(entity.getId())), update,
		// entity.getClass());
		mongoTemplate.save(entityInDb);
	}

	// public <T extends AbstractEntity> List<T> findByBasicDBObject(Class<T> c,
	// BasicDBObject params, ObjectMapper<T> mapper) {
	// List<T> list = new ArrayList<T>();
	// DBCollection collection =
	// mongoTemplate.getCollection(mongoTemplate.getCollectionName(c));
	// DBCursor item = collection.find(params);
	// while (item.hasNext()) {
	// DBObject obj = item.next();
	// list.add(mapper.mapping(obj));
	// }
	// item.close();
	// return list;
	// }

	public <T extends AbstractEntity> long findCountByBasicDBObject(Class<T> c, BasicDBObject params) {
		DBCollection collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(c));
		return collection.count(params);
	}

	public <T extends AbstractEntity, S> List<S> findByBasicDBObject(Class<T> c, BasicDBObject params, ObjectMapper<S> mapper) {
		List<S> list = new ArrayList<S>();
		DBCollection collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(c));
		DBCursor item = collection.find(params);
		while (item.hasNext()) {
			DBObject obj = item.next();
			list.add(mapper.mapping(obj));
		}
		item.close();
		return list;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractEntity, S> List<S> findByBasicDBObject(Class<T> c, BasicDBObject params, String property, Class<S> propertyClass) {
		List<S> list = new ArrayList<S>();
		DBCollection collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(c));
		DBCursor item = collection.find(params);
		while (item.hasNext()) {
			DBObject obj = item.next();
			list.add((S) obj.get(property));
		}
		item.close();
		return list;
	}

	public interface ObjectMapper<T> {
		public T mapping(DBObject obj);
	}

	public <T extends AbstractEntity> T findOne(Query query, Class<T> entityClass) {
		T result = null;
		try {
			result = mongoTemplate.findOne(query, entityClass);
		} catch (Exception e) {

		}
		return result;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getLocalUrl() {
		return localUrl;
	}

	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}
}
