package cn.passwored.cloud.domain;

import java.io.Serializable;
import java.util.Date;

/**
    * 管理员用户表
    */
public class SysAdminUser implements Serializable {
    private static final long serialVersionUID = 856851098636158537L;
    private Long id;

    /**
    * 账号
    */
    private String account;

    /**
    * 用户名
    */
    private String name;

    /**
    * 密码
    */
    private String password;

    /**
    * 手机号
    */
    private String phone;

    /**
    * 组织id
    */
    private Long orgId;

    /**
    * 角色id
    */
    private Long roleId;

    /**
    * 角色名称
    */
    private String roleName;

    /**
    * 0：关闭状态 1：开启状态
    */
    private Integer open;

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getOpen() {
        return open;
    }

    public void setOpen(Integer open) {
        this.open = open;
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