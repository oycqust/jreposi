package com.utstar.integral.repository.btoc2;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author UTSC0928
 * @date 2018/5/31
 */
@Component
public class CommonRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    public List<String> getMediacodeFromOptags(List<String> sysids, List<String> optags){

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int i=0;
        for (String str : optags) {
            sb.append("(instr(optags,'");
            sb.append(str);
            sb.append("')>0)" );
            i++;
            if(i<optags.size()){
                sb.append("or");
            }
        }
        sb.append(")");

        String sql = "select code from programtag where sysid in (:sysids) and "+sb.toString()+"\n" +
                "union \n" +
                "select code from seriestag where sysid in (:sysids) and "+sb.toString();

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("sysids",sysids);
        List<String> list = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
        return list;
    }

    public List<String> getMediacodeFromCategorya(List<String> sysids, List<String> categoryacode){

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        int i=0;
        for (String str : categoryacode) {
            sb.append("(instr(code,'");
            sb.append(str);
            sb.append("')>0)" );
            i++;
            if(i<categoryacode.size()){
                sb.append("or");
            }
        }
        sb.append(")");

        String sql = "select mediacode from categorydtl where categoryid in (select CATEGORYID from category where  sysid in (:sysids) and "+sb.toString()+")";
        /*String sql = "select code from programtag where sysid in (:sysids) and "+sb.toString()+"\n" +
                "union \n" +
                "select code from seriestag where sysid in (:sysids) and "+sb.toString();*/

        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("sysids",sysids);
        List<String> list = namedParameterJdbcTemplate.queryForList(sql, paramMap, String.class);
        return list;
    }

    public List<?> getListMapBySql(String sql)
    {
        Query query = entityManager.createNativeQuery(sql);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<?> rows = query.getResultList();
        return rows;
    }
}
