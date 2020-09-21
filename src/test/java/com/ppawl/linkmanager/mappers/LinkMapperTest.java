package com.ppawl.linkmanager.mappers;

import com.ppawl.linkmanager.controllers.model.LinkResource;
import com.ppawl.linkmanager.repositories.model.LinkEntity;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class LinkMapperTest {

    private final MockNeat mockNeat = MockNeat.threadLocal();

    @Test
    void when_non_empty_entity_then_return_non_empty_resource() {
        //given
        var entity = mockNeat
                .reflect(LinkEntity.class)
                .field("name", mockNeat.names().full())
                .field("url", mockNeat.urls())
                .field("id", mockNeat.ints())
                .val();

        //when
        LinkResource result = LinkMapper.ToResource(entity);

        //then
        assertThat(entity).isEqualToComparingFieldByField(result);

    }
}