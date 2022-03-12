package com.example.skooldio.config.security;

import com.auth0.jwt.interfaces.Claim;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthUtil {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final String SECRET = "mySecretKey";
    public static final String JWT_VERIFY_FAILED = "JWT verify failed.";

    public static String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("GRANT");

        String token = Jwts
                .builder()
                .setId("skooldio")
                .setSubject("skooldio")
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000)) //10 mins
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return AuthUtil.PREFIX + token;
    }

    public static boolean validateToken(String jwtToken) {
        boolean isValid = false;
        try {
            DecodedJWT decodedJWT = JWT.decode(jwtToken);
            Map<String, Claim> map = decodedJWT.getClaims();

            for (String name: map.keySet()) {
                String key = name;
                String value = map.get(name).toString();
                System.out.println(key + " " + value);
            }
            String[] authorities = map.get("authorities").asArray(String.class);
            for (int i = 0; i < authorities.length; i++) {
                if ("GRANT".equalsIgnoreCase(authorities[i])){
                    isValid = true;
                    break;
                }
            }

            if (decodedJWT.getExpiresAt().before(new Date())) {
                isValid = false;
            }

            return isValid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
