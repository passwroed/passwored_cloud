package cn.passwored.cloud.domain;

import java.io.Serializable;
import java.util.Date;

/**
    * 管理员角色表
    */
public class SysAdminRole implements Serializable {
    private static final long serialVersionUID = 6556168599473081143L;
    private Long id;

    /**
    * 角色名称
    */
    private String roleName;

    /**
    * 描述
    */
    private String description;

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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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