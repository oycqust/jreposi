package com.utstar.listener;

import com.utstar.common.DateTimeUtil;
import com.utstar.common.Dom4jUtil;
import com.utstar.common.RedisKeyConstant;
import com.utstar.config.SelfConfigReader;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import com.utstar.integral.service.CommonService;
import com.utstar.integral.thread.IntegralWorkerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 *
 * @author UTSC0928
 * @date 2018/3/9
 */
@Component
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartUpListener.class);

    @Resource
    private IntegralWorkerThread integralWorkerThread;

    @Resource
    private SelfConfigReader selfConfigReader;

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @Resource
    private CommonService commonService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(null == event.getApplicationContext().getParent()){
            //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。

            //验证应用参数是否正常
            //validate();

            loadItems();

            String cdrTime = redisCommonDAO.get(RedisKeyConstant.INTEGRAL_CDR_TIME);
            if(cdrTime == null){
                redisCommonDAO.set(RedisKeyConstant.INTEGRAL_CDR_TIME, DateTimeUtil.getCurrentTime());
            }
//            redisCommonDAO.set(RedisKeyConstant.INTEGRAL_CDR_TIME, "201808082100");



            LOGGER.info("IntegralWorkerThread is starting....");
            //integralWorkerThread.start();

        }
    }

    private void validate(){
        boolean isOk = true;
        String errMsg = "";
        if(RedisKeyConstant.HOSTNAME == null){
            isOk = false;
            errMsg = "cannot get hostname from local. please check.";
            throw new RuntimeException(errMsg);
        }
        if(StringUtils.isEmpty(selfConfigReader.getDataDir())){
            isOk = false;
            errMsg = "data.dir is not set.";
        }else {
            File dataDir = new File(selfConfigReader.getDataDir());
            if(!(dataDir.exists() && dataDir.isDirectory())){
                isOk = false;
                errMsg = "data.dir is not correct. it should be a directory.";
            }
        }
        if(!isOk){
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * 加载所有的积分项
     */
    private void loadItems(){
        String integralHome = System.getProperty("integral.home", "/opt/spark/integral_avtivity");
        List<ItemBean> validItems = Dom4jUtil.getValidItems(integralHome + "/config/items.xml");
        ItemBean.initItems(validItems);

        validItems.forEach(itemBean -> {
            if(ItemBean.WORLD_MATCH.equals(itemBean.getItemId())){
                commonService.loadWorldMatchMediacodeToRedis(itemBean.getSysids(),itemBean.getOptags());
            }
            if(ItemBean.VOTING.equals(itemBean.getItemId())){
                commonService.loadCategoryacodeToRedis(itemBean.getSysids(),itemBean.getMediaCodeCategory());
            }
        });
    }
}
