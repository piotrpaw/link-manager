package com.ppawl.linkmanager.mappers;

import com.ppawl.linkmanager.controllers.model.LinkResource;
import com.ppawl.linkmanager.repositories.model.LinkEntity;

public class LinkMapper {

    public static LinkResource ToResource(LinkEntity entity) {
        LinkResource resource = new LinkResource();
        resource.setId(entity.getId());
        resource.setName(entity.getName());
        resource.setUrl(entity.getUrl());
        return resource;
    }

}
