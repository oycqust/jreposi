package com.utstar.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceCredbConfig
{
  @Bean(name = "credbDS")
  @Qualifier("credbDS")
  @ConfigurationProperties(prefix="ud.credb.datasource")
  public DataSource secondaryDataSource(){
    return DataSourceBuilder.create().build();
  }
}