package com.CourtReserve.app.controllers;


import com.CourtReserve.app.models.Notifies;
import com.CourtReserve.app.repositories.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class homeController {

    @Autowired
    NotifyRepository notifyRepository;
    @RequestMapping("/")
    public String home(HttpSession session, Model model){
        if (session.getAttribute("loggedIn").equals("true") ){
        Map user = (Map) session.getAttribute("user");
        if (!user.get("userType").equals("Admin")){
            List<Notifies> notifies = notifyRepository.findByUserAndStatusOrderByIdAsc(user.get("mobileNo").toString(), "pending");
            Collections.reverse(notifies);
            model.addAttribute("notifies", notifies);
        }}

        return "home" ;
    }



}
