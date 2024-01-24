package lk.travel.customerservice.security;

import lk.travel.customerservice.dto.CustomerDTO;
import lk.travel.customerservice.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class AuthenticationConfig implements AuthenticationProvider {
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        CustomerDTO customerDTO = customerService.searchByEmailCustomer(username);
        if (customerDTO != null) {
            if (passwordEncoder.matches(pwd, customerDTO.getPwd())) {
                return new UsernamePasswordAuthenticationToken(username, pwd, getGenerateAuthorities("USER"));
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
