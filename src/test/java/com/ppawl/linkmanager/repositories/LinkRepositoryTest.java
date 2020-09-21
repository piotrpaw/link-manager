package com.ppawl.linkmanager.repositories;

import com.ppawl.linkmanager.repositories.model.LinkEntity;
import net.andreinc.mockneat.MockNeat;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;



@RunWith(SpringRunner.class)
@DataJpaTest
public class LinkRepositoryTest {

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Autowired
    EntityManager entityManager;

    @Autowired
    private LinkRepository linkRepository;

    @Test
    public void when_findAll_then_return_non_empty_list() {
        // given
        var entity = mockNeat
                .reflect(LinkEntity.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .val();

        entityManager.persist(entity);
        entityManager.flush();

        // when
        List<LinkEntity> linkList = linkRepository.findAll();

        // then
        Assertions.assertThat(linkList)
                .hasSize(1)
                .extracting(LinkEntity::getName)
                .contains(entity.getName());

    }

    @Test
    public void whenFindByUrlContaining_thenReturnLinkResource() {
        // given
        var entityList = mockNeat
                .reflect(LinkEntity.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .list(5)
                .val();

        entityManager.persist(entityList);
        entityManager.flush();

        // when
        var link = entityList.stream().findFirst();
        List<LinkEntity> linkList = linkRepository.findByUrlContaining(link.get().getUrl());

        // then
        Assertions.assertThat(linkList)
                .hasSize(1)
                .extracting(LinkEntity::getUrl)
                .contains(link.get().getUrl());

    }

}