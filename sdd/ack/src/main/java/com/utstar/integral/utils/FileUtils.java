package com.utstar.integral.utils;

import com.utstar.common.Dom4jUtil;
import com.utstar.integral.bean.ItemBean;
import com.utstar.integral.service.impl.LoginServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

/**
 * created by UTSC1244 at 2019/5/9 0009
 */
@Slf4j
public class FileUtils
{
    public static String getFileContendFromClassPath(String fileName)
    {
        try
        {
            String integralHome = System.getProperty("integral.home", "/opt/spark/integral_avtivity");
            /*URL url = FileUtils.class.getClassLoader().getResource(integralHome+"/config/" + fileName);
            if(url == null)
            {
                log.error("the file {} is not exists,because url is null", fileName);
                return "";
            }*/
            String path = integralHome+"/config/" + fileName;
            File file = new File(path);
            if(!file.exists())
            {
                log.error("the file {} is not exists!, path is {}", fileName, path);
                return "";
            }
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine = "";
            while ((readLine = br.readLine()) != null)
            {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        }
        catch (Exception e)
        {
            log.error("fail to get file..", e);
            return "";
        }
    }
}
