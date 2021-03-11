package cn.passwored.cloud.controller;

import cn.passwored.cloud.bean.ApiResponse;
import cn.passwored.cloud.util.jwt.JWTUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Project：cpc_build
 * Description：管理员用户控制器
 * Date：2021/2/19 15:14
 * Author wangke
 */
@RestController
@RequestMapping("/api/user/admin")
public class AdminUserController {


    /**
     * 管理员登录
     *
     * @param params 参数 username 用户名 password密码
     * @return {@link ApiResponse<Object>}
     */
    @PostMapping("/login")
    public ApiResponse<Object> adminLogin(@RequestBody Map<String, String> params) {
        if (params.size()==0){
            return ApiResponse.buildErrorMessage("参数错误");
        }
        Map<String,String>map = new HashMap<>();
        map.put("token", JWTUtil.generateToken(params.get("username"),"1"));
        map.put("username","管理员");
        return ApiResponse.buildSuccessResponse(map);
    }
}
