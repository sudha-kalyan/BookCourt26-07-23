package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.SpecialDates;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SpecialDatesRepository extends CrudRepository<SpecialDates, Integer> {
    SpecialDates findByDate(String date);
}