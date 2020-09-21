package com.ppawl.linkmanager.repositories;

import com.ppawl.linkmanager.repositories.model.LinkEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

    List<LinkEntity> findByUrlContaining(String url);

    List<LinkEntity> findAll();

}
