package com.ppawl.linkmanager.services;

import com.ppawl.linkmanager.controllers.model.LinkResource;
import com.ppawl.linkmanager.mappers.LinkMapper;
import com.ppawl.linkmanager.repositories.LinkRepository;
import com.ppawl.linkmanager.repositories.model.LinkEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
@Scope("singleton")
public class LinkService {


    private LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Async("processExecutor")
    synchronized public CompletableFuture<List<LinkResource>> getAllWithCounter(int counter) throws InterruptedException {
        Thread.sleep(3000L);
        List<LinkEntity> all = linkRepository.findAll();
        log.info("service " + counter);
        List<LinkResource> collect = all.stream().map(LinkMapper::ToResource).collect(Collectors.toList());
        return CompletableFuture.completedFuture(collect);
    }

    @Async("processExecutor")
    public CompletableFuture<List<LinkResource>> getAll() throws InterruptedException {
        Thread.sleep(10000L);
        List<LinkEntity> all = linkRepository.findAll();
        List<LinkResource> linkList = all.stream().map(LinkMapper::ToResource).collect(Collectors.toList());
        return CompletableFuture.completedFuture(linkList);
    }

    @Async("processExecutor")
    public CompletableFuture<List<LinkResource>> getListOfUrlContainingText(String text) throws InterruptedException {
        Thread.sleep(1000L);
        List<LinkEntity> all = linkRepository.findByUrlContaining(text);
        List<LinkResource> linkList = all.stream().map(LinkMapper::ToResource).collect(Collectors.toList());
        return CompletableFuture.completedFuture(linkList);
    }

}
