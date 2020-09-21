package com.ppawl.linkmanager.services;

import com.ppawl.linkmanager.controllers.model.LinkResource;
import net.andreinc.mockneat.MockNeat;
import org.apache.logging.log4j.util.Strings;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LinkServiceTest {


    private final LinkService linkService = mock(LinkService.class);

    private final MockNeat mockNeat = MockNeat.threadLocal();


    @Test
    void when_non_empty_db_should_return_non_empty_list_of_resources() throws InterruptedException {
       //given
        var listSize = 100;
        var listOfResources = mockNeat
                .reflect(LinkResource.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .list(listSize)
                .val();

       var resource = listOfResources.get(1);

         //when
        when(linkService.getAll()).thenReturn(CompletableFuture.completedFuture(listOfResources));
        CompletableFuture<List<LinkResource>> all = linkService.getAll();

        //then
        Assertions.assertThat(listOfResources)
                .hasSize(listSize)
                .extracting(LinkResource::getName)
                .contains(resource.getName());
    }



    @Test
    void when_searching_text_empty_then_return_empty_list() throws InterruptedException, ExecutionException {
        //given
        when(linkService.getListOfUrlContainingText(Strings.EMPTY)).thenReturn(CompletableFuture.completedFuture(Collections.emptyList()));

        //when
        CompletableFuture<List<LinkResource>> resultList = linkService.getListOfUrlContainingText(Strings.EMPTY);

        //then
        Assertions.assertThat(resultList.get()).hasSize(0);
    }

    @Test
    void when_text_is_non_empty_then_return_list() throws InterruptedException, ExecutionException {
        //given
        var listSize = 10;
        var textToContain = "text123";
        var listOfResources = mockNeat
                .reflect(LinkResource.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .list(10)
                .val();

        when(linkService.getListOfUrlContainingText(textToContain)).thenReturn(CompletableFuture.completedFuture(listOfResources));

        //when
        CompletableFuture<List<LinkResource>> resultList = linkService.getListOfUrlContainingText(textToContain);

        //then
        Assertions.assertThat(resultList.get()).hasSize(listSize);
    }
}