package com.bulletinboard.server.security;


import com.bulletinboard.server.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для создания и управления токеном
 */
@Component
public class JWTTokenProvider {
    public static final Logger LOG = LoggerFactory.getLogger(JWTTokenProvider.class);

    /**
     * Метод для создания токена
     *
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {
        // получение пользователя
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        // объект, использующий данные пользователя для генерации хеша
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("id", userId);
        claimsMap.put("username", user.getEmail());
        claimsMap.put("firstname", user.getName());
        claimsMap.put("lastname", user.getLastname());
        claimsMap.put("authorities", user.getAuthorities());

        // создание токена
        return Jwts.builder()
                .setSubject(userId)
                .addClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();

    }

    /**
     * Метод для декодирования токена, парсинга и получения claims-объекта Пользователя
     * (н-р, при получении запроса с клиента)
     *
     * @param token
     * @return
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException |
                MalformedJwtException |
                ExpiredJwtException |
                UnsupportedJwtException |
                IllegalArgumentException ex) {
            LOG.error(ex.getMessage());
            return false;
        }

    }

    /**
     * Вспомогательный метод для извлечения ИД пользователя из токена
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        String id = (String) claims.get("id");
        return Long.parseLong(id);
    }

    /**
     * Вспомогательный метод для извлечения email пользователя из токена
     */
    public String getUserEmailFromToken(String token) {
        Claims claims = getClaims(token);
        String email = (String) claims.get("username");
        return email;
    }


    /**
     * Вспомогательный метод для извлечения ролей пользователя из токена
     */
    public UsernamePasswordAuthenticationToken getAuthentication(final String token,
                                                                 final Authentication authentication,
                                                                 final UserDetails userDetails) {
        Claims claims = getClaims(token);
        List<GrantedAuthority> auths1= (List<GrantedAuthority>)claims.get("authorities");
        LinkedHashMap<GrantedAuthority, String> auths2 = (LinkedHashMap<GrantedAuthority, String>) auths1.get(0);

        Collection<? extends GrantedAuthority> authorities =  auths2
                .values()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * Вспомогательный метод для извлечения данных пользователя из токена
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
