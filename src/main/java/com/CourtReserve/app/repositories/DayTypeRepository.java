package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.DayType;
import org.springframework.data.repository.CrudRepository;

public interface DayTypeRepository extends CrudRepository<DayType, Integer> {
}