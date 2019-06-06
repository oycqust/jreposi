package thread;

import com.utstar.integral.bean.Constant;
import com.utstar.integral.thread.ThreadPoolManage;
import core.ParentTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * created by UTSC1244 at 2019/6/4 0004
 */
public class ThreadTest extends ParentTest {
    @Resource
    private ThreadPoolManage poolManage;

    @Test
    public void testPoolSize() throws InterruptedException {
        CountObj c = new CountObj();
        for(int i = 0; i < 21; i++){
            final int j = i;
            poolManage.getThreadPoolExecutor().submit(() ->{
                try {
                    System.out.println("thread:"+ j + " start");
                    Thread.sleep(5000);
                    System.out.println("thread:"+ j + " end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        Thread.sleep(100000);
    }

    static class CountObj{
        public int count;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
