package com.utstar.common;

import com.utstar.integral.bean.ItemBean;

/**
 * @author UTSC0928
 */
public class RedisKeyConstant {

	public static final String HOSTNAME = TomcatPort.getHostName();

	public static final String INTEGRAL = "acp_";

	public static final String USERID = "userid";

	public static final String ITEMID = "itemid";

	public static final String SUBITEMID = "subitemid";

	public static final String SEPARATOR = ":";

	public static final String WATCH = "watch";

	public static final String CATEGORY = "category";

	/**
	 * 001的作为分布式锁的key , 存放的值是某个应用的hostname值
	 * 形如 acp_item_lock_001
	 */
	public static final String WORLD_MATCH_LOCK = INTEGRAL+"item_lock_"+ItemBean.WORLD_MATCH;

	/**
	 * 003的作为分布式锁的key, 存放的值是某个应用的hostname的值
	 */
	public static final String SUMMER_LOCK = INTEGRAL+"item_lock_"+ItemBean.SUMMER;

	/**
	 * 004编排下操作的作为分布式锁的key, 存放的值是某个应用的hostname的值
	 */
	public static final String VOTING_LOCK = INTEGRAL+"item_lock_"+ItemBean.VOTING;

	/**
	 * 001的作为分布式锁的key的过期时间,300秒
	 */
	public static final int WORLD_MATCH_LOCK_EXPIRE_TIME = 5*60;

	/**
	 * 003的作为分布式锁的key的过期时间,300秒
	 */
	public static final int SUMMER_LOCK_EXPIRE_TIME = 5*60;

	/**
	 * 004编排的作为分布式锁的key的过期时间,300秒
	 */
	public static final int VOTING_LOCK_EXPIRE_TIME = 5*60;

	/**
	 * 10分钟一次从数据库同步标签下的数据到redis
	 * acp_item_lock_tag_001
	 */
	public static final String WORLD_MATCH_SYNC_TAG_LOCK = INTEGRAL+"item_lock_tag_"+ItemBean.WORLD_MATCH;

	/**
	 * 获取形如 acp_userid:123456:itemid:001 的key
	 * @param userid
	 * @param itemid
	 * @return
	 */
	public static String getUserItemKey(String userid, String itemid){
		String userItemKey = INTEGRAL+USERID+SEPARATOR+userid+SEPARATOR+ITEMID+SEPARATOR+itemid;
		return userItemKey;
	}

	/**
	 * 获取形如 acp_userid:123456:itemid:002:subitemid:02000001000000050000000000000008 的key
	 * @param userid
	 * @param itemid
	 * @param subitemid
	 * @return
	 */
	public static String getUserItemSubitemKey(String userid, String itemid, String subitemid){
		return getUserItemKey(userid, itemid)+SEPARATOR+SUBITEMID+SEPARATOR+subitemid;
	}

	/**
	 * 每台sparkStreaming节点的读取cdr文件的时间 yyyyMMddHHmm
	 * 例如： acp_ut01_cdr_time
	 * acp_ut02_cdr_time
	 * acp_ut04_cdr_time
	 */
	public static final String INTEGRAL_CDR_TIME = INTEGRAL+HOSTNAME+"_cdr_time";

	/**
	 * 用户
	 */
//	public static final String INTEGRAL_USERID_ITEMID_SECOND = INTEGRAL+"two_userid_itemid_";

//	public static final String INTEGRAL_USERID_ITEMID_SUBITEMID_SECOND = INTEGRAL+"three_userid_itemid_subitemid_";

	/**
	 * 移动世界杯专区的所有mediacode的集合对应的key，该key的结构是set
	 */
	public static final String INTEGRAL_MEDIACODE_WORLD = INTEGRAL+"itemid_mediacode_"+ ItemBean.WORLD_MATCH;

	/**
	 * 投票对应栏目下面mediacode的集合对应的key，该key的结构是set
	 */
	public static String getVOTINGItemKey(String sysid){
		return INTEGRAL+"itemid_category_mediacode_"+ ItemBean.VOTING+sysid;
	}
	/**
	 * 投票对应观看视频的code key，该key的结构是hash
	 */
	public static final String INTEGRAL_MEDIACODE_VOTING = "VOTE_MEDIA_CODE";

	/**
	 * 获取形如 acp_userid:123456:itemid:004:watch:02000001000000050000000000000008 的key
	 * 获取形如 acp_userid:123456:itemid:004:category key
	 * num 是0（004观看视频播放次数）跟1（编排下code的播放）
	 * @param userid
	 * @param itemid
	 * @return
	 */
	public static String getVotingItemKey(String userid, String itemid,int num){
		String userItemKey = "";
		if(num==0){
			userItemKey = INTEGRAL+USERID+SEPARATOR+userid+SEPARATOR+ITEMID+SEPARATOR+itemid+SEPARATOR+WATCH;
		}else{
			userItemKey = INTEGRAL+USERID+SEPARATOR+userid+SEPARATOR+ITEMID+SEPARATOR+itemid+SEPARATOR+CATEGORY;
		}
		return userItemKey;
	}

}
