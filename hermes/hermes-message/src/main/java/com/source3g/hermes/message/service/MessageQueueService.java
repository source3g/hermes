package com.source3g.hermes.message.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.message.ShortMessage;
import com.source3g.hermes.service.BaseService;

@Service
public class MessageQueueService extends BaseService{
	private static final String queueCollection="messageQueue";
	
	public void add(ShortMessage shortMessage){
		mongoTemplate.save(shortMessage,queueCollection);
	}
	public void addBatch(List<ShortMessage> shortMessages){
		mongoTemplate.save(shortMessages,queueCollection);
	}
	
	public ShortMessage poll(){
		ShortMessage shortMessage =mongoTemplate.findAndRemove(new Query().with(new Sort(Direction.ASC,"priority")), ShortMessage.class, queueCollection);
		return shortMessage;
	}
}
