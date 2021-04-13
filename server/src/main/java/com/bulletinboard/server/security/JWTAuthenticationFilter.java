package com.bulletinboard.server.security;


import com.bulletinboard.server.entity.User;
import com.bulletinboard.server.services.MyUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


/**
 * Класс, который используется для перехвата запроса с клиента на сервер
 * Объект внедряется между существующими фильтрами и выполняет парсинг токена из header запроса,
 * извлечения ИД и проверки пользователя в БД
 */
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    public static final Logger LOG = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    /**
     * Метод перехватчик запроса, который вызывается каждый раз при поступлении запроса на сервер
     * для проверки пользователя по токену
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // парсинг и валидация токена для извлечения ид юзера
            String jwt = getJWTFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {

                String email = jwtTokenProvider.getUserEmailFromToken(jwt);
                UserDetails userDetails = myUserDetailsService.loadUserByUsername(email);

                // поиск юзера по ИД
                UsernamePasswordAuthenticationToken authentication = jwtTokenProvider.getAuthentication(jwt,
                        SecurityContextHolder.getContext().getAuthentication(), userDetails);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            LOG.error("Could not set user authentication");
        }

        // добавление фильтра в цепочку стандартных фильтров
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    /**
     * Вспомогательный метод, который извлекает токен из запроса клиента
     *
     * @param request
     * @return
     */
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if (StringUtils.hasText(bearToken) && bearToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearToken.split(" ")[1];
        }
        return null;
    }
}
