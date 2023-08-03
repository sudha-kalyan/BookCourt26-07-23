package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.Slot;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SlotRepository extends CrudRepository<Slot, Integer> {
    Slot findBySlotCode(String slotCode);
    List<Slot> findByDayTypeAndCourtCode(String dayType, String courtCode);

    List<Slot> findByDayTypeOrderBySlotCodeAsc(String dayType);

    List<Slot> findByDayTypeOrderByStartHourAsc(String dayType);
}