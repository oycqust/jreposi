package com.utstar.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utstar.common.LoginInterceptor;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册拦截器
 */
@Configuration
public class MVCConfig extends WebMvcConfigurerAdapter
{
    @Bean
    public LoginInterceptor loginInterceptor()
    {
        return new LoginInterceptor();
    }


    //此方法位于一个有@Configuration注解的类中
    /*@Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter converter  = new StringHttpMessageConverter(Charset.forName("UTF-8"));
        return converter;
    }


    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        //设置日期格式
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd");
        objectMapper.setDateFormat(smt);
        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
        //设置中文编码格式
        List<MediaType> list = new ArrayList<MediaType>();
        list.add(MediaType.APPLICATION_JSON_UTF8);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(list);
        return mappingJackson2HttpMessageConverter;
    }*/

    /*@Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、创建FastJson信息转换对象
        FastJsonHttpMessageConverter fastConverter=new FastJsonHttpMessageConverter();
        //2、创建FastJsonConfig对象并设定序列化规则  序列化规则详见SerializerFeature类中，后面会讲
        FastJsonConfig fastJsonConfig= new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.WriteNonStringKeyAsString);
        //本人就坑在WriteNonStringKeyAsString 将不是String类型的key转换成String类型，否则前台无法将Json字符串转换成Json对象

        //3、中文乱码解决方案
        List<MediaType> fastMedisTypes = new ArrayList<MediaType>();
        fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);//设定Json格式且编码为utf-8
        fastConverter.setSupportedMediaTypes(fastMedisTypes);
        //4、将转换规则应用于转换对象
        fastConverter.setFastJsonConfig(fastJsonConfig);
        return new HttpMessageConverters(fastConverter);
    }*/

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(loginInterceptor())
                .excludePathPatterns("/login")
                .addPathPatterns("/**");
    }

   /* FastJsonHttpMessageConverter fastJsonHttpMessageConverter()
    {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter=new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig=new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
        SerializerFeature.QuoteFieldNames,
        SerializerFeature.WriteMapNullValue,
        SerializerFeature.DisableCircularReferenceDetect,
        SerializerFeature.WriteDateUseDateFormat,
        SerializerFeature.WriteNullStringAsEmpty);

        List<MediaType> mediaTypeList=new ArrayList<>();
        mediaTypeList.add(MediaType.APPLICATION_JSON_UTF8);
        mediaTypeList.add(MediaType.APPLICATION_JSON);
        fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypeList);
        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
        return fastJsonHttpMessageConverter;
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters)
    {
        //converters.removeIf(t -> t instanceof MappingJackson2HttpMessageConverter);
        converters.clear();
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
        Charset.forName("UTF-8"));
        converters.add(converter);
        converters.add(fastJsonHttpMessageConverter());
    }*/

}
