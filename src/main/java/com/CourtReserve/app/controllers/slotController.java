package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.*;
import com.CourtReserve.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    private BookSlotRepository bookSlotRepository;
    @Autowired
    private UserRepository userRepository;

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
    @GetMapping("/slotViewData")
    public String slotViewOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("true") ){
            // List<User> users = (List<User>) userRepository.findAll();
            List<User> users =  userRepository.findByOrderByIdDesc();
            System.out.println("users:"+users);
            Slot s= new Slot();
            model.addAttribute("user", users);
            model.addAttribute("slot", s);
            return "customer/viewSlots";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";

    }
    @PostMapping("/slotViewData")
    public String slotViewOrder(@RequestParam Map<String, String> body,Model model,HttpServletResponse response,HttpServletRequest request) {
        System.out.println("88888888888");
        System.out.println("body8:"+body);
        List<User> users =  userRepository.findByOrderByIdDesc();
        System.out.println("users:"+users.size());
        System.out.println("users:"+users);
        Slot s= new Slot();
        model.addAttribute("user", users);
        model.addAttribute("slot", s);
        System.out.println("body11:"+body.get("bookedBy"));
        System.out.println("body14:"+body.get("bookedBy"));
        System.out.println("body12:"+body.get("gameDate"));
        String mobileNo="";
        //mobileNo = "7799259170";
        System.out.println("mobileNo:"+mobileNo);
        LocalDate date= LocalDate.parse(body.get("gameDate"));
        System.out.println("date:"+date);
        // List<BookSlot> list = bookSlotRepository.findByBookedByAndGameDate("8096572471",LocalDate.parse("2023-07-22"));
        List<BookSlot> list = bookSlotRepository.findByGameDateAndBookedBy(date,mobileNo);
        // List<BookSlot> list = bookSlotRepository.findByBookedByAndGameDate(mobileNo1,date);

        System.out.println("list:"+list);
        model.addAttribute("list", list);
        System.out.println("Excel Size -- " + list.size());
        return "customer/viewSlots";
    }
}
