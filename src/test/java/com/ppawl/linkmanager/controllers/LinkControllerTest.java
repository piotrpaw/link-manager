package com.ppawl.linkmanager.controllers;


import com.ppawl.linkmanager.controllers.model.LinkResource;
import com.ppawl.linkmanager.services.LinkService;
import net.andreinc.mockneat.MockNeat;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@RunWith(SpringRunner.class)
@WebMvcTest(value = LinkController.class)
class LinkControllerTest {

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @Test
    void getList() throws Exception {
        var listSize = 100;
        List<LinkResource> listOfResources = getLinkResources(listSize);

        Mockito.when(linkService.getAll()).thenReturn(CompletableFuture.completedFuture(listOfResources));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/link/GetList").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        var resource = listOfResources.get(1);


        Assertions.assertThat(listOfResources)
                .hasSize(listSize)
                .extracting(LinkResource::getName)
                .contains(resource.getName());


    }

    @Test
    void getListContainingElement() throws Exception {

        List<LinkResource> listOfResources = getLinkResources(5);

        Optional<LinkResource> first = listOfResources.stream().findFirst();

        Mockito.when(linkService.getListOfUrlContainingText(Mockito.anyString())).thenReturn(CompletableFuture.completedFuture(listOfResources));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/getListElement").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertThat(listOfResources)
                .hasSize(5)
                .extracting(LinkResource::getName)
                .contains(first.get().getName());
    }

    @Test
    void linkDeferred() throws Exception {
        List<LinkResource> listOfResources = getLinkResources(5);

        Optional<LinkResource> first = listOfResources.stream().findFirst();
        Mockito.when(linkService.getAllWithCounter(Mockito.anyInt())).thenReturn(CompletableFuture.completedFuture(listOfResources));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/getListElement").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assertions.assertThat(listOfResources)
                .hasSize(5)
                .extracting(LinkResource::getName)
                .contains(first.get().getName());
    }

    private List<LinkResource> getLinkResources(int i) {
        return mockNeat
                .reflect(LinkResource.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .list(i)
                .val();
    }
}