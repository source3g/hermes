package com.source3g.hermes.dictionary.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.source3g.hermes.service.BaseService;
import com.sourse3g.hermes.branch.BranchCompany;
import com.sourse3g.hermes.branch.Saler;

@Service
public class BranchAndSalersService extends BaseService {

	public List<BranchCompany> showBranchCompany() {
		List<BranchCompany> c = mongoTemplate.findAll(BranchCompany.class, "branchCompany");
		return c;
	}

	public Saler addSaler(String salerName, ObjectId branchCompanyId) {
		List<Saler> salers = mongoTemplate.find(new Query(Criteria.where("name").is(salerName).and("branchCompanyId").is(branchCompanyId)), Saler.class);
		if (salers.size() > 0) {
			return null;
		}
		Saler s = new Saler();
		s.setName(salerName);
		s.setBranchCompanyId(branchCompanyId);
		s.setId(ObjectId.get());
		mongoTemplate.insert(s);
		return s;
	}

	public List<Saler> showSalers(String id) {
		return mongoTemplate.find(new Query(Criteria.where("branchCompanyId").is(new ObjectId(id))), Saler.class);
	}

	public BranchCompany addBranchCompany(String branchCompanyName) {
		BranchCompany b = new BranchCompany();
		b.setName(branchCompanyName);
		b.setId(ObjectId.get());
		mongoTemplate.insert(b);
		return b;
	}

	public void deleteSaler(String id) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), Saler.class);
	}

	public void deleteBranch(String id) {
		mongoTemplate.remove(new Query(Criteria.where("branchCompanyId").is(new ObjectId(id))), Saler.class);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), BranchCompany.class);
	}

}
