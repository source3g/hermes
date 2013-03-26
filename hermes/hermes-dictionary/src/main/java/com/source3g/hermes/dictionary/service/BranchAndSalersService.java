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
		List<BranchCompany>  c=mongoTemplate.findAll(BranchCompany.class, "branchCompany");
		return c;
	}
	
	public Saler addSaler(String salerName, String branchCompanyId) {
		if("undefined".equals(branchCompanyId)){
			return null;
		}
		Saler s=new Saler();
		s.setName(salerName);
		s.setBranchCompanyId(new ObjectId(branchCompanyId));
		s.setId(ObjectId.get());
		mongoTemplate.insert(s);
		return s;
	}

	public List<Saler> showSalers(String id) {
		return mongoTemplate.find(new Query(Criteria.where("branchCompanyId").is(new ObjectId(id))), Saler.class);
	}

	public void addBranchCompany(String branchCompanyName) {
		BranchCompany b=new BranchCompany();
		b.setName(branchCompanyName);
		add(b);
	}

	public void deleteSaler(String id) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), Saler.class);
	}

	public void deleteBranch(String id) {
		mongoTemplate.remove(new Query(Criteria.where("branchCompanyId").is(new ObjectId(id))), Saler.class);
		mongoTemplate.remove(new Query(Criteria.where("_id").is(new ObjectId(id))), BranchCompany.class);
	}

}
