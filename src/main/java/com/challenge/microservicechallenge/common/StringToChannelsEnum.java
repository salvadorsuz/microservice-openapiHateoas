package com.challenge.microservicechallenge.common;

import com.challenge.microservicechallenge.model.Channels;
import org.springframework.core.convert.converter.Converter;

public class StringToChannelsEnum implements Converter<String, Channels> {
    @Override
    public Channels convert(String source) {
        try {
            return Channels.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
