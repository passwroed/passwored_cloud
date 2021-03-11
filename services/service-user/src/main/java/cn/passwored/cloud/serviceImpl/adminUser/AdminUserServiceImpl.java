package cn.passwored.cloud.serviceImpl.adminUser;

import cn.passwored.cloud.services.admin.AdminUserService;

import java.util.List;
import java.util.Map;

/**
 * Project：passwored_cloud
 * Description：管理员账号接口实现
 * Date：2021/3/11 16:23
 * Author wangke
 */
public class AdminUserServiceImpl implements AdminUserService {
    @Override
    public int AddUser(Map cpcAdminUser) {
        return 0;
    }

    @Override
    public int editUser(Map cpcAdminUser) {
        return 0;
    }

    @Override
    public List<Map> findByPhone(String phone) {
        return null;
    }

    @Override
    public List<Map> selectAll() {
        return null;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }
}
