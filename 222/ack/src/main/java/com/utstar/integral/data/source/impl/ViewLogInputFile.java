package com.utstar.integral.data.source.impl;

import com.utstar.common.FileUtil;
import com.utstar.common.RedisKeyConstant;
import com.utstar.config.SelfConfigReader;
import com.utstar.integral.bean.InputResult;
import com.utstar.integral.bean.UserViewLog;
import com.utstar.integral.data.source.ViewLogInput;
import com.utstar.integral.redis.dao.RedisCommonDAO;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 数据来源是文件系统
 * @author UTSC0928
 * @date 2018/6/4
 */
@Component
public class ViewLogInputFile implements ViewLogInput {

    private static final Logger LOGGER = LoggerFactory.getLogger(ViewLogInputFile.class);

    @Resource
    private SelfConfigReader selfConfigReader;

    @Resource
    private RedisCommonDAO redisCommonDAO;

    @Override
    public InputResult getInputResult() {
        InputResult inputResult = new InputResult();

        String cdrTime = redisCommonDAO.get(RedisKeyConstant.INTEGRAL_CDR_TIME);
        List<File> waitForHandleList = FileUtil.getListCdrFileByDir(selfConfigReader.getDataDir(), cdrTime);

        if(waitForHandleList.size() > 0){
            LOGGER.info("{} is {}",RedisKeyConstant.INTEGRAL_CDR_TIME,cdrTime);

            File file = waitForHandleList.get(0);
            String fileCdrTime = FileUtil.getTimeFromFile(file);
            //可能有cdrTime相同的多个文件,將cdrTime相同的文件的內容整合成一個InputResult
            List<File> fileList = FileUtil.getDuplicateFileByCdrTime(selfConfigReader.getDataDir(), fileCdrTime);
            List<UserViewLog> userViewLogs = new ArrayList<>();
            fileList.forEach(dupFile -> {
                LOGGER.info("{} is handling......",dupFile.getName());
                userViewLogs.addAll(FileUtil.readUserViewLogsFromFile(dupFile));
            });

            inputResult.setUserViewLogs(userViewLogs);
            LOGGER.info("read {} userviewlog lines ",userViewLogs.size());
            inputResult.setCdrTime(fileCdrTime);

        }

        return inputResult;
    }
}
