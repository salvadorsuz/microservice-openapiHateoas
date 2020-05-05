package com.challenge.microservicechallenge.web;

import com.challenge.microservicechallenge.web.model.ChannelsDto;
import org.springframework.core.convert.converter.Converter;

public class StringToChannelsEnum implements Converter<String, ChannelsDto> {
    @Override
    public ChannelsDto convert(String source) {
        try {
            return ChannelsDto.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
