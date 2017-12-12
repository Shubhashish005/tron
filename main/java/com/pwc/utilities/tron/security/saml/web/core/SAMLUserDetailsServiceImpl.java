/*
 * Copyright 2017 Vincenzo De Notaris
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.pwc.utilities.tron.security.saml.web.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.xml.XMLObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.security.saml.userdetails.SAMLUserDetailsService;
import org.springframework.stereotype.Service;

import com.pwc.utilities.tron.model.entity.LocalUser;
import com.pwc.utilities.tron.model.entity.Permission;
import com.pwc.utilities.tron.model.entity.Role;
import com.pwc.utilities.tron.services.AdminService;

@Service
public class SAMLUserDetailsServiceImpl implements SAMLUserDetailsService {
	
	// Logger
	private static final Logger LOG = LoggerFactory.getLogger(SAMLUserDetailsServiceImpl.class);
	
	@Autowired
	private AdminService adminService;
	
	public Object loadUserBySAML(SAMLCredential credential)
			throws UsernameNotFoundException {
		
		// The method is supposed to identify local account of user referenced by
		// data in the SAML assertion and return UserDetails object describing the user.
		
		String userID = credential.getNameID().getValue();
		LOG.info(credential.getAttribute("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname").getName());
		for (Attribute attr : credential.getAttributes()) {
			LOG.info(credential.getAttributeAsString(attr.getName())+" - - "+attr.getName());
		}
		
		Attribute attr = credential.getAttribute("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname");
		String aa = credential.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname");
		LOG.info(aa);
		
		userID = credential.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name");
		userID = userID + " ["+credential.getAttributeAsString("http://schemas.pwc.com/identity/claims/employeeid")+"] ";
		LOG.info(userID + " is logged in");
		
		String guid = credential.getAttributeAsString("http://schemas.pwc.com/identity/claims/pwcguid");
		LocalUser localUser = adminService.getLocalUserByGuid(guid.trim());
		localUser.getRoles();
		
		Collection<GrantedAuthority> authorities = getAuthorities(localUser.getRoles());
		
		for(GrantedAuthority a : authorities) {
			LOG.info("===> "+a.getAuthority());
		}
		
		//List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
		//authorities.add(authority);

		// In a real scenario, this implementation has to locate user in a arbitrary
		// dataStore based on information present in the SAMLCredential and
		// returns such a date in a form of application specific UserDetails object.
		localUser.setGivenName(credential.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name"));
		localUser.setEmail(credential.getAttributeAsString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress"));
		localUser.setEmployeeId(credential.getAttributeAsString("http://schemas.pwc.com/identity/claims/employeeid"));
		adminService.addLocalUser(localUser);
		return new User(userID, "<abc123>", true, true, true, true, authorities);
	
	}
	
	private Collection<GrantedAuthority> getAuthorities(
		      Collection<Role> roles) {
		  
		        return getGrantedAuthorities(getPrivileges(roles));
		    }
		 
		    private List<String> getPrivileges(Collection<Role> roles) {
		  
		        List<String> privileges = new ArrayList<>();
		        List<Permission> collection = new ArrayList<>();
		        for (Role role : roles) {
		        	LOG.info(">>>>>>>>>> "+role.getName());
		            collection.addAll(role.getPermissions());
		        }
		        for (Permission item : collection) {
		        	LOG.info("<<<<<<<<<< "+item.getName());
		            privileges.add(item.getName());
		        }
		        return privileges;
		    }
		 
		    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
		        List<GrantedAuthority> authorities = new ArrayList<>();
		        for (String privilege : privileges) {
		            authorities.add(new SimpleGrantedAuthority(privilege));
		        }
		        return authorities;
		    }
	
}
