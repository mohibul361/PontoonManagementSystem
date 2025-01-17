package com.biwta.pontoon.security;

import com.biwta.pontoon.domain.Authority;
import com.biwta.pontoon.domain.Employee;
import com.biwta.pontoon.domain.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {

	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:360000000}")
	private long validityInMilliseconds;

	@Autowired
	private MyUserDetails myUserDetails;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(String username, Users appUser) {
		List<GrantedAuthority> roles = new ArrayList<>(appUser.getAuthorities().size());
	    
		for (Authority appRole: appUser.getAuthorities()) {
			roles.add(new SimpleGrantedAuthority(appRole.getName()));
	    }

	    Claims claims = Jwts.claims().setSubject(username);
	    claims.put("auth", roles);

	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);

	    return Jwts.builder()
	    		.setClaims(claims)
	    		.setIssuedAt(now)
	    		.setExpiration(validity)
	    		.signWith(SignatureAlgorithm.HS256, secretKey)
	    		.compact();
	}
	public String createToken(String username, Employee employee) {
		List<GrantedAuthority> roles = new ArrayList<>((Collection) employee.getAuthority());

	    Claims claims = Jwts.claims().setSubject(username);
	    claims.put("auth", roles);

	    Date now = new Date();
	    Date validity = new Date(now.getTime() + validityInMilliseconds);

	    return Jwts.builder()
	    		.setClaims(claims)
	    		.setIssuedAt(now)
	    		.setExpiration(validity)
	    		.signWith(SignatureAlgorithm.HS256, secretKey)
	    		.compact();
	}

	public Authentication getAuthentication(String token) {
	    UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
	    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
	    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
	    String bearerToken = req.getHeader("Authorization");
	    
	    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
	    	return bearerToken.substring(7);
	    }
	    
	    return null;
	}

	public boolean validateToken(String token) {
	    try {
			System.out.println("--------------------");
			System.out.println(secretKey);
	    	Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
	    	return true;
	    } catch (JwtException | IllegalArgumentException e) {
	    	throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Expired or invalid JWT token");
	    }
	}

}
