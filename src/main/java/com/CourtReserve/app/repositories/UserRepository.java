package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select u from User u where u.mobileNo = ?1 and u.password = ?2")
    User findByMobileNoAndPassword(String mobileNo, String password);
    // You can define additional methods for custom queries or operations

    User findByMobileNo(String MobileNo);

    List<User> findByOrderByIdDesc();
    User findByPassword( String Password);
}
