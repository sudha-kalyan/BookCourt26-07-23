package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.UserLog;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserLogRepository extends CrudRepository<UserLog,Integer> {
    @Query("select * from user where mobile=?1")
    UserLog findById(Long id);

    UserLog findByMobileNoAndStatus(String mobileNo, String active);

    UserLog findBySessionIdAndStatus(String sessionId,String active);


}
