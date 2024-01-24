package lk.travel.apigateway.security;


import jakarta.servlet.http.HttpServletRequest;
import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.CustomerDTO;
import lk.travel.apigateway.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class AuthenticationConfig implements AuthenticationProvider {

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String pwd = authentication.getCredentials().toString();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpServletRequest request = requestAttributes.getRequest();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION));
        String userEmail = null;
        String hashPwd = "";
        String role = "";
        if (request.getServletPath().startsWith("/api/v1/gateway/customer")) {

            CustomerDTO customerDTO = WebClient.create(SecurityConstant.URL +
                    "8082/api/v1/customer/search/email?email=" + userName).get().
                    headers(h -> h.add(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION))).retrieve().bodyToMono(CustomerDTO.class).block();
            hashPwd = customerDTO.getPwd();
            userEmail = customerDTO.getEmail();
            role = "ADMIN";
        } else {
            UserDTO userDTO = WebClient.create(SecurityConstant.URL +
                    "8081/api/v1/user/search/email?email=" + userName).get().
                    headers(h -> h.add(HttpHeaders.AUTHORIZATION, request.getHeader(HttpHeaders.AUTHORIZATION))).retrieve().bodyToMono(UserDTO.class).block();
            hashPwd = userDTO.getPwd();
            userEmail = userDTO.getEmail();
            role = userDTO.getRole().name();
        }
        if (userEmail != null) {
            if (passwordEncoder.matches(pwd, hashPwd)) {
                return new UsernamePasswordAuthenticationToken(userName, pwd, getGrantedAuthority(role));
            } else {
                throw new BadCredentialsException("Invalid Password");
            }
        } else {
            throw new BadCredentialsException("Invalid User Name");
        }
    }

    private Collection<GrantedAuthority> getGrantedAuthority(String role) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_" + role));
        return grantedAuthorityList;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
