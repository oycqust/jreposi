import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by UTSC0928 on 2018/5/30.
 */
public class TestApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        MapPropertySource systemProperties = (MapPropertySource) propertySources.get("systemProperties");
        Map<String, Object> source = systemProperties.getSource();
        /*source.put("spring.config.location","D:\\svn_idea\\ack\\src\\main\\config\\application.properties");
        source.put("spring.pid.fail-on-write-error","true");
        //source.put("spring.pid.file","/opt/spark/avtivity/world_match/ai.pid");
        source.put("logging.config","D:\\svn_idea\\ack\\src\\main\\config\\logback.xml");*/

        source.put("spring.config.location","D:\\svn_idea\\ack\\src\\main\\config\\application.properties");
        source.put("spring.pid.fail-on-write-error","true");
        source.put("spring.pid.file","D:\\svn_idea\\ack\\ack.pid");
        source.put("logging.config","D:\\svn_idea\\ack\\src\\main\\config\\logback.xml");
        source.put("integral.home","D:\\svn_idea\\ack\\src\\main");

        Map<String,Object> map = new HashMap<>();
        map.put("spring.redis.database",12);
        map.put("spring.redis.sentinel.nodes","10.80.248.24:26379");
        map.put("spring.redis.sentinel.master","mymaster");
        map.put("spring.redis.password","rss123");
        map.put("spring.redis.pool.max-active",100);

        map.put("ud.botc2.datasource.driverClassName","oracle.jdbc.OracleDriver");
        map.put("ud.botc2.datasource.url","jdbc:oracle:thin:@10.80.248.24:1521:orcl");
        map.put("ud.botc2.datasource.username","bto_c2");
        map.put("ud.botc2.datasource.password=","oss");

        map.put("ud.credb.datasource.driverClassName","oracle.jdbc.OracleDriver");
        map.put("ud.credb.datasource.url","jdbc:oracle:thin:@10.80.248.24:1521:orcl");
        map.put("ud.credb.datasource.username","credb");
        map.put("ud.credb.datasource.password=","oss");
        /*ud.botc2.datasource.driverClassName=oracle.jdbc.OracleDriver
        ud.botc2.datasource.url=jdbc:oracle:thin:@10.80.248.24:1521:orcl
        ud.botc2.datasource.username=bto_c2
        ud.botc2.datasource.password=oss

        ud.credb.datasource.driverClassName=oracle.jdbc.OracleDriver
        ud.credb.datasource.url=jdbc:oracle:thin:@10.80.248.24:1521:orcl
        ud.credb.datasource.username=credb
        ud.credb.datasource.password=oss*/
        MapPropertySource applicationConfig = new MapPropertySource("applicationConfig",map);
        propertySources.addLast(applicationConfig);
    }
}
