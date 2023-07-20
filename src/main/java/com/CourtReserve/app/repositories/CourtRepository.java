package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.Court;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourtRepository extends CrudRepository<Court, Integer> {
    Court findByCode(String code);
    List<Court> findByName(String Name);

    @Override
    void deleteById(Integer integer);
}
