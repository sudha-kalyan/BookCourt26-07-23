package com.CourtReserve.app.controllers;

import csv.DownloadCsvReport;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;


import ch.qos.logback.classic.spi.LoggingEventVO;
import com.CourtReserve.app.models.*;
import com.CourtReserve.app.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;
    @Autowired
    private UserLogRepository userLogRepository;
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
            System.out.println(57);
            System.out.println(date);
            System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));



            if (date.equals(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))){
                Integer hourNow = LocalDateTime.now().getHour();
                System.out.println("hour:"+hourNow);
                System.out.println("date:"+date);
                if(Integer.parseInt(slot.getStartHour().toString().split(":")[0] )<= hourNow+1){
                    continue;
                }
            }

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
                status = bookSlot1.getConfirmStatus()+"-"+bookSlot1.getGameMode();
                LocalDate date1= bookSlot1.getGameDate();
                System.out.println("date:"+date);
                DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String DateinText = date1.format(formatters);
                System.out.println("text:"+DateinText);
//                if(DateinText>= LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString()){
//                    continue;
//                }
            }

            slotMap.putIfAbsent("status",status);
            slotMap.putIfAbsent("statusColor",statusColor);
            slotListed.add(slotMap);
        }

        Map user = (Map) session.getAttribute("user");
       List< BookSlot> bookSlot2=bookSlotRepository.findByBookedBy(user.get("mobileNo").toString()  );
        System.out.println("bookSlot2:"+bookSlot2);
        System.out.println("bookSlot2:"+bookSlot2);
        System.out.println(slots);
        model.addAttribute("slots", slotListed);
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
    public String myRequests(Model model, HttpSession session) throws ParseException {
        Map user = (Map) session.getAttribute("user");
        List<BookSlot> bookSlots = bookSlotRepository.findByBookedByOrderByGameDateDesc(user.get("mobileNo").toString());
        Iterable<Court> courts = courtRepository.findAll();
        List<Map> bookslotsMap = new ArrayList<>();
        for (BookSlot bookSlot: bookSlots) {
            Map bookslotMap = bookSlot.toMap();
            Court court = courtRepository.findByCode(bookSlot.getCourtCode());
            String statusColor = "secondary";
            if (bookSlot.getConfirmStatus().equals("accepted")) {
                statusColor = "success";
            } else if (bookSlot.getConfirmStatus().equals("rejected")) {
                statusColor = "danger";
            }
            LocalDate date = bookSlot.getGameDate();
            System.out.println("date:" + date);
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String DateinText = date.format(formatters);
            System.out.println("text:" + DateinText);
           String date3=LocalDate.now().format(formatters);
//           Date d =new Date();
//            System.out.println(d);
         //   SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
         //   System.out.println(sdf.parse(String.valueOf(date)).before(sdf.parse(date3)));
           // if (DateinText.equals(date3)) { // it is working but not our achievement
                bookslotMap.put("cardColor", statusColor);
                bookslotMap.put("court", court);
                bookslotMap.put("gameDateMod", DateinText);
                bookslotMap.put("slot", slotRepository.findBySlotCode(bookSlot.getSlotCode()));
                bookslotsMap.add(bookslotMap);
            }
      // }
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
    @GetMapping("/slotViewDataUser")
    public String slotViewOrderForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn").equals("true") ){
            UserLog users = userLogRepository.findBySessionIdAndStatus(session.getId(), "active");
            System.out.println(users.getMobileNo());
            User user = (userRepository.findByMobileNo(users.getMobileNo()));
            System.out.println(user.getMobileNo());
            Slot s= new Slot();
            model.addAttribute("user", user);
            model.addAttribute("slot", s);
            return "customer/viewSlotsUser";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";

    }
    @PostMapping("/slotViewDataUser")
    public @ResponseBody String slotViewOrder(HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
        System.out.println("88888888888");
        //System.out.println(body);
        String mobileNo=request.getParameter("mobileNo");
        String gameMode=request.getParameter("gameMode");
        String status=request.getParameter("status");
        LocalDate fromDate= LocalDate.parse(request.getParameter("fromDate"));
        LocalDate toDate= LocalDate.parse(request.getParameter("toDate"));
        List list=bookSlotRepository.findByGameDateBetweenAndBookedByOrderByIdAsc(fromDate,toDate,mobileNo);
       // List<BookSlot> list = bookSlotRepository.findByBookedByAndGameDateBetweenAndGameModeAndConfirmStatus(mobileNo,fromDate,toDate,gameMode,status);
        System.out.println("list45:"+list);
        System.out.println("list46:"+list.size());

        if(!status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusOrderByIdAsc(fromDate,toDate,mobileNo,status);
        } else if(!status.equals("all") && !mobileNo.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndBookedByOrderByIdAsc(fromDate,toDate,status,mobileNo);
        }



        ObjectMapper mapper = mapperBuilder.build();
        String  output = mapper.writeValueAsString(list);

        System.out.println("Excel Size -- " + list.size());
        //  model.addAttribute("list", list);
        return output;

    }

    @Autowired
    SpringTemplateEngine springTemplateEngine;
    @GetMapping("/slotPdfDataMember")
    public ResponseEntity slotViewPdfOrder(Model model, HttpServletResponse response, HttpServletRequest request) {
        String mobileNo=request.getParameter("mobileNo");
        String gameMode=request.getParameter("gameMode");
        String status=request.getParameter("status");
        LocalDate fromDate= LocalDate.parse(request.getParameter("fromDate"));
        LocalDate toDate= LocalDate.parse(request.getParameter("toDate"));
        List list=bookSlotRepository.findByGameDateBetweenAndBookedByOrderByIdAsc(fromDate,toDate,mobileNo);
        if(!status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusOrderByIdAsc(fromDate,toDate,mobileNo,status);
        } else if(!status.equals("all") && !mobileNo.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndBookedByOrderByIdAsc(fromDate,toDate,status,mobileNo);
        }

        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("list", list);
        String finalhtml = springTemplateEngine.process("customer/slotpdfWeb",context);
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        System.out.println(finalhtml);
        renderer.setDocumentFromString(finalhtml);
        renderer.layout();
        renderer.createPDF(ops, false);
        renderer.finishPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fromDate.toString()+"-"+toDate.toString()+".pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());

    }

    @GetMapping("/slotExcelDataMember")
    public ResponseEntity slotViewOrder1(HttpSession session,@RequestParam Map<String, String> body,Model model,HttpServletResponse response, HttpServletRequest request) throws Exception {

        String mobileNo=body.get("mobileNo");
        String gameMode=body.get("gameMode");
        String status=body.get("status");
        LocalDate fromDate= LocalDate.parse(body.get("fromDate"));
        LocalDate toDate= LocalDate.parse(body.get("toDate"));
        List<BookSlot> list=bookSlotRepository.findByGameDateBetweenAndBookedByOrderByIdAsc(fromDate,toDate,mobileNo);
        if(!status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusOrderByIdAsc(fromDate,toDate,mobileNo,status);
        } else if(!status.equals("all") && !mobileNo.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndBookedByOrderByIdAsc(fromDate,toDate,status,mobileNo);
        }
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(body.get("mobileNo").toString());
        HSSFRow Header = sheet.createRow(0);
        int headercellStart = 0;
        String header[] ={"gameDate","gameName","courtCode","startTime","endTime","slotCode","gameMode","confirmStatus","bookedBy","bookTime","approvedBy","RemarksByUser","RemarksByAdmin"};
        DownloadCsvReport.getCsvReportDownload(response, header, list, "slot_data.csv");
//        for (String i : header) {
//            HSSFCellStyle style = workbook.createCellStyle();
//            style.setFillBackgroundColor(IndexedColors.BRIGHT_GREEN.getIndex());
//            HSSFCell cell = Header.createCell(headercellStart);
//            cell.setCellValue(i);
//            cell.setCellStyle(style);
//            headercellStart = headercellStart + 1;
//        }
//        int rowVal = 1;
//        for (BookSlot order : list) {
//            System.out.println("order:"+order);
//            HSSFRow row = sheet.createRow(rowVal);
//            int cellval = 0;
//            User user1 = userRepository.findByMobileNo(body.get("mobileNo"));
//            for (String i : order.getListValues(user1.getMobileNo())) {
//                System.out.println("Hi:"+i);
//                HSSFCell cell = row.createCell(cellval);
//                cellval= cellval+ 1;
//
//                if (cellval == header.length-1 ) {
//                    cell.setCellValue(i);
//                }
//                else{
//                    cell.setCellValue(i);
//                }
//            }
//
//            rowVal= rowVal+1;
//        }
//        try {
//
//            ByteArrayOutputStream ops = new ByteArrayOutputStream();
//            workbook.write(ops);
//            workbook.close();
//
//
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fromDate.toString()+"-"+toDate.toString()+".xls").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());
//        }catch (Exception e){
//
//        }


        return (ResponseEntity) ResponseEntity.status(203);

    }




}
