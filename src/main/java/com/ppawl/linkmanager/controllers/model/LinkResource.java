package com.ppawl.linkmanager.controllers.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkResource {

    private long id;
    private String url;
    private String name;

}
