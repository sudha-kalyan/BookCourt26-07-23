package com.CourtReserve.app.controllers;

import com.CourtReserve.app.models.Referral;
import com.CourtReserve.app.models.User;
import com.CourtReserve.app.models.UserLog;
import com.CourtReserve.app.repositories.ReferralRepository;
import com.CourtReserve.app.repositories.UserLogRepository;
import com.CourtReserve.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class entryController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReferralRepository referralRepository;
    @Autowired
    UserLogRepository userLogRepository;
    @GetMapping("/public/register")
    public String showRegistrationForm(HttpSession session) {

        if (session.getAttribute("verified") != null ){
            if (session.getAttribute("verified").toString().equals( "true")){

            return "entryTemplates/registrationForm";}
        } else {
            return "entryTemplates/verification";
        }
        return "entryTemplates/verification";
    }

    @RequestMapping("/public/login")
    public String showLoginForm(HttpSession session){

        return "entryTemplates/login";
    }
    @PostMapping("/public/login")
    public String loginUser(HttpSession session, @RequestParam Map body, HttpServletResponse response,HttpSession request, Model model) throws UnknownHostException {
        List<String> messages = new ArrayList<>();
        System.out.println(body);
        User user = new User();
        UserLog userLog = new UserLog();
        String result="";
        UserLog usercheck= userLogRepository.findByMobileNoAndStatus(body.get("mobileNo").toString(),"active");
        if(usercheck==null) {
            System.out.println("if exe");
            user = userRepository.findByMobileNoAndPassword(body.get("mobileNo").toString(), body.get("password").toString());
            if (user != null) {
                session.setAttribute("loggedIn", "true");
                session.setAttribute("loggedMobile", user.getMobileNo());
                session.setAttribute("userType", user.getUserType());
                session.setAttribute("user", user.getDict());
                userLog.setUserId(user.getId());
                userLog.setMobileNo((String) body.get("mobileNo"));
                userLog.setIpAddress(String.valueOf(InetAddress.getByName("192.168.29.119")));
                userLog.setSessionId(session.getId());
                userLog.setStatus("active");
                userLogRepository.save(userLog);
                //messages.add("Successfully login");
                result = "redirect:/";
            } else {
                messages.add("Pls Enter Valid Credentials");
                result = "entryTemplates/login";
                System.out.println("user invalid credentials");
            }
        }else{
            messages.add("User Already LoggedIn!");
            System.out.println("user already loggedin");
            result= "redirect:entryTemplates/login";
        }
        model.addAttribute("messages", messages);
        return result;
    }
    @RequestMapping("/public/logout")
    public String loginUser(HttpSession session){
        UserLog userLog=new UserLog();
        System.out.println(session.getId());
         User user=new User();
        UserLog usercheck= userLogRepository.findBySessionIdAndStatus(session.getId(),"active");
        System.out.println("user Checking:"+usercheck);
        userLogRepository.delete(usercheck);
        session.setAttribute("loggedIn", "false");
        session.setAttribute("loggedMobile", null);
        session.setAttribute("userType", null);
        session.setAttribute("user", null);
        return "redirect:/";

    }
    @RequestMapping("/public/password")
    public String changePassword(){
        System.out.println("password");
        return "entryTemplates/password";
    }
    @PostMapping("/public/register")
    public String registerUser(@ModelAttribute User user, HttpSession session,Model model) {
        // Process the user registration
        System.out.println("user:"+user);
        List<String> messages = new ArrayList<>();
        String result="";
        if (user.getReferral().equals("")){
            user.setUserType("NonMember");
        }else{
            System.out.println(referralRepository.findAll());

            Referral referral = referralRepository.findByCode(user.getReferral().toString());

            user.setUserType(referral.getType());
            messages.add(" you Registered Successfully");

        }
        userRepository.save(user);
        session.setAttribute("verified" , null);
        session.setAttribute("mobileNo", null);
        model.addAttribute("messages",messages);

        // Redirect to a success page or perform other actions
        return "redirect:/"; // Name of the success page or URL
    }
    @PostMapping("/public/password")
    public String changePassword( HttpServletRequest request, Model model,@RequestParam Map<String, String> body) {
        List<String> messages = new ArrayList<>();
        System.out.println(body);
        String result="";
        User user = userRepository.findByMobileNo(body.get("mobileNo"));
        System.out.println("user:" + user);
        if(user==null){
            messages.add("Pls Enter Registered MobileNo");
            result = "entryTemplates/password";
        }
        else{
            System.out.println("user mob:" + user.getMobileNo());
            System.out.println("user mob:" + body.get("mobileNo"));
            user.setPassword(body.get("newPassword"));
            System.out.println(user);
            messages.add("Your Password Has Been Changed Successfully");
            result ="entryTemplates/password";
        }
        model.addAttribute("messages",messages);
        return result;
    }

    @RequestMapping("/public/pwd")
    public String changePwdHtmlForm(){
        System.out.println("password");
        return "entryTemplates/changePassword";
    }
    @PostMapping("/public/pwd")
    public String changePwdHtmlForm( HttpServletRequest request, Model model,@RequestParam String mobileNo,  @RequestParam String oldPassword, @RequestParam String newPassword) {
        System.out.println(mobileNo);
        System.out.println(oldPassword);
        System.out.println(newPassword);
//        System.out.println(con_password);
        User user = userRepository.findByPassword( oldPassword);

        if(user!=null)
        {
            user.setPassword(newPassword);
            userRepository.save(user);
        }
        return "entryTemplates/login";
    }

}
