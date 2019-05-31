import com.utstar.integral.redis.dao.RedisCommonDAO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
public class DemoTest extends RedisTest
{
    @Autowired
    private RedisCommonDAO redisCommonDAO;
    @Test
    public void test011(){
        redisCommonDAO.keys("*").forEach(obj -> System.out.println(obj));
    }
}
