package com.source3g.hermes.utils;

import org.apache.commons.lang.StringUtils;

import com.source3g.hermes.enums.PhoneOperator;

public class PhoneUtils {
	public static final String cm = "^((13[4-9])|(147)|(15[0-2,7-9])|(18[2-3,7-8]))\\d{8}$";
	public static final String cu = "^((13[0-2])|(145)|(15[5-6])|(186))\\d{8}$";
	public static final String ct = "^((133)|(153)|(18[0,9]))\\d{8}$";

	public static PhoneOperator getOperatior(String phoneNumber) {
		if (StringUtils.isEmpty(phoneNumber)) {
			return PhoneOperator.其它;
		}
		PhoneOperator operator;
		if (phoneNumber.matches(cm)) {
			operator = PhoneOperator.移动;
		} else if (phoneNumber.matches(cu)) {
			operator = PhoneOperator.联通;
		} else if (phoneNumber.matches(ct)) {
			operator = PhoneOperator.电信;
		} else {
			operator = PhoneOperator.其它;
		}
		return operator;
	}
}
