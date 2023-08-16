package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.DayType;
import com.CourtReserve.app.models.SpecialDates;
import com.CourtReserve.app.repositories.CourtRepository;
import com.CourtReserve.app.repositories.DayTypeRepository;
import com.CourtReserve.app.repositories.SpecialDatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class specialDatesController {

    @Autowired
    private SpecialDatesRepository specialDatesRepository;
    @Autowired
    private DayTypeRepository dayTypeRepository;
    @Autowired
    private CourtRepository courtRepository;

    @RequestMapping("/specialDates")
    public String showSpecialDates(Model model){
        Iterable<SpecialDates> specialDates = specialDatesRepository.findAll();
        model.addAttribute("courts", courtRepository.findAll());
        model.addAttribute("specialDates", specialDates);
        model.addAttribute("dayTypes", dayTypeRepository.findAll());

        return "masters/specialDates";
    }
    @PostMapping("/specialDates")
    public String addSpecialDate(@ModelAttribute SpecialDates specialDates, Model model){
        specialDatesRepository.save(specialDates);

        return "redirect:/specialDates";
    }
}
