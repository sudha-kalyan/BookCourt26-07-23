package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.BookSlot;
import com.CourtReserve.app.repositories.BookSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/views")
public class ViewRestController {
    @Autowired
    private BookSlotRepository bookSlotRepository;
    @GetMapping("/slots")
    public List<BookSlot> getSlots(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request){
        List<BookSlot> list = Collections.emptyList();
        if(!body.isEmpty() && !StringUtils.isEmpty(body.get("bookedBy")) && !StringUtils.isEmpty(body.get("gameDate"))) {
            //LocalDate date = LocalDate.parse(body.get("gameDate"));
            System.out.println(LocalDate.parse(body.get("gameDate")));
//            list = bookSlotRepository.findByGameDateAndBookedBy( LocalDate.parse(body.get("gameDate")), body.get("bookedBy"));
            System.out.println(bookSlotRepository.findAll());
            // List<BookSlot> list = bookSlotRepository.findByBookedByAndGameDate(mobileNo1,date);
        }
        System.out.println("Slots"+list);
        return (List<BookSlot>) bookSlotRepository.findAll();
    }
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    @GetMapping(value = "/getSlotsPdf")
    public ResponseEntity getSlotsPdf(@RequestParam Map<String, String> body, Model model, HttpServletResponse response, HttpServletRequest request, HttpSession session ) throws IOException {
        System.out.println(body);
        List<BookSlot> list = Collections.emptyList();
        if(!body.isEmpty() && !StringUtils.isEmpty(body.get("bookedBy")) && !StringUtils.isEmpty(body.get("gameDate"))) {
            //LocalDate date = LocalDate.parse(body.get("gameDate"));
            System.out.println(LocalDate.parse(body.get("gameDate")));
            list = bookSlotRepository.findByGameDateAndBookedBy( LocalDate.parse(body.get("gameDate")), body.get("bookedBy"));
//            System.out.println(bookSlotRepository.findAll());
            // List<BookSlot> list = bookSlotRepository.findByBookedByAndGameDate(mobileNo1,date);
        }
        WebContext context = new WebContext(request, response, request.getServletContext());
        context.setVariable("list", list);
        String finalhtml = springTemplateEngine.process("htmlPdf/slotsViewPdf",context);
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        System.out.println(finalhtml);
        renderer.setDocumentFromString(finalhtml);
        renderer.layout();
        renderer.createPDF(ops, false);
        renderer.finishPDF();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=User"+ ".pdf").contentType(MediaType.APPLICATION_OCTET_STREAM).body(ops.toByteArray());


    }
}
