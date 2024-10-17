package com.biwta.pontoon.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(corsConfigurationSource());
	    http.csrf().disable();
	    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    http.authorizeRequests()
	    	.antMatchers("/api/sign-in").permitAll()
	        .antMatchers("/api/authenticate").permitAll()
	        .antMatchers("/api/register").permitAll()
	        .antMatchers("/api/activate").permitAll()
	        .antMatchers("/api/account/reset-password/init").permitAll()
	        .antMatchers("/api/account/reset-password/finish").permitAll()
	        .antMatchers("/file/**").permitAll()
	        .antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
	        .antMatchers("/api/v1/**").authenticated()
	        	.antMatchers(
	        			HttpMethod.GET,
	                    "/v3/api-docs/**",
	                    "/swagger-ui/**", "/swagger-ui.html"
	            ).permitAll()
	            .anyRequest().authenticated();
	    http.exceptionHandling().accessDeniedPage("/login");
	    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));
	    http.httpBasic();
	  }

	  @Bean
	  CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("*"));
	    configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PUT", "PATCH"));
	    configuration.setAllowedHeaders(Collections.singletonList("*"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	  }

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	  @Override
	  @Bean
	  public AuthenticationManager authenticationManagerBean() throws Exception {
	    return super.authenticationManagerBean();
	  }

}
