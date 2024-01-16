package com.jsl.oa.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.regex.Pattern;

public class JwtUtil {
    // 替换为实际的密钥，建议使用足够长的随机字符串
    private static final String SECRET_KEY = "238542310128901753637022851772455105464283332917211091531086967815273100806759714250034263888525489008903447113697698540563820710887668094087054975808574632265678643370464260078072153369247242449569221118098938297741582538222826493707667477115117609126233";

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
    public static boolean verify(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            // 从JWT中获取用户名进行匹配
            String tokenUsername = claimsJws.getBody().getSubject();
            // 验证用户名是否匹配
            return Pattern.matches("^[0-9A-Za-z_]{3,40}$", tokenUsername);
        } catch (Exception e) {
            // 验证失败
            return false;
        }
    }
}
