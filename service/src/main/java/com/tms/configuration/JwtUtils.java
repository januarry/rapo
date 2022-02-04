package com.tms.configuration;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.tms.serviceImpl.UserDetailsImpl;
import com.tms.util.RestTemplateUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${tms.app.jwtSecret}")
	private String jwtSecret;

	@Value("${tms.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	@Value("${sas.app.baseUrl}")
	public String sasBaseUrl;

	@Value("${sms.app.baseUrl}")
	public String smsBaseUrl;

	@Value("${hrms.service.baseUrl}")
	public String hrmsBaseUrl;

	@Value("${qb.service.baseUrl}")
	public String qbBaseUrl;

	@Value("${qb.service.quebaseUrl}")
	public String queBaseUrl;

	public String token;

	public HttpHeaders headers;
	public HttpEntity<HashMap<String, Object>> entity;

	public final RestTemplateUtil restTemplateUtil;

	public JwtUtils(RestTemplateUtil restTemplateUtil) {
		this.restTemplateUtil = restTemplateUtil;
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	public String getToken() {
		return token;
	}


	public HttpEntity<HashMap<String, Object>> getEntity() {
		return this.entity;
	}

	public void setEntity(HttpEntity<HashMap<String, Object>>  entity) {
		this.entity = entity;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");
		String token = Jwts.builder().setHeaderParams(headerMap).setId(jwtSecret)
				.setSubject(userPrincipal.getUsername())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 600000000))
				.signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()).compact();
		// "Bearer " + token;
		return token;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	public String generateJwtToken(String username) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		Map<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put("alg", "HS256");
		headerMap.put("typ", "JWT");
		String token = Jwts.builder().setHeaderParams(headerMap).setId(jwtSecret).setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes()).compact();
		// "Bearer " + token;
		return "Bearer " + token;
	}

	public String extractUserName(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	// public String extractTenantId(String token) {
	// Claims claims = extractAllClaims(token);
	// return (String) claims.get("tenantId");
	// }

	public String extractTenantCode(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("TenantCode");
	}

	public String extractDbId(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("DBId");
	}

	public String extractUId(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("uid");
	}

	public String extractFirstName(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("FirstName");
	}

	public String extractLastName(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("LastName");
	}

	public String extractLastLoggedInTime(String token) {
		Claims claims = extractAllClaims(token);
		return (String) claims.get("LastLoggedInTime");
	}

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();

	}

	public String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length());
		}

		return null;
	}

	private String createToken(Map<String, Object> claims) {
		JwtBuilder builder = Jwts.builder().setClaims(claims).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
				.signWith(SignatureAlgorithm.HS256, jwtSecret);
		return builder.compact();
	}

	public String getURL(String url) throws UnsupportedEncodingException {

		return URLDecoder.decode(url,
				StandardCharsets.UTF_8.name());
	}

}
