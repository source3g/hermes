package com.source3g.hermes.security;

import java.io.Serializable;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.source3g.hermes.security.entity.Account;
import com.source3g.hermes.security.entity.Resource;
import com.source3g.hermes.security.entity.Role;
import com.source3g.hermes.security.service.SecurityService;

@Component
// TODO 权限
public class ShiroDbRealm extends AuthorizingRealm {

	@Autowired
	private SecurityService securityService;

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		Account account = securityService.findUserByLoginName(shiroUser.getLoginName());
		if (account != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (Role role : account.getRoles()) {
				// 基于Permission的权限信息
				for (Resource resource : role.getResources())
					info.addStringPermission(resource.getCode());
			}
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();
		if (userName != null && !"".equals(userName)) {
			Account account = securityService.login(token.getUsername(), String.valueOf(token.getPassword()));
			if (account != null) {
				return new SimpleAuthenticationInfo(account.getAccount(), account.getPassword(), getName());
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

		public ShiroUser(String loginName, String name) {
			this.loginName = loginName;
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
	}

}
