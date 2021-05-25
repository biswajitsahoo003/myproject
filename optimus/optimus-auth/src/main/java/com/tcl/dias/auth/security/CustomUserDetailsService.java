package com.tcl.dias.auth.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.tcl.dias.common.beans.CustomUserData;

/**
 * 
 * @author Manojkumar R
 *
 */
public class CustomUserDetailsService implements GrantedAuthoritiesMapper, Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	// @Cacheable(value = "userDetails", key = "#userName")
	// @Override
	public UserDetails loadUserByUsername(String userName) {
		CustomUserData customUserData = new CustomUserData();
		customUserData.setAuthentication(true);
		Collection<CustomRole> roles = new ArrayList<>();
		roles.add(new CustomRole("ROLE_DEV"));
		logger.info("***** The list of available actions for user{} are {} ", userName, roles);
		org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(
				userName, "", roles);
		customUserData.setUser(userDetail);
		customUserData.setAuthorities(roles);
		return customUserData;
	}

	public class CustomRole implements GrantedAuthority {
		private static final long serialVersionUID = 1L;
		String role = null;

		public CustomRole(String role) {
			this.role = role;
		}

		@Override
		public String getAuthority() {
			return role;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((role == null) ? 0 : role.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CustomRole other = (CustomRole) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (role == null) {
				if (other.role != null)
					return false;
			} else if (!role.equals(other.role)) {
				return false;
			}
			return true;
		}

		private CustomUserDetailsService getOuterType() {
			return CustomUserDetailsService.this;
		}

		@Override
		public String toString() {
			return role;
		}
	}

	/**
	 * mapAuthorities
	 * 
	 * @param authorities
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
		KeycloakPrincipal<KeycloakSecurityContext> keyPrincipal = (KeycloakPrincipal<KeycloakSecurityContext>) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		Collection<CustomRole> roles = new ArrayList<>();
		Set<String> rolesKey = keyPrincipal.getKeycloakSecurityContext().getToken().getRealmAccess().getRoles();
		for (String role : rolesKey) {
			roles.add(new CustomRole(role));
		}
		return roles;
	}

}
