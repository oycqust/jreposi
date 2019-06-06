package com.utstar.integral.repository.btoc2;

import com.utstar.integral.bean.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * created by UTSC1244 at 2019/5/15 JpaRepository
 */
public interface ActivityRepository extends CrudRepository<ActivityEntity, Long>, JpaSpecificationExecutor<ActivityEntity>
{
    @Query(value = "select * from CATEGORY", nativeQuery = true)
    List<Object[]> selectTest();

    ActivityEntity findByActivityId(String activityId);

    @Modifying
    @Transactional
    @Query(value="UPDATE CREDB.ACP_POINT_CONF SET STATUS = 2 WHERE confid IN (:ids)", nativeQuery = true)
    int deleteByIds(@Param("ids")Iterable<Long> ids);

    List<ActivityEntity> findByActivityIdIn(List<String> ids);

    @Query(value="SELECT MAX(activityId) from CREDB.ACP_POINT_CONF", nativeQuery = true)
    String selectMaxActivityId();
}
