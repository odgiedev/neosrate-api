package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class Community {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private String creator;

    @Column(name = "total_participant")
    private String totalParticipant;

    @Column(name = "total_post")
    private String totalPost;

    @Column(name = "created_at")
    private String createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTotalParticipant() {
        return totalParticipant;
    }

    public void setTotalParticipant(String totalParticipant) {
        this.totalParticipant = totalParticipant;
    }

    public String getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(String totalPost) {
        this.totalPost = totalPost;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
