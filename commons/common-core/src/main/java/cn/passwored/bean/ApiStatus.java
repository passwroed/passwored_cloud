package cn.passwored.bean;

/**
 * @program: passwored_cloud
 * @description: API调用返回状态
 * @author: Wangke
 * @create: 2021-02-01 00:59
 **/
public class ApiStatus {

    /**
     * （成功） 服务器已成功处理了请求
     */
    public static final int SC_OK = 200;

    /**
     * （错误请求） 服务器不理解请求的语法
     */
    public static final int SC_BAD_REQUEST = 400;
    /**
     * （未授权） 请求要求身份验证
     */
    public static final int SC_UNAUTHORIZED = 401;
    /**
     * （禁止） 服务器拒绝请求
     */
    public static final int SC_FORBIDDEN = 403;
    /**
     * （未找到） 服务器找不到请求的网页
     */
    public static final int SC_NOT_FOUND = 404;

    /**
     * （服务器内部错误） 服务器遇到错误，无法完成请求
     */
    public static final int SC_INTERNAL_SERVER_ERROR = 500;

    /**
     * （常规服务访问错误） 常规服务访问错误，无法完成请求
     */
    public static final int SC_COMMON_SERVICE_ERROR = 5001;

    /**
     * （Feign访问错误） 通过Feign访问服务错误，无法完成请求
     */
    public static final int SC_FEIGN_SERVICE_ERROR = 5002;
    /**
     * （token访问错误） 无效的Token
     */
    public static final int TOKEN_INVALID = 4001;
    /**
     * （token访问错误） 无效的签名
     */
    public static final int TOKEN_SIGNATURE_INVALID = 4002;
    /**
     * （token访问错误） token已过期
     */
    public static final int TOKEN_EXPIRED = 4003;
    /**
     * （token访问错误） 未知错误
     */
    public static final int UNKNOWN_ERROR = 4004;
    /**
     * 数据解密错误
     */
    public static final int DATA_DEC_ERROR = 4005;
}
