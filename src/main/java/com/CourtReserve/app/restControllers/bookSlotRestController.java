package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.*;
import com.CourtReserve.app.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class bookSlotRestController {
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final BookSlotRepository bookSlotRepository;
    private final SpecialDatesRepository specialDatesRepository;
    private final NotifyRepository notifyRepository;

    public bookSlotRestController(SlotRepository slotRepository,
                                  UserRepository userRepository,
                                  BookSlotRepository bookSlotRepository,
                                  SpecialDatesRepository specialDatesRepository,
                                  NotifyRepository notifyRepository) {
        this.slotRepository = slotRepository;
        this.userRepository = userRepository;
        this.bookSlotRepository = bookSlotRepository;
        this.specialDatesRepository = specialDatesRepository;
        this.notifyRepository = notifyRepository;
    }

    @PostMapping("/requestSlot")
    public ResponseEntity requestSlotPost(@RequestParam Map map){
        System.out.println(map);
        Map response = new HashMap();
        String date = map.get("date").toString();
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );

        Slot slot = slotRepository.findBySlotCode(map.get("slotCode").toString());
        System.out.println(slot);
        User user = userRepository.findByMobileNo(map.get("userMobile").toString());
        BookSlot bookSlot = new BookSlot();
        int series = 1;
        Iterable<BookSlot> bookSlotsIterator = bookSlotRepository.findAll();
        List<BookSlot> bookSlotsList = new ArrayList<>();
        for (BookSlot getSeries: bookSlotsIterator ){
            bookSlotsList.add(getSeries);
        }
        if (!bookSlotsList.isEmpty()){
            String bookingNo = bookSlotsList.get(bookSlotsList.size()-1).getBookingNo();
            series = Integer.parseInt(bookingNo.split("-")[bookingNo.split("-").length - 1]) + 1;
        }
        bookSlot.setSlotCode(slot.getSlotCode());
        bookSlot.setCourtCode(slot.getCourtCode());
        bookSlot.setBookingDate(LocalDate.now());
        bookSlot.setGameDate(dateModified);
        bookSlot.setBookingDate(dateModified);
        bookSlot.setBookTime(LocalDateTime.now());
        bookSlot.setBookedBy(user.getMobileNo());
        bookSlot.setConfirmStatus("pending");
        bookSlot.setRefNo(map.get("referredBy").toString());
        bookSlot.setGameMode(map.get("gameMode").toString());



        bookSlot.setRemarksByUser(map.get("remarks").toString());
        String bkNo= dateModified.format(DateTimeFormatter.ofPattern("ddMMyyyy"))+ "-"+slot.getStartHour() +"-"+series;
        bookSlot.setBookingNo(bkNo);
        bookSlotRepository.save(bookSlot);

        response.putIfAbsent("msg", "success");
        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/approveSlot")
    public ResponseEntity approveSlot(@RequestParam Map map, HttpSession session){
        System.out.println(map);
        Map response = new HashMap();
        Optional<BookSlot> bookSlotResp = bookSlotRepository.findById(Integer.parseInt(map.get("id").toString()));
        BookSlot bookSlot = bookSlotResp.get();
        System.out.println(bookSlot);
        List<BookSlot> bookSlots = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCodeAndGameMode(bookSlot.getGameDate(),"accepted", bookSlot.getSlotCode(), bookSlot.getGameMode());
        System.out.println(bookSlots);
        System.out.println(bookSlots.size());

                if(bookSlot.getGameMode().equals("Singles")){
                    System.out.println(bookSlots.size() >= 2);
                    if (bookSlots.size() >= 2){

                        response.putIfAbsent("msg", "already slot allotted to maximum number allowed per the game mode type-"+ bookSlots.get(0).getGameMode());
                        response.putIfAbsent("status", 203);
                        return ResponseEntity.ok(response);
                    }
                }else if(bookSlot.getGameMode().equals("Doubles")){
                    if (bookSlots.size() >= 4){
                        response.putIfAbsent("msg", "already slot allotted to maximum number allowed per the game mode type-"+ bookSlots.get(0).getGameMode());
                        response.putIfAbsent("status", 203);
                        return ResponseEntity.ok(response);
                    }
                }

                if(bookSlots.size()==0 && bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(bookSlot.getGameDate(),"accepted", bookSlot.getSlotCode()).size() !=0){
                    response.putIfAbsent("msg", "already slot allotted to game mode-"+ bookSlot.getGameMode());
                    response.putIfAbsent("status", 203);
                    return ResponseEntity.ok(response);
                }



        Map user = (Map) session.getAttribute("user");
        User user1 = userRepository.findByMobileNo(bookSlot.getBookedBy());

            bookSlot.setApprovedBy(user.get("userName").toString());
            bookSlot.setApprovedTime(LocalDateTime.now());
            bookSlot.setConfirmStatus("accepted");
            bookSlot.setRemarksByAdmin(map.get("remarks").toString());
            if (map.get("autoReject").equals("true")) {
                bookSlots = bookSlotRepository.findByGameDateAndSlotCode(bookSlot.getGameDate(), bookSlot.getSlotCode());
                for (BookSlot bookSlot1 : bookSlots) {
                    if (bookSlot1 != bookSlot) {
                        bookSlot1.setConfirmStatus("rejected");
                        bookSlot1.setApprovedBy(user.get("userName").toString());
                    }
                    response.put("msg", "successfully accepted request of " + user1.getUserName() + " a " + user1.getUserType() + " to use court " + bookSlot.getCourtCode() + " of slotCode:  "+ bookSlot.getSlotCode() + " while rejecting all others;" );
                }
            }
        response.putIfAbsent("msg", "successfully accepted request of " + user1.getUserName() + " a " + user1.getUserType() + " to use court " + bookSlot.getCourtCode() + " of slotCode:  "+ bookSlot.getSlotCode());



        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/changeGameMode")
    public ResponseEntity<Map> changeGameMode(@RequestParam Map map){
        Optional<BookSlot> bookSlotResp = bookSlotRepository.findById(Integer.parseInt(map.get("id").toString()));
        BookSlot bookSlot = bookSlotResp.get();
        Optional<Notifies> notifiesOptional = notifyRepository.findById(Integer.parseInt(map.get("selfId").toString()));
        Notifies notifies = notifiesOptional.get();
        if(map.get("status").equals("agreed")){
            notifies.setStatus("agreed");
            bookSlot.setConfirmStatus("notification agreed");
        }else{
            notifies.setStatus("diagreed");
            bookSlot.setConfirmStatus("notification disagreed");

        }
        System.out.println(bookSlot.getConfirmStatus());
        System.out.println(notifies.getStatus());
        Map response = new HashMap();
        response.putIfAbsent("msg", "successfully updated your action");
        response.putIfAbsent("status", 202);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/notifySlot")
    public ResponseEntity notifySlot(@RequestParam Map map, HttpSession session){
        System.out.println(map);
        Map response = new HashMap();
        Optional<BookSlot> bookSlotResp = bookSlotRepository.findById(Integer.parseInt(map.get("id").toString()));
        BookSlot bookSlot = bookSlotResp.get();
        String gameMode= bookSlot.getGameMode();
        if (gameMode.equals("Singles")){
            gameMode = "doubles";
        }else if (gameMode.equals("Doubles")){
            gameMode = "singles";

        }
        User user1 = userRepository.findByMobileNo(bookSlot.getBookedBy());
        Slot slot = slotRepository.findBySlotCode(bookSlot.getSlotCode());
        String msg = "Can you change your game mode to " + gameMode + "  as there is an opening for this game mode at your chosen slot on " + bookSlot.getGameDate() + " at " + slot.getStartHour() + " to " + slot.getEndHour();

        Notifies notifies = new Notifies();
        notifies.setMsg(msg);
        notifies.setUser(user1.getMobileNo());
        notifies.setStatus("pending");
        notifies.setBookSlotId(bookSlot.getId());

        bookSlot.setConfirmStatus("notified");


        Map user = (Map) session.getAttribute("user");

        notifyRepository.save(notifies);

        response.putIfAbsent("msg", "successfully notified user " + user1.getUserName() + " a " + user1.getUserType() );



        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/rejectSlot")
    public ResponseEntity rejectSlot(@RequestParam Map map, HttpSession session){
        System.out.println(map);
        Map response = new HashMap();
        Optional<BookSlot> bookSlotResp = bookSlotRepository.findById(Integer.parseInt(map.get("id").toString()));
        BookSlot bookSlot = bookSlotResp.get();
        System.out.println(bookSlot);


        Map user = (Map) session.getAttribute("user");
        bookSlot.setApprovedBy(user.get("userName").toString());
        bookSlot.setApprovedTime(LocalDateTime.now());
        bookSlot.setConfirmStatus("rejected");
        bookSlot.setRemarksByAdmin(map.get("remarks").toString());




        User user1 = userRepository.findByMobileNo(bookSlot.getBookedBy());

        response.putIfAbsent("msg", "successfully rejected request of " + user1.getUserName() + " a " + user1.getUserType() + " to use court " + bookSlot.getCourtCode() + " of slotCode:  "+ bookSlot.getSlotCode() );
        response.putIfAbsent("status", 202);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/getMonthDays")
    public ResponseEntity getMonthDays(@RequestParam Map map, HttpSession session){
        String month = map.get("month").toString();
        String year = map.get("year").toString();
        System.out.println(map);
        Map response = new HashMap();
        Iterable<BookSlot> bookSlots = bookSlotRepository.findAll();
        if (month.length() == 1){
            month = "0"+month;
        }
        Map datesMap = new HashMap<>();
        for (BookSlot bookSlot: bookSlots){
            String date= bookSlot.getGameDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println(date);
            if (!datesMap.containsKey(date ) && date.split("-")[1].equals(month) && date.split("-")[2].equals(year) ){
                int Size = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "pending").size();
                int Size1 = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "notified").size();
                int Size2 = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "notification agreed").size();
                int Size3 = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "notification disagreed").size();
                int Sizeaccpt = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "accepted").size();
                int SizeReject = bookSlotRepository.findByGameDateAndConfirmStatus(bookSlot.getGameDate(), "rejected").size();

                String statusColor= "white";
                if(Size != 0||Size1 != 0||Size2 != 0|| Size3 != 0){
                    statusColor="yellow";
                }else if (Sizeaccpt != 0){
                    statusColor="green";
                }else if (SizeReject != 0){
                    statusColor="red";
                }
                datesMap.putIfAbsent(Integer.parseInt(date.split("-")[0]),new String[]{String.valueOf(Size), statusColor} );
            }
        }

        response.put("datesMap", datesMap);
        response.put("defaultColor", "white");

        System.out.println("1234 "+datesMap);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/getSlots")
    public ResponseEntity getSlots2(@RequestParam(name = "date", defaultValue = "") String date, HttpSession session){
        System.out.println(date);
        Map response = new HashMap();
        if (date.equals("")){
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
        List<Map> slotsList = new ArrayList<>();
        for(Slot slot: slots){
            Map slotMap = slot.getDict();
            int sizeList = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "pending", slot.getSlotCode()).size();
            int sizeListNotified = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "notified", slot.getSlotCode()).size();
            int sizeListNotified2 = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "notification agreed", slot.getSlotCode()).size();
            int sizeListNotified3 = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "notification disagreed", slot.getSlotCode()).size();
            List<BookSlot> sizeListAcc = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "accepted", slot.getSlotCode());
            int sizeListRej = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "rejected", slot.getSlotCode()).size();


            String statusColor= "white";
            if(sizeList != 0 || sizeListNotified !=0 || sizeListNotified2 !=0 || sizeListNotified3 !=0){
                statusColor="yellow";
            }

            else if(sizeListRej != 0){
                statusColor="red";
            }

            int approvedSlots = 0;
            int all = 0;
            if (sizeListAcc.size() != 0){
                if(sizeListAcc.get(0).getGameMode().equals("Singles")){
                    all=2;
                    approvedSlots = sizeListAcc.size();
                    if (sizeListAcc.size() >= 2){

                        statusColor= "green";
                    }else{
                        if(sizeList != 0){
                            statusColor="yellow";
                        }else if(sizeListAcc.size() !=0){
                            statusColor="green";
                        }

                        else if(sizeListRej != 0){
                            statusColor="red";
                        }
                    }
                }else if(sizeListAcc.get(0).getGameMode().equals("Doubles")){
                    all=4;
                    approvedSlots = sizeListAcc.size();
                    if (sizeListAcc.size() >= 4 ){

                        statusColor= "green";
                    }else{
                        if(sizeList != 0){
                            statusColor="yellow";
                        }else if(sizeListAcc.size() !=0){
                            statusColor="green";
                        }
                        else if(sizeListRej != 0){
                            statusColor="red";
                        }
                    }
                }

            }


            if (!statusColor.equals("white")) {
                slotMap.put("statusColor", statusColor);
                slotMap.put("numberOfRequest", sizeList);
                slotMap.put("aprSlots", approvedSlots);
                slotMap.put("totalAvb", all);

                slotsList.add(slotMap);


            }
        }
        List<BookSlot> bookSlotsList = bookSlotRepository.findByGameDate(dateModified);
        List<Map> bookSlotMapList = new ArrayList<>();

        for (BookSlot bookSlot: bookSlotsList){

            Map bookSlotMap = bookSlot.toMap();
            bookSlotMap.put("user", userRepository.findByMobileNo(bookSlot.getBookedBy()).getDict());
            bookSlotMap.put("slotId", slotRepository.findBySlotCode(bookSlot.getSlotCode()).getId());
            bookSlotMapList.add(bookSlotMap);
        }
        System.out.println(slotsList);

        response.putIfAbsent("bookSlotMap", bookSlotMapList);
        response.putIfAbsent("slots", slotsList);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getRequests")
    public ResponseEntity getRequests(@RequestParam(name = "date", defaultValue = "") String date,@RequestParam(name = "slotCode") String slotCode, HttpSession session){
        System.out.println(date);
        LocalDate dateModified = LocalDate.of(Integer.parseInt(date.split("-")[0]),Integer.parseInt(date.split("-")[1]),Integer.parseInt(date.split("-")[2]) );

        Map response = new HashMap();
        List<BookSlot> bookSlotsList = bookSlotRepository.findByGameDateAndConfirmStatusAndSlotCode(dateModified, "pending", slotCode);
        List<Map> bookSlotMapList = new ArrayList<>();

        for (BookSlot bookSlot: bookSlotsList){

            Map bookSlotMap = bookSlot.toMap();
            bookSlotMap.put("user", userRepository.findByMobileNo(bookSlot.getBookedBy()));
            bookSlotMapList.add(bookSlotMap);
        }
        response.putIfAbsent("bookSlotMap", bookSlotMapList);
        return ResponseEntity.ok(response);
    }






}
//    private String branch;
//    private String courtCode;
//    private LocalDate bookingDate;
//    private LocalDate gameDate;
//    private String slotCode;
//    private String bookedBy;
//    private String remarksByUser;
//    private LocalDateTime bookTime;
//    private String confirmStatus;
//    private String remarksByAdmin;
//    private String approvedBy;
//    private LocalDateTime approvedTime;
//    private String bookingNo;
//    private String refNo;