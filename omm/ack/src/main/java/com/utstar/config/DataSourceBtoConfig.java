package com.utstar.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceBtoConfig {

  @Bean(name = "btoDS")
  @Qualifier("btoDS")
  @Primary
  @ConfigurationProperties(prefix="ud.botc2.datasource")
  public DataSource primaryDataSource(){
    return DataSourceBuilder.create().build();
  }

}