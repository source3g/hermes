package com.source3g.hermes.merchant.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.note.Note;
import com.source3g.hermes.service.BaseService;

@Service
public class NoteService extends BaseService {

	public void add(Note note, String merchantId) {
		Update update = new Update();
		update.addToSet("notes", note);
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(merchantId))), update, Merchant.class);
	}

}
