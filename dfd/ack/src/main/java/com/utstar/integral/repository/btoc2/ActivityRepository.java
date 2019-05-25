package com.utstar.integral.repository.btoc2;

import com.utstar.integral.bean.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * created by UTSC1244 at 2019/5/15 0015
 */
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>, JpaSpecificationExecutor<ActivityEntity>
{
    @Query(value = "select * from CATEGORY", nativeQuery = true)
    public List<Object[]> selectTest();

    public ActivityEntity findByActivityId(String activityId);
}
