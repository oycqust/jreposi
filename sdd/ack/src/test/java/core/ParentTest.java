package core;

import com.utstar.IntegralActivityApplicationDev;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.redis.dao.RedisLockDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by UTSC0928 on 2018/5/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {IntegralActivityApplicationDev.class},webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
public class ParentTest {


}
