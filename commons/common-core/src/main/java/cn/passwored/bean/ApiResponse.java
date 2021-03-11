package cn.passwored.bean;


import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.passwored.util.HumpUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @program: passwored_cloud
 * @description: 统一API返回对象
 * @author: Wangke
 * @create: 2021-02-01 00:59
 **/
public class ApiResponse<T> implements Serializable {
    private static final long serialVersionUID = -1;
    private static String key = "5efsYqfySlfzPb7L";
    private int code;
    private String message;
    private List<T> list;
    private int total;

    public ApiResponse() {

    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, List list, int total) {
        this.code = code;
        this.message = message;
        this.list = list;
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getLength() {
        if (this.list == null) {
            return 0;
        }
        return this.list.size();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 判断是否成功
     *
     * @return Boolean
     */
    public Boolean isSuccess() {
        return ApiStatus.SC_OK == this.code;
    }

    /**
     * 构建成功提示返回对象
     *
     * @param message 返回message
     * @return ApiResponse
     */
    public static ApiResponse<Object> buildSuccessMessage(String message) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ApiStatus.SC_OK);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * 构建错误提示返回对象
     *
     * @param message 返回message
     * @return ApiResponse
     */
    public static ApiResponse<Object> buildErrorMessage(String message) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ApiStatus.SC_COMMON_SERVICE_ERROR);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * 构建错误消息和错误代码
     *
     * @param message 返回message
     * @param code    代码
     * @return ApiResponse
     */
    public static ApiResponse<Object> buildMessageCode(String message, int code) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * 熔断错误提示返回对象
     *
     * @param message 返回message
     * @return ApiResponse
     */
    public static ApiResponse<Object> buildFeignErrorMessage(String message) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ApiStatus.SC_FEIGN_SERVICE_ERROR);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * 构建成功返回数据对象(分页)
     *
     * @param total 总页数
     * @param obj   dto对象
     * @param <T>   范型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> buildSuccessResponse(int total, Object obj) {
        ApiResponse<T> apiResponse = buildSuccessResponse(obj);
        apiResponse.setTotal(total);
        return apiResponse;
    }

    /**
     * 构建成功返回数据对象
     *
     * @param obj dto对象
     * @param <T> 范型
     * @return ApiResponse
     */
    public static <T> ApiResponse<T> buildSuccessResponse(Object obj) {
        List respList;
        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ApiStatus.SC_OK);
        apiResponse.setMessage("success");
        if (obj == null) {
            return apiResponse;
        }

        if (List.class.isAssignableFrom(obj.getClass())) {
            respList = (List) obj;
//            //进行加密
//            respList = new ArrayList();
//            Map<String, String> map = new HashMap<>();
//            map.put("data", SM4EncForCBC(JSON.toJSONString(obj)));
//            respList.add(map);
        } else {
            respList = new ArrayList();
            respList.add(obj);
//            //进行加密
//            respList = new ArrayList();
//            Map<String, String> map = new HashMap<>();
//            List list = new ArrayList();
//            list.add(obj);
//            map.put("data", SM4EncForCBC(JSON.toJSONString(list)));
//            respList.add(map);
        }

        respList.removeAll(Collections.singleton(null));

        if (respList.size() == 0 || !HashMap.class.equals(respList.get(0).getClass())) {
            apiResponse.setList(respList);
            return apiResponse;
        }
        //下划线转驼峰
        List<T> resultList = (List<T>) HumpUtil.lineToHump(respList);
        apiResponse.setList(resultList);

        return apiResponse;

    }

    @Override
    public String toString() {
        String resultMsg = "code:[" + code + "],MESSAGE:[" + message + "]";
        if (list != null) {
            resultMsg += ",DATA:[LIST TOTAL=" + total + " LEN=" + getLength() + "]";
        }
        return resultMsg;
    }

    //对称秘钥加密(CBC)
    public static String SM4EncForCBC(String text) {
        SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
        return sm4.encryptBase64(text);
    }
}
