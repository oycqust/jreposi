package com.utstar.integral.bean;

import com.utstar.common.Dom4jUtil;
import com.utstar.listener.StartUpListener;
import lombok.Data;

import java.util.*;

/**
 * 积分项
 * @author UTSC0928
 * @date 2018/6/1
 */
@Data
public class ItemBean {

    public static final String WORLD_MATCH = "001";
    public static final String HERO_CARD = "002";
    public static final String SUMMER = "003";
    public static final String VOTING = "004";


    public static Map<String,ItemBean> itemBeanMap = new HashMap<>();

    /**
     * @see StartUpListener# loadItems()
     */
    public static List<ItemBean> itemBeanList = new ArrayList<>();

    public static void initItems(List<ItemBean> validItems){
        itemBeanList.addAll(validItems);

        validItems.forEach(itemBean -> {
            itemBeanMap.put(itemBean.getItemId(),itemBean);
        });

    }

    private String itemId;

    private List<String> optags;

    private List<String> sysids;

    private Set<String> mediacodeSet;

    private String minViewMinutes;

    private String maxCoresPerDay;

    private List<String> mediaCodeCategory;

    private String secViewSecond;
}
