package com.jsl.oa.utils;

import com.jsl.oa.common.constant.SafeConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.security.Key;
import java.util.regex.Pattern;

/**
 * <h1>JWT工具类</h1>
 * <hr/>
 * 用于生成Token和验证Token
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @see com.jsl.oa.config.JwtFilter
 * @since v1.1.0
 */
@Slf4j
public class JwtUtil {
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * <h2>生成Token</h2>
     * <hr/>
     * 用于生成Token
     * @param userId 用户ID
     * @return 返回生成的Token
     */
    public static String generateToken(@NotNull Long userId) {
        Key key = Keys.hmacShaKeyFor(SafeConstants.SECRET_KEY.getBytes());
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * <h2>验证Token</h2>
     * <hr/>
     * 用于验证Token是否有效
     *
     * @param token Token
     * @return {@link Boolean}
     */
    public static boolean verify(String token) {
        try {
            Long getTokenInUserId = getUserId(token);
            // 验证用户名是否匹配
            log.debug("Token值" + getTokenInUserId.toString());
            return Pattern.matches("^[0-9]+$", getTokenInUserId.toString());
        } catch (Exception e) {
            log.debug("Token验证失败", e);
            return false;
        }
    }

    /**
     * <h2>获取用户名</h2>
     * <hr/>
     * 用于获取Token中的用户名
     *
     * @param token Token
     * @return 返回获取到的用户名
     */
    public static Long getUserId(String token) {
        Key key = Keys.hmacShaKeyFor(SafeConstants.SECRET_KEY.getBytes());
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        // 从JWT中获取用户名进行匹配
        long userId;
        try {
            userId = Long.parseLong(claimsJws.getBody().getSubject());
            log.debug("用户ID" + userId);
        } catch (NumberFormatException exception) {
            log.debug("用户ID格式错误", exception);
            throw new NumberFormatException("用户ID格式错误");
        }
        return userId;
    }
}
