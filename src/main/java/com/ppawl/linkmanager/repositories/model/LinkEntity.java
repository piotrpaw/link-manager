package com.ppawl.linkmanager.repositories.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "link")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String url;
    private String name;

}
