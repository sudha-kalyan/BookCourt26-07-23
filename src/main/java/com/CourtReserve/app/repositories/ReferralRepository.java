package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.Referral;
import com.CourtReserve.app.models.User;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReferralRepository extends CrudRepository<Referral, Integer> {
    // You can define additional methods for custom queries or operations
    @Query("select * from referral where code=?1")
    Referral findByCode(String Code);
}
