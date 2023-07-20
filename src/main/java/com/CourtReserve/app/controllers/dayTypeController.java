package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.Court;
import com.CourtReserve.app.models.DayType;
import com.CourtReserve.app.repositories.DayTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class dayTypeController {
    @Autowired
    DayTypeRepository dayTypeRepository;
    @RequestMapping("/dayTypes")
    public String showDayTypes(Model model){
        Iterable<DayType> dayTypes = dayTypeRepository.findAll();
        System.out.println(dayTypes);
        model.addAttribute("dayTypes", dayTypes);

        return "masters/dayTypes";
    }
    @PostMapping("/dayTypes")
    public String addDayType(@ModelAttribute DayType dayType, Model model){

        dayTypeRepository.save(dayType);

        return "redirect:/dayTypes";
    }
}
