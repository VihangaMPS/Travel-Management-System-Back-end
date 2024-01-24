package lk.travel.vehicleservice.security;

import lk.travel.apigateway.constant.SecurityConstant;
import lk.travel.apigateway.dto.UserDTO;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthenticationConfig implements AuthenticationProvider {
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        HttpHeaders httpHeaders = new HttpHeaders();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); // get Basic auth Header
        httpHeaders.set(HttpHeaders.AUTHORIZATION, requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION));
        UserDTO userDTO = restTemplate.exchange(SecurityConstant.USER_URL +"/api/v1/user/search/email?email="+ username, HttpMethod.GET, new HttpEntity<>(httpHeaders), UserDTO.class).getBody();
        if (userDTO != null) {
            if (passwordEncoder.matches(pwd, userDTO.getPwd())) {
                return new UsernamePasswordAuthenticationToken(username, pwd, getGenerateAuthorities(userDTO.getRole().name()));
            }
            throw new BadCredentialsException("Invalid Password name");
        } else {
            throw new BadCredentialsException("Invalid User name");
        }

    }

    private Collection<? extends GrantedAuthority> getGenerateAuthorities(String name) {
        ArrayList<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + name));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }


}
