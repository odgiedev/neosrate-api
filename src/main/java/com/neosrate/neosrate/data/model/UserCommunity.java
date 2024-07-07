package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class UserCommunity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String community;

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }
}
