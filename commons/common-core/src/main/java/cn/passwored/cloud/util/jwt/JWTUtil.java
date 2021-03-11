package cn.passwored.cloud.util.jwt;

import cn.passwored.cloud.bean.TokenAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import static cn.passwored.cloud.bean.ApiStatus.*;


/**
 * Project：eparty
 * Description：jwt认证工具类
 * Date：2021/2/18 20:47
 * Author wangke
 */
public class JWTUtil {
    //token负载数据-用户名
    private static final String USER_INFO_KEY = "username";
    //token负载数据-id
    private static final String USER_ID_KEY = "userid";
    // token 签名的秘钥，可设置到配置文件中
    private static final String SECRET_KEY = "secretKey:123456";
    // token过期时间
    public static final long TOKEN_EXPIRE_TIME = 7200 * 1000;
    //发行人
    private static final String ISSUER = "com.qchtkj";

    /**
     * 生成令牌
     * 生成Token
     *
     * @param username 用户标识(唯一)
     * @param userId   用户id
     * @return {@link String}
     */
    public static String generateToken(String username,String userId) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

        Date now = new Date();

        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim(USER_INFO_KEY, username)
                .withClaim(USER_ID_KEY, userId)
                .sign(algorithm);
    }

    /**
     * 校验Token
     *
     * @param token   访问秘钥
     */
    public static void verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier jwtVerifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            jwtVerifier.verify(token);
        } catch (JWTDecodeException jwtDecodeException) {
            throw new TokenAuthenticationException(TOKEN_INVALID, "无效的Token");
        } catch (SignatureVerificationException signatureVerificationException) {
            throw new TokenAuthenticationException(TOKEN_SIGNATURE_INVALID, "无效的签名");
        } catch (TokenExpiredException tokenExpiredException) {
            throw new TokenAuthenticationException(TOKEN_EXPIRED, "token已过期");
        } catch (Exception ex) {
            throw new TokenAuthenticationException(UNKNOWN_ERROR, "未知错误");
        }
    }

    /**
     * 从Token中提取用户信息
     *
     * @param token 令牌
     * @return String
     */
    public static String getUserInfo(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim(USER_ID_KEY).asString();
    }

}
