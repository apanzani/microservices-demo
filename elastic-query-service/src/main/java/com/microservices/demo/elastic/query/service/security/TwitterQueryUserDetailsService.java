package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.elastic.query.service.business.QueryUserService;
import com.microservices.demo.elastic.query.service.transformer.UserPermissionsToUserDetailTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TwitterQueryUserDetailsService implements UserDetailsService {

    private final QueryUserService queryUserService;
    private final UserPermissionsToUserDetailTransformer transformer;

    public TwitterQueryUserDetailsService(QueryUserService queryUserService, UserPermissionsToUserDetailTransformer transformer) {
        this.queryUserService = queryUserService;
        this.transformer = transformer;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return queryUserService.findAllPermissionsByUsername(username)
                .map(transformer::getUserDetails)
                .orElseThrow(
                        () -> new UsernameNotFoundException("No user found with name " + username)
                );
    }
}
