package com.microservices.demo.elastic.query.service.security;

import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQuerySeviceResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class QueryServicePermissionEvaluator implements PermissionEvaluator {

    private static final String SUPER_USER_ROLE = "APP_SUPER_USER_ROLE";

    private final HttpServletRequest httpServletRequest;

    public QueryServicePermissionEvaluator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomain, Object permission) {
        
        if(isSuperUser()){
            return true;
        }
        if (targetDomain instanceof ElasticQuerySeviceRequestModel) {
            return preAuthorize(authentication, ((ElasticQuerySeviceRequestModel) targetDomain).getId(), permission);
        } else if (targetDomain instanceof ResponseEntity || targetDomain == null) {
            if (targetDomain == null) {
                return true;
            }
            List<ElasticQuerySeviceResponseModel> responseBody =
                    ((ResponseEntity<List<ElasticQuerySeviceResponseModel>>) targetDomain).getBody();
            Objects.requireNonNull(responseBody);
            return postAuthorize(authentication, responseBody, permission);
        }
        return false;
    }

    private boolean isSuperUser() {
        return httpServletRequest.isUserInRole(SUPER_USER_ROLE);
    }

    private boolean postAuthorize(Authentication authentication,
                                  List<ElasticQuerySeviceResponseModel> responseBody,
                                  Object permission) {
        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal();
        for (ElasticQuerySeviceResponseModel responseModel : responseBody) {
            PermissionType userPermission = twitterQueryUser.getPermissions().get(responseModel.getId());
            if (!hasPermission((String) permission, userPermission)) {
                return false;
            }
        }
        return true;
    }

    private boolean preAuthorize(Authentication authentication, String id, Object permission) {
        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal();
        PermissionType userPermission = twitterQueryUser.getPermissions().get(id);
        return hasPermission((String) permission, userPermission);
    }

    private boolean hasPermission(String requiredPermission, PermissionType userPermission) {
        return userPermission != null && requiredPermission.equals(userPermission.getType());
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if(isSuperUser()){
            return true;
        }
        if (targetId == null) {
            return false;
        }
        return preAuthorize(authentication, (String) targetId, permission);
    }
}
