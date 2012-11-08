package com.source3g.hermes.api.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

@Component
//TODO 权限
public class ShiroDbRealm extends AuthorizingRealm {

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		/*ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		User user = accountManager.findUserByLoginName(shiroUser.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (Group group : user.getGroupList()) {
				//基于Permission的权限信息
				info.addStringPermissions(group.getPermissionList());
			}
			return info;
		} else {
			return null;
		}*/
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();    
        if( userName != null && !"".equals(userName) ){    
           /* User user = accountManager.login(token.getUsername(),    
                            String.valueOf(token.getPassword()));    

            if( user != null )    
                return new SimpleAuthenticationInfo(    
                            user.getLoginName(),user.getPassword(), getName()); 
                                */   
        }
        return null;
	}

}
