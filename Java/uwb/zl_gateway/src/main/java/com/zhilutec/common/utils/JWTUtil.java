package com.zhilutec.common.utils;

import com.zhilutec.common.result.Result;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTUtil {

    public final static String ACCOUNT = "GW_ACCOUNT";
    public final static String ACCOUNT_PRE = ACCOUNT + ":";
    public final static Long REDIS_TTL = 600L;  //Redis 过期时间 单位秒
    public final static String TOKEN_PRE = "TOKEN:";

    private final static Long JWT_TTL = REDIS_TTL;  //Redis 过期时间 单位秒
    private final static String AUDIENCE = "zl_uwb"; //接收者
    private final static String ISSUER = "zhilutec"; //签发者
    private final static Long REDIS_DEFAULT_TTL = -1L; //默认过期时间 单位秒
    private final static Long JWT_DEFAULT_TTL = REDIS_DEFAULT_TTL; //默认过期时间 单位秒


    private final static String BASE64_SECUTITY = "prisonSecurity";
    private final static String SUBJECT = "zhilutecSbj";

    private static SecretKey genKey(String base64Security) {
        //生成签名密钥
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = dataTypeConver(base64Security);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }

    // private static SecretKey generalKey() {
    //     byte[] encodedKey = Base64.decode(BASE64_SECUTITY.getBytes());
    //     SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    //     return key;
    // }

    private static byte[] dataTypeConver(String base64Security) {
        if (base64Security == null || base64Security.isEmpty()) {
            base64Security = BASE64_SECUTITY;
        }
        return DatatypeConverter.parseBase64Binary(base64Security);
    }

    public static Claims parseJWT(String jsonWebToken, String base64Security) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(genKey(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 解析JWT字符串
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    // public static Claims parseJWT(String jwt) throws Exception {
    //     SecretKey secretKey = generalKey();
    //     return Jwts.parser()
    //             .setSigningKey(secretKey)
    //             .parseClaimsJws(jwt)
    //             .getBody();
    // }

    public static String createJWT(String username, String userId, String roleId,
                                   String audience, String issuer, Long ttlMillis, String base64Security) {
        if (audience == null || audience.isEmpty()) {
            audience = AUDIENCE;
        }
        if (issuer == null || issuer.isEmpty()) {
            issuer = ISSUER;
        }
        if (ttlMillis == null) {
            ttlMillis = JWT_TTL;
        }

        if (base64Security == null) {
            base64Security = BASE64_SECUTITY;
        }


        //############################转成毫秒,不转成毫秒无法解析jwt#####################
        ttlMillis = ttlMillis * 1000;
        //############################转成毫秒########################################

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // Key signingKey = genKey(base64Security);
        Key signingKey = genKey(base64Security);
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("type", "JWT")
                .setId(userId)  //jwt id
                .setSubject(SUBJECT) //主题
                .claim("username", username) //自定义属性
                .claim("userId", userId) //自定义属性
                .claim("roleId", roleId)
                .setIssuer(issuer)  // 签发者
                .setAudience(audience) // 接受者
                .signWith(signatureAlgorithm, signingKey); // 签名算法以及密匙
        //添加Token过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp)  // 过期时间
                    .setNotBefore(now); // 失效时间
        }

        //生成JWT
        return builder.compact();
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    public static Result validateJWT(String jwtStr, String base64Security) {
        Map rsMap = new HashMap();
        Result checkResult = new Result();
        Claims claims = null;
        try {
            claims = parseJWT(jwtStr, base64Security);
            checkResult.ok(claims);
        } catch (ExpiredJwtException e) {
            rsMap.put("jwtExpired", "ToKen过期");
            checkResult.ok(rsMap);
        } catch (SignatureException e) {
            rsMap.put("jwtSign", "ToKen认证失败");
            checkResult.ok(rsMap);
        } catch (Exception e) {
            rsMap.put("jwtExpired", "ToKen认证异常");
            checkResult.ok(rsMap);
        }
        return checkResult;
    }

    /**
     * 验证JWT
     *
     * @param jwtStr
     * @return
     */
    // public static Result validateJWT(String jwtStr) {
    //     Map rsMap = new HashMap();
    //     Result checkResult = new Result();
    //     Claims claims = null;
    //     try {
    //         claims = parseJWT(jwtStr);
    //         checkResult.ok(claims);
    //     } catch (ExpiredJwtException e) {
    //         rsMap.put("jwtExpired", "ToKen过期");
    //         checkResult.ok(rsMap);
    //     } catch (SignatureException e) {
    //         rsMap.put("jwtSign", "ToKen认证失败");
    //         checkResult.ok(rsMap);
    //     } catch (Exception e) {
    //         rsMap.put("jwtExpired", "ToKen认证异常");
    //         checkResult.ok(rsMap);
    //     }
    //     return checkResult;
    // }


    // 设置jwt有效期
    public static Date getExpiryDate(int minutes) {
        // 根据当前日期，来得到到期日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

}
