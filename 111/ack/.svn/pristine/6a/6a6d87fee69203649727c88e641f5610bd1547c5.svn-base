package com.utstar.common;

import com.utstar.integral.bean.ItemBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author UTSC0928
 * @date 2018/6/1
 */
public class Dom4jUtil {

    public static String getWorldMatchCron(){
        String integralHome = System.getProperty("integral.home", "/opt/spark/integral_avtivity");
        SAXReader saxReader = new SAXReader();
        File file = new File(integralHome + "/config/items.xml");
        try {
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator("Item");
            while (iterator.hasNext()){
                Element element = iterator.next();
                if(ItemBean.WORLD_MATCH.equals(element.elementText("Id"))){
                    return element.elementText("Cron");
                }
            }

        } catch (DocumentException e) {
            LoggerFactory.getLogger(Dom4jUtil.class).error("parse WorldMatchCron failed. ",e);
        }
        return null;
    }

    public static List<ItemBean> getValidItems(String url){
        List<ItemBean> itemBeanList = new ArrayList<>();

        SAXReader saxReader = new SAXReader();
        File file = new File(url);
        try {
            Document document = saxReader.read(file);
            Element rootElement = document.getRootElement();
            Iterator<Element> iterator = rootElement.elementIterator("Item");
            iterator.forEachRemaining(element -> {
                String valid = element.elementText("Valid");
                if("true".equals(valid)){
                    String itemId = element.elementText("Id");
                    String optags = element.elementText("Optags");
                    String sysid = element.elementText("Sysid");
                    String mediaCodeFile = element.elementText("MediaCodeFile");
                    String minViewMinutes = element.elementText("MinViewMinutes");
                    String maxCoresPerDay = element.elementText("MaxCoresPerDay");
                    String mediaCodeCategory = element.elementText("MediaCodeCategory");
                    String secViewSecond = element.elementText("SecViewSecond");
                    ItemBean itemBean = new ItemBean();
                    itemBean.setItemId(itemId);
                    if(!StringUtils.isEmpty(optags)){
                        itemBean.setOptags(Arrays.stream(optags.split(",")).collect(Collectors.toList()));
                    }
                    if(!StringUtils.isEmpty(sysid)){
                        itemBean.setSysids(Arrays.stream(sysid.split(",")).collect(Collectors.toList()));
                    }
                    if(!StringUtils.isEmpty(mediaCodeFile)){
                        itemBean.setMediacodeSet(FileUtil.readMediaCode(mediaCodeFile));
                    }
                    if(!StringUtils.isEmpty(minViewMinutes)){
                        itemBean.setMinViewMinutes(minViewMinutes);
                    }
                    if(!StringUtils.isEmpty(maxCoresPerDay)){
                        itemBean.setMaxCoresPerDay(maxCoresPerDay);
                    }
                    if(!StringUtils.isEmpty(mediaCodeCategory)){
                        itemBean.setMediaCodeCategory(Arrays.asList(mediaCodeCategory.split(",")));
                    }
                    if(!StringUtils.isEmpty(secViewSecond)){
                        itemBean.setSecViewSecond(secViewSecond);
                    }
                    itemBeanList.add(itemBean);
                }
            });
        } catch (DocumentException e) {
            LoggerFactory.getLogger(Dom4jUtil.class).error("parse "+url+" failed. ",e);
        }

        return itemBeanList;
    }

    public static void main(String[] args) {
        List<ItemBean> validItems = getValidItems("D:\\sop_idea\\activity_pro\\wolrdMatch\\src\\main\\config\\items.xml");
        System.out.println(validItems);
    }
}
