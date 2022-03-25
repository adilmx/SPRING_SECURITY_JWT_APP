package com.adilmx.spring_security_jwt_app.service.utils;

import com.adilmx.spring_security_jwt_app.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtils implements Serializable {
    //the secret key used in the signature of JWT while generated
    private String secretKey = "secretKeyMx";

    //generating the token for user
    //we define here all the necessary claims as : roles, username, isUsedAt(date), expiration(date)
    //and then we sign theJWT using a specific algorithm(HS512) and a secret key
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        addRolesClaim(userDetails, claims);
        return Jwts.builder().setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    //extracting roles from user and add it to the claims
    private void addRolesClaim(UserDetails userDetails, Map<String, Object> claims) {
        if (userDetails == null || userDetails.getAuthorities() == null || userDetails.getAuthorities().isEmpty()) {
            return;
        } else {
            String roles = "";
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            for (GrantedAuthority granted : authorities) {
                //adding roles to a String result = ROLE1,ROLE2,ROLE3,...,
                roles += granted.getAuthority() + ",";
            }
            if (!roles.isEmpty()) {
                //to remove the last "," from the String of roles
                roles = roles.substring(0, roles.length() - 1);
            }
            //putting the Roles as [ROLE1,ROLE2,ROLE3,..,ROLEn]
            claims.put("roles", "[" + roles + "]");
        }
    }

    //validating the JWT Token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //check if the token has expired
    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //retrieve a specific claim from jwt token
    //(using a FunctionalInterface method that accept an argument(Claims) and return a result(T))
    //Claims::getExpiration means that take the Input and Output as the ones of [Date getExpiration()]
    //but we make the Input here static(Claims) to map it with the getter of a T Object(Claims)
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //retrieving any information from token we will need with the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public void registerAuthenticationTokenInContext(UserDetails userDetails, HttpServletRequest request) {
        //get the Details of the Principal and his authorities
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        //
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        //add the AuthToken To the context
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}