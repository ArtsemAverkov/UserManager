package ru.clevertec.UserManager.config;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters (List<HttpMessageConverter<?>> converters) {
        converters.add (new ProtobufHttpMessageConverter());
    }
}