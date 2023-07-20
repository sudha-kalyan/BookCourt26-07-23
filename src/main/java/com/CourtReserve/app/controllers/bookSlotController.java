package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.BookSlot;
import com.CourtReserve.app.models.Court;
import com.CourtReserve.app.models.Slot;
import com.CourtReserve.app.models.SpecialDates;
import com.CourtReserve.app.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class bookSlotController {
    private final SpecialDatesRepository specialDatesRepository;
    private final SlotRepository slotRepository;

    public bookSlotController(SpecialDatesRepository specialDatesRepository,
                              SlotRepository slotRepository,
                              CourtRepository courtRepository,
                              BookSlotRepository bookSlotRepository,
                              UserRepository userRepository) {
        this.specialDatesRepository = specialDatesRepository;
        this.slotRepository = slotRepository;
        this.courtRepository = courtRepository;
        this.bookSlotRepository = bookSlotRepository;
        this.userRepository = userRepository;
    }

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final CourtRepository courtRepository;
    private final BookSlotRepository bookSlotRepository;

    @GetMapping("/bookSlot")
    public String getSlots(Model model, @RequestParam(name = "date", defaultValue = "") String date, HttpSession session){
        if (date.equals("")){
            date = LocalDate.now().format(dtf);
        }
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );
        SpecialDates specialDate = specialDatesRepository.findByDate(date);
        String dayType = "REGL";
        System.out.println(dateModified.getDayOfWeek());
        if (!(specialDate == null)){
            dayType = specialDate.getDayType();
        } else if (dateModified.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||dateModified.getDayOfWeek().equals(DayOfWeek.SUNDAY) ) {
            dayType = "WEND";
        }
        List<Slot> slots = slotRepository.findByDayTypeOrderByStartHourAsc(dayType);
        List<Map> slotListed = new ArrayList<Map>();
        for (Slot slot : slots){
            Map slotMap = slot.getDict();
            int sizeList = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "pending", slot.getSlotCode()).size();
            List<BookSlot> sizeListAcc = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "accepted", slot.getSlotCode());

            String status = "Open";
            String statusColor= "green";
            String gameMode = "";
            if(sizeList != 0){
                statusColor="yellow";
            }

            slotMap.putIfAbsent("singlesNo", bookSlotRepository.findByGameDateAndGameModeAndSlotCode(dateModified, "Singles", slot.getSlotCode()).size());
            slotMap.putIfAbsent("doublesNo", bookSlotRepository.findByGameDateAndGameModeAndSlotCode(dateModified, "Doubles", slot.getSlotCode()).size());



            int approvedSlots = 0;
            int all = 0;
            if (sizeListAcc.size() != 0){
                gameMode = sizeListAcc.get(0).getGameMode();
                if(sizeListAcc.get(0).getGameMode().equals("Singles")){
                    all=2;
                    approvedSlots = sizeListAcc.size();
                    if (sizeListAcc.size() >= 2){
                        status="Already slot is booked";
                        statusColor= "red";
                    }else{
                        if(sizeListAcc.size() != 0){
                            status = "Slot is assigned for game mode- "+gameMode + " only "+(all - approvedSlots)+ "slots available";
                            statusColor="yellow";
                        }
                    }
                }else if(sizeListAcc.get(0).getGameMode().equals("Doubles")){
                    all=4;
                    approvedSlots = sizeListAcc.size();
                    if (sizeListAcc.size() >= 4 ){
                        status="Already slot is booked";
                        statusColor= "red";

                    }
                    }else{
                    if(sizeListAcc.size() != 0){
                        status = "Slot is assigned for game mode- "+gameMode + " only "+(all - approvedSlots)+ "slots available";
                        statusColor="yellow";
                    }
                    }
                }



            Map user = (Map) session.getAttribute("user");
            BookSlot bookSlot1 = bookSlotRepository.findByGameDateAndSlotCodeAndBookedBy(dateModified,slot.getSlotCode(),user.get("mobileNo").toString()  );
            if (bookSlot1 != null){
                statusColor = "blue";
                status = "You have already placed a request";

            }

            slotMap.putIfAbsent("status",status);
            slotMap.putIfAbsent("statusColor",statusColor);
            slotListed.add(slotMap);
        }
        System.out.println(slots);
        model.addAttribute("slots", slotListed);
        model.addAttribute("date", date);
        model.addAttribute("courts", courtRepository.findAll());
        return "customer/bookSlotUser";
    }
//    @GetMapping("/bookSlot")
//    public String getSlots(Model model, @RequestParam(name = "date", defaultValue = "") String date, HttpSession session){
//        if (date.equals("")){
//            date = LocalDate.now().format(dtf);
//        }
//        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );
//        SpecialDates specialDate = specialDatesRepository.findByDate(date);
//        String dayType = "REGL";
//        System.out.println(dateModified.getDayOfWeek());
//        if (!(specialDate == null)){
//            dayType = specialDate.getDayType();
//        } else if (dateModified.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||dateModified.getDayOfWeek().equals(DayOfWeek.SUNDAY) ) {
//            dayType = "WEND";
//        }
//        List<Slot> slots = slotRepository.findByDayTypeOrderByStartHourAsc(dayType);
//        List<Map<String, java.io.Serializable>> slotListed = new ArrayList<Map<String, java.io.Serializable>>();
//        for (Slot slot : slots){
//            Map<String, java.io.Serializable> slotMap = slot.getDict();
//            String statusColor = "green";
//            String status = "Open";
//            List<BookSlot> bookSlots = bookSlotRepository.findBySlotCodeAndGameDate(slot.getSlotCode(), dateModified);
//            if (!bookSlots.isEmpty()){
//                statusColor = "yellow";
//                status = "Other "+ bookSlots.size()+" users also requested";
//
//                Map user = (Map) session.getAttribute("user");
//                BookSlot bookSlot1 = bookSlotRepository.findByGameDateAndSlotCodeAndBookedBy(dateModified,slot.getSlotCode(),user.get("mobileNo").toString()  );
//                if (bookSlot1 != null){
//                    statusColor = "blue";
//                    status = "You have already placed a request";
//                }
//            }
//
//            slotMap.putIfAbsent("status",status);
//            slotMap.putIfAbsent("statusColor",statusColor);
//            slotListed.add(slotMap);
//        }
//        System.out.println(slots);
//        model.addAttribute("slots", slotListed);
//        model.addAttribute("date", date);
//        model.addAttribute("courts", courtRepository.findAll());
//        return "customer/bookSlotUser";
//    }

    @RequestMapping("/myRequests")
    public String myRequests(Model model, HttpSession session){
        Map user = (Map) session.getAttribute("user");
        List<BookSlot> bookSlots = bookSlotRepository.findByBookedByOrderByGameDateDesc(user.get("mobileNo").toString());
        Iterable<Court> courts = courtRepository.findAll();
        List<Map> bookslotsMap = new ArrayList<>();
        for (BookSlot bookSlot: bookSlots){
            Map bookslotMap = bookSlot.toMap();
            Court court = courtRepository.findByCode(bookSlot.getCourtCode());
            String statusColor = "secondary";
            if(bookSlot.getConfirmStatus().equals("accepted")){
                statusColor = "success";
            } else if (bookSlot.getConfirmStatus().equals("rejected")) {
                statusColor = "danger";
            }
            bookslotMap.put("cardColor", statusColor);
            bookslotMap.put("court" , court);
            bookslotMap.put("gameDateMod",bookSlot.getGameDate().getDayOfMonth() +" "+ bookSlot.getGameDate().getMonth());
            bookslotMap.put("slot", slotRepository.findBySlotCode(bookSlot.getSlotCode()));
            bookslotsMap.add(bookslotMap);
        }
        System.out.println(bookslotsMap);
        model.addAttribute("requests", bookslotsMap);
        return "customer/myRequests";
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final UserRepository userRepository;

    @RequestMapping("/acceptSlots")
    public String acceptSlots(Model model, HttpSession session){
        Map user = (Map) session.getAttribute("user");

        model.addAttribute("courts", courtRepository.findAll());
        return "admin/acceptSlotsCalendar";
    }



}
