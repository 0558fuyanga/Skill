package com.cjl.skill.pojo;

import java.io.Serializable;

public class Address implements Serializable {
    private Integer id;

    private String address;

    private Integer userId;

    private Boolean iisDefault;

    private static final long serialVersionUID = 1L;

    
    public Address(Integer id, String address, Integer userId, Boolean iisDefault) {
		this.id = id;
		this.address = address;
		this.userId = userId;
		this.iisDefault = iisDefault;
	}

    public Address() {};
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getIisDefault() {
        return iisDefault;
    }

    public void setIisDefault(Boolean iisDefault) {
        this.iisDefault = iisDefault;
    }
}