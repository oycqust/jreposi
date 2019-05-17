package com.utstar.integral.repository;

import com.utstar.integral.bean.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * created by UTSC1244 at 2019/5/15 0015
 */
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long>, JpaSpecificationExecutor<ActivityEntity> {
}
