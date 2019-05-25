package com.utstar.integral.repository.credb;

import com.utstar.integral.bean.VueTreeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * created by UTSC1244 at 2019/5/23
 */
@Repository
@Slf4j
public class MediaCodeRepository
{
    @PersistenceContext(unitName = "credbPersistenceUnit")
    private EntityManager entityManager;

    /**
     * 通过栏目id和平台id获取媒资code
     * @param categoryIds 栏目id
     * @param sysids 平台id
     * @return  Map<String, Set<String>> key:sysid value 媒资code的set
     */
    public Map<String, Set<String>> getMediaCodeByCategoryIdsAndSysids(List<String> categoryIds, List<String> sysids)
    {
        Query nativeQuery = entityManager.createNativeQuery
                ("SELECT sysid,mediacode FROM view_acp_categorydtl WHERE sysid IN (:sysids) AND categoryid IN (:categoryIds)");
        nativeQuery.setParameter("sysids", sysids);
        nativeQuery.setParameter("categoryid", categoryIds);
        List<Object> resultList = nativeQuery.getResultList();
        if(CollectionUtils.isEmpty(resultList))
        {
            log.info("activity:query mediacode by categoryId is empty, categoryIds is {},sysId is {}");
            return null;
        }
        return getMediaCodeSet(resultList);
    }

    /**
     * 通过基础标签查询mediacode
     * @param basetagCodes 基础标签code
     * @return Set mediacode
     */
    public Map<String, Set<String>> getMediaCodeByBasetag(List<String> basetagCodes, List<String> sysids)
    {
        Query nativeQuery = entityManager.createNativeQuery
                ("SELECT mediacode FROM view_acp_mediabasetag WHERE basetagcode IN (:basetagCodes)");
        nativeQuery.setParameter("tagIds", basetagCodes);
        List<Object> resultList = nativeQuery.getResultList();
        if(CollectionUtils.isEmpty(resultList))
        {
            log.info("activity:query mediacode by basetagCodes is empty, categoryIds is {},sysId is {}");
            return null;
        }

        Map<String, Set<String>> result = new HashMap<>();
        Set<String> mediaCodeSetNotSysid = getMediaCodeSetNotSysid(resultList);
        if(CollectionUtils.isEmpty(mediaCodeSetNotSysid))
        {
            return result;
        }
        for (String sysid :sysids)
        {
            result.put(sysid, mediaCodeSetNotSysid);
        }
        return result;
    }

    /**
     * 通过运营标签和平台查询mediacode
     * @param optagCodes 运营标签code
     * @param sysids 运营商
     * @return Map<String, Set<String>>
     */
    public Map<String, Set<String>> getMediaCodeByoptags(List<String> optagCodes, List<String> sysids)
    {
        Query nativeQuery = entityManager.createNativeQuery
                ("SELECT sysid,mediacode FROM view_acp_mediaoptag WHERE sysid IN (:sysids) AND optagcode IN (:optagCodes)");
        nativeQuery.setParameter("sysids", sysids);
        nativeQuery.setParameter("optagCodes", optagCodes);
        List<Object> resultList = nativeQuery.getResultList();
        if(CollectionUtils.isEmpty(resultList))
        {
            log.info("activity:query mediacode by optagCodes is empty, categoryIds is {},sysId is {}");
            return null;
        }
        return getMediaCodeSet(resultList);
    }

    /**
     * 通过columnCodes 查询mediacode
     * @param columnCodes columnCodes
     * @param sysids 运营商
     * @return Set
     */
    public Map<String, Set<String>> getMediaCodeByColumnCodes(List<String> columnCodes, List<String> sysids)
    {
        Map<String, Set<String>> result = new HashMap<>();

        for (String sysid :sysids)
        {
            String sysidSqlReg = transformSysid2SqlReg(sysid);
            if(sysidSqlReg == null) continue;
            Set<String> codeSet = getMediaCodeByColumnCode(columnCodes , sysidSqlReg);
            if(codeSet.isEmpty()) continue;
            result.put(sysid, codeSet);
        }

        return result;
    }

    /**
     * 查询点播
     * @param chargetypes 点播收费，点播免费
     * @param sysids 运营商
     * @return Set
     */
    public Map<String, Set<String>> getMediaCodeByChargetypes (List<String> chargetypes, List<String> sysids)
    {
        Map<String, Set<String>> result = new HashMap<>();

        for (String sysid :sysids)
        {
            Set<String> codeSet = getMediaCodeByChargetype(chargetypes, sysid);
            if(codeSet.isEmpty()) continue;
            result.put(sysid, codeSet);
        }

        return result;
    }

    public List<VueTreeEntity> getCategoryByPid(Integer pid)
    {
        String sql = null;
        if(pid == null)
        {
            sql = "SELECT LEVEL,CATEGORYID,NAME FROM BTO_C2.CATEGORY WHERE LEVEL = 1 START WITH PARENTID IS NULL CONNECT BY PRIOR CATEGORYID = PARENTID";
        }else
        {
            sql = "SELECT CATEGORYID,NAME FROM BTO_C2.CATEGORY WHERE PARENTID = " + pid;
        }

        Query nativeQuery = entityManager.createNativeQuery(sql, VueTreeEntity.class);
        List<VueTreeEntity> resultList = nativeQuery.getResultList();
        return resultList;
    }

    private Set<String> getMediaCodeByChargetype(List<String> chargetypes, String sysid)
    {
        String sql = getChargetypeSql(chargetypes, sysid);
        if(sql == null) return Collections.EMPTY_SET;
        String sysidSqlReg = transformSysid2SqlReg(sysid);
        Set<String> codeSet = new HashSet<>();
        Query nativeQuery = entityManager.createNativeQuery(sql);

        nativeQuery.setParameter("sysidSqlReg", sysidSqlReg);
        List<Object> resultList = nativeQuery.getResultList();
        if(CollectionUtils.isEmpty(resultList))
        {
            log.info("activity:query mediacode by chargetype is empty, categoryIds is {},sysId is {}");
            return codeSet;
        }
        return getMediaCodeSetNotSysid(resultList);
    }

    private String getChargetypeSql(List<String> chargetypes, String sysid)
    {
        String sqlFormat = "SELECT c2code FROM WS_MERGEDMEDIA WHERE regexp_like(status,:sysidSqlReg) AND %s";
        String sqlType = "type != p";
        if(chargetypes.size() == 2)//查询所有点播
        {
            return String.format(sqlFormat, sqlType);
        }
        else if("1".equals(chargetypes))//点播收费
        {
            sqlType += " AND " + sysid + "chargetype = '0'";
        }else if("0".equals(chargetypes))//点播免费
        {
            sqlType += " AND " + sysid + "chargetype != '0'";
        }else
        {
            return null;
        }
        return String.format(sqlFormat, sqlType);
    }

    /**
     * 通过columnCodes 查询mediacode
     * @param columnCodes columnCode
     * @param sysidSqlReg sysid 正则
     * @return Set
     */
    private Set<String> getMediaCodeByColumnCode(List<String> columnCodes, String sysidSqlReg)
    {
        Set<String> codeSet = new HashSet<>();
        Query nativeQuery = entityManager.createNativeQuery
                ("SELECT c2code FROM WS_MERGEDMEDIA WHERE regexp_like(status,:sysidSqlReg) AND columntype IN (:columnCodes)");

        nativeQuery.setParameter("sysidSqlReg", sysidSqlReg);
        nativeQuery.setParameter("columnCodes", columnCodes);
        List<Object> resultList = nativeQuery.getResultList();

        if(CollectionUtils.isEmpty(resultList))
        {
            log.info("activity:query mediacode by columnCodes is empty, categoryIds is {},sysId is {}");
            return codeSet;
        }
        return getMediaCodeSetNotSysid(resultList);
    }

    private String transformSysid2SqlReg(String sysid)
    {
        String s = null;
        switch (sysid)
        {
            case "t":
                s = "0[0-9]{5,}";
                break;
            case "m":
                s = "[0-9]0[0-9]{4,}";
                break;
            case "u":
                s = "[0-9]{2}0[0-9]{3,}";
                break;
        }
        return s;
    }

    private Set<String> getMediaCodeSetNotSysid(List<Object> resultList)
    {
        Set<String> result = new HashSet<>();
        for (Object o : resultList)
        {
            if (o == null || !o.getClass().isArray())
            {
                continue;
            }
            Object[] objects = (Object[]) o;
            Object codeObj = objects[0];
            if (codeObj == null)
            {
                continue;
            }
            String mediacode = codeObj.toString();
            if (StringUtils.isBlank(mediacode))
            {
                continue;
            }
            result.add(mediacode);
        }
        return result;
    }

    private Map<String, Set<String>> getMediaCodeSet(List<Object> resultList)
    {
        Map<String, Set<String>> result = new HashMap<>();
        for (Object o : resultList)
        {
            if (o == null || !o.getClass().isArray())
            {
                continue;
            }
            Object[] objects = (Object[]) o;
            Object sysidObj = objects[0];
            Object codeObj = objects[1];
            if (sysidObj == null || codeObj == null)
            {
                continue;
            }
            String sysid = sysidObj.toString();
            String mediacode = codeObj.toString();
            if (StringUtils.isBlank(sysid) || StringUtils.isBlank(mediacode))
            {
                continue;
            }
            Set<String> codeSet = result.get(sysid);
            if (codeSet == null)
            {
                codeSet = new HashSet<>();
                result.put(sysid, codeSet);
            }
            codeSet.add(mediacode);
        }
        return result;
    }
}
