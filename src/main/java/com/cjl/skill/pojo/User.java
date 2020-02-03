package com.cjl.skill.pojo;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Integer id;

    private String userName;

    private String password;

    private Integer type;

    private Date lastLoginTime;

    private static final long serialVersionUID = 1L;
    
    public User(Integer id, String userName) {
		this.id = id;
		this.userName = userName;
	}

    public User() {}
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}