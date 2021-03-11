package cn.passwored.cloud.bean;


/**
 * Project：passwored_cloud
 * Description：token认证异常
 * Date：2021/2/18 20:47
 * Author wangke
 */
public class TokenAuthenticationException extends RuntimeException{
    private int code;

    private String message;

    public TokenAuthenticationException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
