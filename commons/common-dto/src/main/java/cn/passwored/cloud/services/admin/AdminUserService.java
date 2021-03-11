package cn.passwored.cloud.services.admin;

import java.util.List;
import java.util.Map;

/**
 * Project：passwored_cloud
 * Description：管理员账号接口
 * Date：2021/3/11 16:03
 * Author wangke
 */
public interface AdminUserService {
    /**
     * 添加管理员用户
     */
    int AddUser(Map cpcAdminUser);

    /**
     * 编辑管理员用户
     */
    int editUser(Map cpcAdminUser);

    /**
     * 通过手机号查询
     */
    List<Map> findByPhone(String phone);

    /**
     * 查询全部管理员
     */
    List<Map> selectAll();

    /**
     * 通过id删除管理员
     */
    int deleteById(Long id);
}
