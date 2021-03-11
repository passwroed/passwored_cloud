package cn.passwored.cloud.domain;

import java.io.Serializable;
import java.util.Date;

/**
    * 管理员  角色-权限 中间表
    */
public class SysAdminRolePermission implements Serializable {
    private static final long serialVersionUID = 7083610272321792314L;
    private Long id;

    /**
    * 管理员角色id
    */
    private Long adminRoleId;

    /**
    * 管理员权限id
    */
    private Long adminPermissionId;

    /**
    * 创建时间
    */
    private Date createDate;

    /**
    * 更新时间
    */
    private Date updateDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdminRoleId() {
        return adminRoleId;
    }

    public void setAdminRoleId(Long adminRoleId) {
        this.adminRoleId = adminRoleId;
    }

    public Long getAdminPermissionId() {
        return adminPermissionId;
    }

    public void setAdminPermissionId(Long adminPermissionId) {
        this.adminPermissionId = adminPermissionId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}