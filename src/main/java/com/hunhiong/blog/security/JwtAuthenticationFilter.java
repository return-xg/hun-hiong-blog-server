package com.hunhiong.blog.security;

import com.hunhiong.blog.common.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 *
 * <p>从请求头中解析 Token，校验通过后构建 SecurityContext。</p>
 *
 * @author hunhiong
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 白名单路径：不校验 Token，直接放行
        if ("/article/page".equals(path)
                || path.matches("/article/\\d+")
                || path.matches("/article/\\d+/view")
                || path.matches("/article/\\d+/like")
                || "/category/page".equals(path)
                || path.matches("/category/\\d+")
                || "/category/list".equals(path)
                || "/tag/page".equals(path)
                || "/tag/list".equals(path)
                || path.matches("/tag/\\d+")
                || "/music/list".equals(path)
                || "/music/page".equals(path)
                || path.matches("/music/\\d+")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 非白名单路径才走 Token 校验
        try {
            String bearerToken = request.getHeader(SecurityConstants.TOKEN_HEADER);
            String token = jwtTokenProvider.resolveToken(bearerToken);

            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                Claims claims = jwtTokenProvider.parseClaims(token);
                Long userId = Long.valueOf(claims.get(SecurityConstants.CLAIM_USER_ID).toString());
                String username = claims.get(SecurityConstants.CLAIM_USERNAME, String.class);
                String nickname = claims.get(SecurityConstants.CLAIM_NICKNAME, String.class);

                LoginUser loginUser = new LoginUser(userId, username, nickname);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.warn("JWT 认证处理异常: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
