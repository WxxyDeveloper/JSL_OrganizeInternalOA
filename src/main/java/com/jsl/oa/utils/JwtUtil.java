package com.jsl.oa.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class JwtUtil {
    // 替换为实际的密钥，建议使用足够长的随机字符串
    private static final String SECRET_KEY = Processing.createJobNumber((short) 1, (short) 255);

    // Token 有效期，这里设置为一天，可以根据实际需求调整
    private static final long EXPIRATION_TIME = 86400000;

    // 生成Token
    public static String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证Token
    public static boolean verify(String token, String username) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            // 从JWT中获取用户名进行匹配
            String tokenUsername = claimsJws.getBody().getSubject();

            // 验证用户名是否匹配
            return username.equals(tokenUsername);
        } catch (Exception e) {
            // 验证失败
            return false;
        }
    }
}
