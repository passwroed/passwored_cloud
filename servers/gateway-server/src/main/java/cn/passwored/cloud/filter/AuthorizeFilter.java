package cn.passwored.cloud.filter;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.passwored.cloud.bean.ApiResponse;
import cn.passwored.cloud.bean.ApiStatus;
import cn.passwored.cloud.bean.TokenAuthenticationException;
import cn.passwored.cloud.util.jwt.JWTUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Project：eparty
 * Description：认证过滤器
 * Date：2021/2/18 20:47
 * Author wangke
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static String key = "5efsYqfySlfzPb7L";
    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${spring.profiles.active}")
    private String active;

    private static final String TOKEN_CACHE_PREFIX = "auth-service:";
    //测试验签
    public static final String TEST_SM4_URL = "/apiUser/api/app/test";
    //认证路径
    public static final String LOGIN_USER_URL = "/api/user/admin/login";
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        ServerHttpResponse serverHttpResponse = exchange.getResponse();

        String token = serverHttpRequest.getHeaders().getFirst("token");

        //处理登录请求
        if (StringUtils.isBlank(token)) {
            //是登录接口则放行，否则返回异常
            PathContainer pathContainer = serverHttpRequest.getPath().pathWithinApplication();
            String path = pathContainer.value();
            if ((path.contains(TEST_SM4_URL)||path.contains(LOGIN_USER_URL)) && HttpMethod.POST.equals(serverHttpRequest.getMethod())) {
                Mono<Void> voidMono = null;
                // 对POST解密操作
                voidMono = readBody(exchange, chain,null);
                return voidMono;
            }
            serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return getVoidMono(serverHttpResponse, ApiStatus.TOKEN_EXPIRED, "token已过期");
        }


        // 检查是否可以解密
        String username = null;
        try {
            username = JWTUtil.getUserInfo(token);
        } catch (Exception e) {
            return getVoidMono(serverHttpResponse, ApiStatus.TOKEN_INVALID, "无效的Token");
        }

        if (StringUtils.isEmpty(username)) {
            return getVoidMono(serverHttpResponse, ApiStatus.TOKEN_INVALID, "无效的Token");
        }

        // 检查Redis中是否有此Token
//        String key = MD5Util.getMD5Str(username);
//        if (!redisTemplate.opsForHash().hasKey(TOKEN_CACHE_PREFIX + key, "token")) {
//            return getVoidMono(serverHttpResponse, ApiStatus.TOKEN_INVALID,"无效的Token");
//        }

        try {
            JWTUtil.verifyToken(token);
        } catch (TokenAuthenticationException ex) {
            return getVoidMono(serverHttpResponse, ex.getCode(), ex.getMessage());
        } catch (Exception ex) {
            return getVoidMono(serverHttpResponse, ApiStatus.UNKNOWN_ERROR, "未知错误");
        }

        Mono<Void> voidMono = null;
        //解密修改body
        if (serverHttpRequest.getMethod() == HttpMethod.POST && active.equals("test")) {
            // 对POST解密操作
            voidMono = readBody(exchange, chain,JWTUtil.getUserInfo(token));
            return voidMono;
        }
        return chain.filter(exchange);
    }

    private Mono<Void> getVoidMono(ServerHttpResponse serverHttpResponse, int code, String msg) {
        serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        DataBuffer dataBuffer = serverHttpResponse.bufferFactory().wrap(JSON.toJSONString(ApiResponse.buildMessageCode(msg, code)).getBytes());
        return serverHttpResponse.writeWith(Flux.just(dataBuffer));
    }

    @Override
    public int getOrder() {
        return 0;
    }

    /**
     * 阅读的身体
     * 读取body并进行修改
     *
     * @param exchange ServerWebExchange
     * @param chain    GatewayFilterChain
     * @param userId   用户id
     * @return {@link Mono<Void>}
     */
    private Mono<Void> readBody(ServerWebExchange exchange, GatewayFilterChain chain,String userId) {
        final ServerHttpRequest request = exchange.getRequest();
        MediaType mediaType = request.getHeaders().getContentType();
        // 修改响应体
        ServerRequest serverRequest = new DefaultServerRequest(exchange);
        Mono<String> modifiedBody;
        modifiedBody = serverRequest.bodyToMono(String.class)
                .flatMap(body -> {
                    return Mono.just(decodeBody(body,userId));
                });
        //修改header防止报错
        BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(request.getHeaders());
        headers.remove(HttpHeaders.CONTENT_LENGTH);
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        return bodyInserter.insert(outputMessage, new BodyInserterContext())
                .then(Mono.defer(() -> {
                    ServerHttpRequestDecorator decorator = new ServerHttpRequestDecorator(
                            request) {
                        @Override
                        public HttpHeaders getHeaders() {
                            long contentLength = headers.getContentLength();
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.putAll(super.getHeaders());
                            if (contentLength > 0) {
                                httpHeaders.setContentLength(contentLength);
                            } else {
                                httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                            }
                            return httpHeaders;
                        }

                        @Override
                        public Flux<DataBuffer> getBody() {
                            return outputMessage.getBody();
                        }
                    };
                    return chain.filter(exchange.mutate().request(decorator).build());
                }));
    }

    /**
     * 解码body
     *
     * @param body   body
     * @param userId 用户id
     * @return {@link String}
     */
    private String decodeBody(String body,String userId) {
        Map<String,String> decMap = JSON.parseObject(body, Map.class);
        if (decMap.size() == 0&&StringUtils.isBlank(userId)) {
            return JSON.toJSONString(decMap);
        }
        String data = decMap.get("data");
        if (active.equals("test")){
            decMap.put("userId",userId);//传输userId
            return JSON.toJSONString(decMap);
        }
        String sm4Data = SM4DecForCBC(data);//解密
        if (StringUtils.isBlank(sm4Data)) {
            return JSON.toJSONString("");
        }
        return sm4Data;
    }

    //对称秘钥解密(CBC)
    public static String SM4DecForCBC(String text) {
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        String decryptStr = sm4.decryptStr(text, CharsetUtil.CHARSET_UTF_8);//解密
        return decryptStr;
    }
}
