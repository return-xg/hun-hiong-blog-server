package com.hunhiong.blog.security;

import com.hunhiong.blog.common.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token 提供者
 *
 * <p>负责 Token 的生成、解析与校验。</p>
 *
 * @author hunhiong
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    /** Access Token 有效期（秒），默认 2 小时 */
    @Value("${jwt.access-token-expiration:7200}")
    private long accessTokenExpiration;

    /** Refresh Token 有效期（秒），默认 7 天 */
    @Value("${jwt.refresh-token-expiration:604800}")
    private long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        if (secret == null || secret.length() < 32) {
            log.warn("JWT secret 长度不足 32 字符，建议配置更长的密钥以保证安全性");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 Access Token
     */
    public String generateAccessToken(Long userId, String username, String nickname) {
        return buildToken(userId, username, nickname, accessTokenExpiration);
    }

    /**
     * 生成 Refresh Token
     */
    public String generateRefreshToken(Long userId, String username, String nickname) {
        return buildToken(userId, username, nickname, refreshTokenExpiration);
    }

    private String buildToken(Long userId, String username, String nickname, long expirationSeconds) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationSeconds * 1000L);
        return Jwts.builder()
                .claim(SecurityConstants.CLAIM_USER_ID, userId)
                .claim(SecurityConstants.CLAIM_USERNAME, username)
                .claim(SecurityConstants.CLAIM_NICKNAME, nickname)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * 从 Token 中解析 Claims
     */
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 获取用户ID
     */
    public Long getUserId(String token) {
        Object value = parseClaims(token).get(SecurityConstants.CLAIM_USER_ID);
        if (value == null) {
            return null;
        }
        return Long.valueOf(value.toString());
    }

    /**
     * 获取用户名
     */
    public String getUsername(String token) {
        return parseClaims(token).get(SecurityConstants.CLAIM_USERNAME, String.class);
    }

    /**
     * 获取昵称
     */
    public String getNickname(String token) {
        return parseClaims(token).get(SecurityConstants.CLAIM_NICKNAME, String.class);
    }

    /**
     * 校验 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            log.warn("Token 校验失败: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Token 为空或格式错误: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 从请求头 Authorization 中提取 Token
     */
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return bearerToken.substring(SecurityConstants.TOKEN_PREFIX.length()).trim();
        }
        return null;
    }

    public long getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
