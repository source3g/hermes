package com.source3g.hermes.admin.security;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.admin.security.service.AdminSecurityService;
import com.source3g.hermes.entity.merchant.Merchant;
import com.source3g.hermes.entity.security.admin.Account;
import com.source3g.hermes.entity.security.admin.Resource;
import com.source3g.hermes.entity.security.admin.Role;
import com.source3g.hermes.enums.TypeEnum.LoginType;
import com.source3g.hermes.merchant.security.service.MerchantSecurityService;

@Component
public class ShiroDbRealm extends AuthorizingRealm {
	@Autowired
	private AdminSecurityService adminSecurityService;
	@Autowired
	private MerchantSecurityService merchantSecurityService;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		Account account = shiroUser.getAccount();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		if (account != null) {
			info.addRole("admin");
			for (Role role : account.getRoles()) {
				// 基于Permission的权限信息
				for (Resource resource : role.getResources())
					info.addStringPermission(resource.getCode());
			}
			return info;
		} else {
			Merchant merchant = shiroUser.getMerchant();
			if (merchant != null) {
				info.addRole("merchant");
				return info;
			}
		}
		return null;
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Subject currentUser = SecurityUtils.getSubject();
		LoginType type = (LoginType) currentUser.getSession().getAttribute("loginType");
		String userName = token.getUsername();
		if (StringUtils.isNotEmpty(userName)) {
			if (LoginType.merchant.equals(type)) {
				Merchant merchant = merchantSecurityService.login(token.getUsername(), String.valueOf(token.getPassword()));
				if (merchant != null) {
					SecurityUtils.getSubject().getSession().setAttribute("loginUser", merchant);
					ShiroUser shiroUser = new ShiroUser();
					shiroUser.setMerchant(merchant);
					shiroUser.setLoginName(token.getUsername());
					return new SimpleAuthenticationInfo(shiroUser, merchant.getPassword(), getName());
				}
			} else {
				Account account = adminSecurityService.login(token.getUsername(), String.valueOf(token.getPassword()));
				SecurityUtils.getSubject().getSession().setAttribute("loginAdmin", account);
				ShiroUser shiroUser = new ShiroUser();
				shiroUser.setAccount(account);
				return new SimpleAuthenticationInfo(shiroUser, account.getPassword(), getName());
			}
		}
		return null;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -1748602382963711884L;
		private String loginName;
		private String name;
		private boolean isMerchant = false;
		private Merchant merchant;
		private Account account;

		public ShiroUser() {
		}

		public ShiroUser(String loginName, String name) {
			this.loginName = loginName;
			this.name = name;
		}

		public boolean isMerchant() {
			return isMerchant;
		}

		public void setMerchant(boolean isMerchant) {
			this.isMerchant = isMerchant;
		}

		public Merchant getMerchant() {
			return merchant;
		}

		public void setMerchant(Merchant merchant) {
			this.merchant = merchant;
		}

		public void setLoginName(String loginName) {
			this.loginName = loginName;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLoginName() {
			return loginName;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		public String getName() {
			return name;
		}

		public Account getAccount() {
			return account;
		}

		public void setAccount(Account account) {
			this.account = account;
		}
	}

}
