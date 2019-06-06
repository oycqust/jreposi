import com.utstar.IntegralActivityApplicationDev;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.redis.dao.RedisLockDAO;
import core.TestApplicationContextInitializer;
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
public class RedisTest {

    /*@Resource
    private CommonRepository commonRepository;*/

    @Resource
    private RedisCommonDAO redisCommonDAO;

    /*@Resource
    private SelfConfigReader selfConfigReader;*/

    @Resource
    private RedisLockDAO redisLockDAO;

    @Test
    public void test01(){
        redisCommonDAO.keys("*").forEach(obj -> System.out.println(obj));
    }

    @Test
    public void test02(){
        System.out.println(redisCommonDAO.keys("*"));
    }

    @Test
    public void test03(){
        List<String> list = new ArrayList<>();
        list.add("0521");
        list.add("1521");
//        commonRepository.getMediacodeFromOptags(selfConfigReader.getWorldMatchSysids(),list);
    }

    @Test
    public void test04(){
        boolean lock = redisLockDAO.tryGetDistributedLock("lock_patrick", "patrick001", 100);
        System.out.println(lock);

        boolean release_1 = redisLockDAO.releaseDistributedLock("lock_patrick", "patrick002");
        boolean release_2 = redisLockDAO.releaseDistributedLock("lock_patrick", "patrick001");
        System.out.println(release_1);
        System.out.println(release_2);
    }

    @Test
    public void test05(){
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(true == true);
        System.out.println(true == false);
        Map<String, Long> map = new HashMap<>();
        Map<String, Long> linkedMap = new LinkedHashMap<>();
        for(int i = 0; i<10; i++)
        {
            map.put("key"+i, (long)i);
            linkedMap.put("key"+i, (long)i);
        }

        Set<String> set = map.keySet();
        Set<String> linkedSet = linkedMap.keySet();
        System.out.println(set);
        System.out.println(linkedSet);

    }
}
