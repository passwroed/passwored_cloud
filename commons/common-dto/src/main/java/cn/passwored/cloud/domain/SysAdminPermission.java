package cn.passwored.cloud.domain;

import java.io.Serializable;
import java.util.Date;

/**
    * 管理员权限表
    */
public class SysAdminPermission implements Serializable {
    private static final long serialVersionUID = -116250011800790490L;
    private Long id;

    /**
    * 父权限id
    */
    private Long pid;

    /**
    * 权限名称
    */
    private String name;

    /**
    * 0:栏目 1:页面 2:按钮
    */
    private Integer type;

    /**
    * 图标
    */
    private String icon;

    /**
    * 授权路径
    */
    private String url;

    /**
    * 备注
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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