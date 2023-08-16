package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.*;
import com.CourtReserve.app.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import csv.DownloadCsvReport;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private UserRepository userRepository;
    @Autowired
    private UserLogRepository userLogRepository;
    @Autowired
    private BookSlotRepository bookSlotRepository;

    @Autowired
    Jackson2ObjectMapperBuilder mapperBuilder;

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
            return "admin/viewSlots";
        }
        List messages = new ArrayList<>();
        messages.add("Login First");
        model.addAttribute("messages", messages);
        return "redirect:/loginPage";

    }
    @PostMapping("/slotViewData")
    public @ResponseBody String slotViewOrder(Model model,HttpServletResponse response,HttpServletRequest request) throws JsonProcessingException {


        System.out.println("88888888888");
        //System.out.println(body);
        String mobileNo=request.getParameter("mobileNo");
        String gameMode=request.getParameter("gameMode");
        String status=request.getParameter("status");
        LocalDate fromDate= LocalDate.parse(request.getParameter("fromDate"));
        LocalDate toDate= LocalDate.parse(request.getParameter("toDate"));
        List list=bookSlotRepository.findByGameDateBetweenOrderByIdAsc(fromDate,toDate);

        if(!status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,status,gameMode);
        } else if(!status.equals("all") && !mobileNo.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndBookedByOrderByIdAsc(fromDate,toDate,status,mobileNo);
        }
        if(list.size()==0){
            List messages=new ArrayList<>();
            messages.add("No Matches Found");
            model.addAttribute("messages",messages);
            System.out.println("No Matches Found");
        }







        ObjectMapper mapper = mapperBuilder.build();
        String  output = mapper.writeValueAsString(list);

        System.out.println("Excel Size -- " + list.size());
        //  model.addAttribute("list", list);
        return output;
    }

    @GetMapping("/slotExcelData")
    public ResponseEntity slotViewOrder1(HttpSession session,@RequestParam Map<String, String> body,Model model,HttpServletResponse response, HttpServletRequest request) throws Exception {

        String mobileNo=body.get("mobileNo");
        String gameMode=body.get("gameMode");
        String status=body.get("status");
        LocalDate fromDate= LocalDate.parse(body.get("fromDate"));
        LocalDate toDate= LocalDate.parse(body.get("toDate"));
        List<BookSlot> list=bookSlotRepository.findByGameDateBetweenOrderByIdAsc(fromDate,toDate);
        if(!status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,status,gameMode);
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
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    @GetMapping("/slotPdfData")
    public ResponseEntity slotViewPdfOrder(Model model,HttpServletResponse response,HttpServletRequest request) {
        String mobileNo=request.getParameter("mobileNo");
        String gameMode=request.getParameter("gameMode");
        String status=request.getParameter("status");
        LocalDate fromDate= LocalDate.parse(request.getParameter("fromDate"));
        LocalDate toDate= LocalDate.parse(request.getParameter("toDate"));
        List list=bookSlotRepository.findByGameDateBetweenOrderByIdAsc(fromDate,toDate);
        if(!status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,status,gameMode);
        } else if(status.equals("all") && !mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndBookedByAndGameModeOrderByIdAsc(fromDate,toDate,mobileNo,gameMode);
        } else if(!status.equals("all") && mobileNo.equals("all") && !gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndGameModeOrderByIdAsc(fromDate,toDate,status,gameMode);
        } else if(!status.equals("all") && !mobileNo.equals("all") && gameMode.equals("all")){
            list = bookSlotRepository.findByGameDateBetweenAndConfirmStatusAndBookedByOrderByIdAsc(fromDate,toDate,status,mobileNo);
        }
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("list", list);
        String finalhtml = springTemplateEngine.process("admin/slotpdfWeb",context);
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        System.out.println(finalhtml);
        renderer.setDocumentFromString(finalhtml);
        renderer.layout();
        renderer.createPDF(ops, false);
        renderer.finishPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fromDate.toString()+"-"+toDate.toString()+".pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());

    }


    @GetMapping("/slotExcelDataUser")
    public ResponseEntity slotViewUserOrder1(HttpSession session,@RequestParam Map<String, String> body,Model model,HttpServletResponse response, HttpServletRequest request) throws Exception {

        String mob= request.getSession().getAttribute("loggedMobile").toString();
        User user= userRepository.findByMobileNo(mob);
        List<BookSlot> list = bookSlotRepository.findByBookedBy(mob);
        System.out.println("list:"+list);
        System.out.println("Excel Size -- " + list.size());
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(request.getSession().getAttribute("loggedMobile").toString());
        HSSFRow Header = sheet.createRow(0);
        int headercellStart = 0;
        String header[] ={"gameDate","slotCode","gameMode","confirmStatus","bookedBy","approvedBy","RemarksByUser","RemarksByAdmin","bookTime"};
       // String header[]={"gameDate","slotCode","game"};
        DownloadCsvReport.getCsvReportDownload(response, header, list, "invoice_data.csv");

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
//            User user1 = userRepository.findByMobileNo(request.getSession().getAttribute("loggedMobile").toString());
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
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+user.getUserName().toString()+".xls").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());
//        }catch (Exception e){
//
//        }


        return (ResponseEntity) ResponseEntity.status(203);

    }
    @GetMapping("/slotPdfDataUser")
    public ResponseEntity slotViewPdfUserOrder(Model model,HttpServletResponse response,HttpServletRequest request) {
        System.out.println("@@@@@@@@@@@");
        User user= userRepository.findByMobileNo(request.getSession().getAttribute("loggedMobile").toString());
        System.out.println("user:"+user);
        System.out.println("user:"+user.getUserName());
        List<BookSlot> lists=bookSlotRepository.findByBookedBy(request.getSession().getAttribute("loggedMobile").toString());
        System.out.println("list:"+lists);
        System.out.println("Excel Size -- " + lists.size());
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("list", lists);
        String finalhtml = springTemplateEngine.process("admin/slotpdfWeb",context);
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        System.out.println(finalhtml);
        renderer.setDocumentFromString(finalhtml);
        renderer.layout();
        renderer.createPDF(ops, false);
        renderer.finishPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+user.getUserName()+ request.getSession().getAttribute("loggedMobile").toString()+".pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());

    }
}
