package com.hkt.btu.common.spring.security.core.userdetails;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;

public interface BtuUserDetailsService extends UserDetailsService {
	UserDetails loadVirtualUserByJobName(String jobName, Collection<? extends GrantedAuthority> authorities);
}
