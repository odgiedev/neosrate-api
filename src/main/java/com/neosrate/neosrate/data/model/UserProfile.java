package com.neosrate.neosrate.data.model;

import jakarta.persistence.*;

@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String username;

    private String bio;

    @Column(name = "community_owner")
    private String communityOwner;

    @Column(name = "community_participant")
    private String communityParticipant;
}
