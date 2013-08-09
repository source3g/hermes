package com.source3g.hermes.customer.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.source3g.hermes.customer.service.CustomerService;
import com.source3g.hermes.entity.customer.Customer;
import com.source3g.hermes.entity.customer.Remind;
import com.source3g.hermes.entity.merchant.Merchant;

@Component
public class BirthdayScheduler {
	@Autowired
	private CustomerService customerService;
	@Autowired
	private MongoTemplate mongoTemplate;

	private static final Logger logger = LoggerFactory.getLogger(BirthdayScheduler.class);

	public void addBirthdayRemind() {
		List<Merchant> merchants = mongoTemplate.find(new Query(Criteria.where("canceled").is(false)), Merchant.class);
		for (Merchant merchant : merchants) {
			try {
				if (!merchant.getSetting().isBirthdayRemind()) {
					continue;
				}
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, merchant.getSetting().getBirthdayRemindTemplate().getAdvancedTime());
				int month = calendar.get(Calendar.MONTH) + 1;
				String monthStr = "";
				if (month < 10) {
					monthStr = "0" + month;
				} else {
					monthStr = String.valueOf(month);
				}
				String birthday = monthStr + "-" + calendar.get(Calendar.DAY_OF_MONTH);
				logger.debug(merchant.getName() + "生成生日为" + birthday + "的提醒");
				List<Customer> customersTodayBirthday = customerService.findCustomersByBirthday(merchant.getId(), birthday);
				Remind remind = new Remind();
				remind.setAlreadyRemind(false);
				remind.setMerchantRemindTemplate(merchant.getSetting().getBirthdayRemindTemplate());
				remind.setRemindTime(new Date(calendar.getTimeInMillis()));
				for (Customer c : customersTodayBirthday) {
					Update update = new Update();
					update.addToSet("reminds", remind);
					mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(c.getId())), update, Customer.class);
				}
			} catch (Exception e) {
				System.out.println(merchant.getName() + "生日提醒出了问题");
				e.printStackTrace();
			}
		}
	}
}
