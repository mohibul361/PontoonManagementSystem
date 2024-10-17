package com.biwta.pontoon.security;

import com.biwta.pontoon.domain.Authority;
import com.biwta.pontoon.domain.Users;
import com.biwta.pontoon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<Users> appUser = userRepository.findByEmailOrUsernameIgnoreCaseAndActivated(username, true);
        
        if (appUser == null || !appUser.isPresent()) {
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        List<GrantedAuthority> authorities = new ArrayList<>(appUser.get().getAuthorities().size());
        
        for (Authority appRole : appUser.get().getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(appRole.getName()));
        }

        return org.springframework.security.core.userdetails.User
        		.withUsername(username)
        		.password(appUser.get().getPassword())
        		.authorities(authorities)
        		.roles(appUser.get().getAuthorities().stream().map(appRole -> appRole.getName()).collect(Collectors.toList()).toArray(String[]::new))
        		.accountExpired(false)
        		.accountLocked(false)
        		.credentialsExpired(false)
        		.disabled(false)
        		.build();
    }

}
