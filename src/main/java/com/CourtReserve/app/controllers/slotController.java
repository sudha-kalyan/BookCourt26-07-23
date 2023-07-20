package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.Court;
import com.CourtReserve.app.models.Slot;
import com.CourtReserve.app.models.DayType;
import com.CourtReserve.app.repositories.CourtRepository;
import com.CourtReserve.app.repositories.DayTypeRepository;
import com.CourtReserve.app.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class slotController {
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private DayTypeRepository dayTypeRepository;

    @RequestMapping("/slots")
    public String slots(Model model){
        List<Court> courts = (List<Court>) courtRepository.findAll();
        model.addAttribute("courts_itr", courtRepository.findAll());
        Iterable<Slot> slots = slotRepository.findAll();
        Iterable<DayType> dayTypes = dayTypeRepository.findAll();
        Court court = new Court();
        court.setCode("all");
        courts.add(0,court);
        model.addAttribute("courts", courts);
        model.addAttribute("slots", slots);
        model.addAttribute("dayTypes", dayTypes);


        return "masters/slot";
    }

    @PostMapping("/slots")
    public String addSlots(@RequestParam Map body){
        System.out.println(body);
        if (!body.get("courtCode").equals("all")){
            Slot slot = new Slot();
            slot.setSlotLength(Integer.parseInt(body.get("slotLength").toString()));
            slot.setEndHour(body.get("endHour").toString());
            slot.setStartHour(body.get("startHour").toString());
            slot.setCourtCode(body.get("courtCode").toString());
            slot.setDayType(body.get("dayType").toString());
            List<Slot> slots = slotRepository.findByDayTypeAndCourtCode(body.get("dayType").toString(),body.get("courtCode").toString());

            String slotCode = body.get("courtCode") +"-"+ body.get("dayType") + "/"+"1";

        if (!slots.isEmpty()){
            Slot slotLatest = slots.get(slots.size()-1);
            slotCode =  slotLatest.getSlotCode().split("/")[0] + "/"+ (Integer.parseInt(slotLatest.getSlotCode().split("/")[1]) +1);
        }
        slot.setSlotCode(slotCode);
        slotRepository.save(slot);
        }
        else{
            List<Court> courts = (List<Court>) courtRepository.findAll();
            for (Court court: courts){
                Slot slot = new Slot();
                slot.setSlotLength(Integer.parseInt(body.get("slotLength").toString()));
                slot.setEndHour(body.get("endHour").toString());
                slot.setStartHour(body.get("startHour").toString());
                slot.setCourtCode(court.getCode());
                slot.setDayType(body.get("dayType").toString());
                List<Slot> slots = slotRepository.findByDayTypeAndCourtCode(body.get("dayType").toString(),court.getCode());
                String slotCode = court.getCode() +"-"+ body.get("dayType") + "/"+"1";
                if (!slots.isEmpty()){
                    Slot slotLatest = slots.get(slots.size()-1);
                    slotCode =  slotLatest.getSlotCode().split("/")[0] + "/"+ (Integer.parseInt(slotLatest.getSlotCode().split("/")[1]) +1);
                }
                slot.setSlotCode(slotCode);
                slotRepository.save(slot);
            }
        }
        return "redirect:/slots";
    }
    @PostMapping("/slotAddMultiple")
    public String slotAddMultiple(@RequestParam Map body){
        int slotLength = Integer.parseInt(body.get("slotLength").toString());
        int startHour = Integer.parseInt(body.get("startHour").toString().split(":")[0]);
        int startMinutes = Integer.parseInt(body.get("startHour").toString().split(":")[1]);
        int endHour = Integer.parseInt(body.get("endHour").toString().split(":")[0]);
        int endMinutes = Integer.parseInt(body.get("endHour").toString().split(":")[1]);
        while (startHour < endHour ||(startHour == endHour && startMinutes< endMinutes)){
            int endMinutesTemp = startMinutes;
            int endHourTemp = startHour;
            if (slotLength == 60){
                endHourTemp = startHour +1;
            } else if (slotLength < 60) {
                if ((slotLength+startMinutes) < 60){
                    endMinutesTemp = slotLength+startMinutes;
                } else if ((slotLength +startMinutes) == 60) {
                    endMinutesTemp = 0;
                    endHourTemp = startHour + 1;
                } else if ((slotLength + startMinutes) >60) {
                    endMinutesTemp = startMinutes + slotLength -60;
                    endHourTemp = startHour + 1;
                }
            } else {
                endMinutesTemp =  (startMinutes + slotLength)%60;
                endHourTemp = startHour+ ((startMinutes+slotLength)/60);
            }
            String endHourTempS = String.valueOf(endHourTemp);
            String endMinutesTempS = String.valueOf(endMinutesTemp);
            String startHourTempS = String.valueOf(startHour);
            String startMinutesTempS = String.valueOf(startMinutes);

            if (endHourTemp < 10){
                endHourTempS = "0"+endHourTemp;
            }
            if (endMinutesTemp < 10){
                 endMinutesTempS = "0"+endMinutesTemp;
            }
            if (startHour < 10){
                 startHourTempS = "0"+startHour;
            }
            if (startMinutes < 10){
                startMinutesTempS = "0"+startMinutes;
            }
            if (!body.get("courtCode").equals("all")){
                Slot slot = new Slot();
                slot.setSlotLength(Integer.parseInt(body.get("slotLength").toString()));
                slot.setEndHour(endHourTempS + ":"+ endMinutesTempS);
                slot.setStartHour(startHourTempS + ":"+ startMinutesTempS);
                slot.setCourtCode(body.get("courtCode").toString());
                slot.setDayType(body.get("dayType").toString());
                List<Slot> slots = slotRepository.findByDayTypeAndCourtCode(body.get("dayType").toString(),body.get("courtCode").toString());

                String slotCode = body.get("courtCode") +"-"+ body.get("dayType") + "/"+"1";

                if (!slots.isEmpty()){
                    Slot slotLatest = slots.get(slots.size()-1);
                    slotCode =  slotLatest.getSlotCode().split("/")[0] + "/"+ (Integer.parseInt(slotLatest.getSlotCode().split("/")[1]) +1);
                }
                slot.setSlotCode(slotCode);
                slotRepository.save(slot);
            }
            else{
                List<Court> courts = (List<Court>) courtRepository.findAll();
                for (Court court: courts){
                    Slot slot = new Slot();
                    slot.setSlotLength(Integer.parseInt(body.get("slotLength").toString()));
                    slot.setEndHour(endHourTempS + ":"+ endMinutesTempS);
                    slot.setStartHour(startHourTempS + ":"+ startMinutesTempS);
                    slot.setCourtCode(court.getCode());
                    slot.setDayType(body.get("dayType").toString());
                    List<Slot> slots = slotRepository.findByDayTypeAndCourtCode(body.get("dayType").toString(),court.getCode());
                    String slotCode = court.getCode() +"-"+ body.get("dayType") + "/"+"1";
                    if (!slots.isEmpty()){
                        Slot slotLatest = slots.get(slots.size()-1);
                        slotCode =  slotLatest.getSlotCode().split("/")[0] + "/"+ (Integer.parseInt(slotLatest.getSlotCode().split("/")[1]) +1);
                    }
                    slot.setSlotCode(slotCode);
                    slotRepository.save(slot);
                }

            }
            startHour = endHourTemp;
            startMinutes =endMinutesTemp;
        }


        return "redirect:/slots";
    }


}
