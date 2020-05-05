package com.challenge.microservicechallenge.web.mapper;

import com.challenge.microservicechallenge.service.model.Channels;
import com.challenge.microservicechallenge.web.model.ChannelsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ChannelsMapper {

    ChannelsMapper INSTANCE = Mappers.getMapper(ChannelsMapper.class);

    Channels channelsDtoToChannels(ChannelsDto channelsDto);
}
