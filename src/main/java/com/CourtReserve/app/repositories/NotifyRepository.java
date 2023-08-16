package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.Game;
import com.CourtReserve.app.models.Notifies;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NotifyRepository extends CrudRepository<Notifies, Integer> {
    List<Notifies> findByUserOrderByIdAsc(String user);


    List<Notifies> findByUserAndStatusOrderByIdAsc(String mobileNo, String status);
}