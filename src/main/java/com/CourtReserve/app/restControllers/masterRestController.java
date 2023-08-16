package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.BookSlot;
import com.CourtReserve.app.models.Notifies;
import com.CourtReserve.app.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class masterRestController {
    private final CourtRepository courtRepository;
    private final DayTypeRepository dayTypeRepository;
    private final SpecialDatesRepository specialDatesRepository;
    private final GameRepository gameRepository;
    private final SlotRepository slotRepository;
    private final BookSlotRepository bookSlotRepository;
    private final NotifyRepository notifyRepository;

    public masterRestController(CourtRepository courtRepository,
                                DayTypeRepository dayTypeRepository,
                                SpecialDatesRepository specialDatesRepository,
                                GameRepository gameRepository,
                                SlotRepository slotRepository,
                                BookSlotRepository bookSlotRepository,
                                NotifyRepository notifyRepository) {
        this.courtRepository = courtRepository;
        this.dayTypeRepository = dayTypeRepository;
        this.specialDatesRepository = specialDatesRepository;
        this.gameRepository = gameRepository;
        this.slotRepository = slotRepository;
        this.bookSlotRepository = bookSlotRepository;
        this.notifyRepository = notifyRepository;
    }

    @PostMapping("/removeCourt")
    public ResponseEntity removeCourt(@RequestParam Map body){
        courtRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    //removeDayType

    @PostMapping("/removeDayType")
    public ResponseEntity removeDayType(@RequestParam Map body){
        dayTypeRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeSpecialDates")
    public ResponseEntity removeSpecialDates(@RequestParam Map body){
        specialDatesRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeGames")
    public ResponseEntity removeGames(@RequestParam Map body){
        gameRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
    @PostMapping("/removeSlot")
    public ResponseEntity removeSlot(@RequestParam Map body){
        slotRepository.deleteById(Integer.parseInt(body.get("id").toString()));

        return ResponseEntity.ok(new HashMap<>().putIfAbsent("msg", "success"));
    }
}
