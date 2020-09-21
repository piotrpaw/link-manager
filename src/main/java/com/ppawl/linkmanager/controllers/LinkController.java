package com.ppawl.linkmanager.controllers;

import com.ppawl.linkmanager.controllers.model.LinkResource;
import com.ppawl.linkmanager.services.LinkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@RestController()
@RequestMapping(value = "/link")
public class LinkController {

    private final AtomicInteger atomic = new AtomicInteger(0);

    private LinkService linkService;

    @Autowired
    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/getList")
    public List<LinkResource> getList() throws InterruptedException, ExecutionException {
        CompletableFuture<List<LinkResource>> list = linkService.getAll();
        return list.get();
    }

    @GetMapping("/getListElement")
    public List<LinkResource> getListContainingElement(String text) throws InterruptedException, ExecutionException {
        if (Strings.isEmpty(text)) {
            return null;
        }
        CompletableFuture<List<LinkResource>> listOfUrlContainingText = linkService.getListOfUrlContainingText(text);
        return listOfUrlContainingText.get();
    }

    @RequestMapping(value = "/getListAsync", method = RequestMethod.GET)
    public DeferredResult<ResponseEntity<List<LinkResource>>> linkDeferred(HttpServletResponse response) throws InterruptedException, ExecutionException {
        int counter = atomic.incrementAndGet();
        log.info("Deferred link request " + counter);
        DeferredResult<ResponseEntity<List<LinkResource>>> result = new DeferredResult<>();


        result.onError((Throwable t) -> {
            result.setErrorResult(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An error occurred."));
        });

        result.onCompletion(() -> log.info("request completed"));

        result.onTimeout(() -> {
                    result.setErrorResult(
                            ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
                                    .body("Request timeout occurred."));
                    log.error("timeout");
                }
        );

        CompletableFuture<List<LinkResource>> feature = linkService.getAllWithCounter(counter);
        result.setResult(ResponseEntity.ok(feature.get()));

        return result;
    }

}
