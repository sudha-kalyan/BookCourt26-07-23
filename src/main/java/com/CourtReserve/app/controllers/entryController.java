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
                messages.add("Successfully login");
                result = "redirect:/";
            } else {
                messages.add("Pls Enter Valid Credentials");
                result = "entryTemplates/login";
                System.out.println("user invalid credentials");
            }
        }else{
            messages.add("User Already LoggedIn!");
            System.out.println("user already loggedin");
            result= "entryTemplates/login";
        }
        model.addAttribute("messages", messages);
        return result;
    }
    @RequestMapping("/public/logout")
    public String loginUser(HttpSession session){
        List<String> messages = new ArrayList<>();
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
        messages.add("Successfully logout");
        return "redirect:/";

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
    @GetMapping("/public/password")
    public String changePassword(){
        System.out.println("password");
        return "entryTemplates/password";
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
            result ="redirect:entryTemplates/login";
        }
        model.addAttribute("messages",messages);
        return result;
    }

//    @GetMapping("/public/pwd")
//    public String changePwdHtmlForm(){
//        System.out.println("password");
//        return "entryTemplates/changePassword";
//    }
//    @PostMapping("/public/pwd")
//    public String changePwdHtmlForm(HttpServletRequest request, Model model, @RequestParam String mobileNo, @RequestParam String oldPassword) {
//        System.out.println("333333333");
//        System.out.println(mobileNo);
//        System.out.println("22222222");
//        System.out.println(oldPassword);
//
////        System.out.println(con_password);
//        User user = userRepository.findByMobileNo(mobileNo);
//
//
//        user.setPassword(oldPassword);
//        userRepository.save(user);
//        System.out.println(user);
//
//        return "redirect:/";
//    }
//
//    @PostMapping("/getOldPassword")
//    public ResponseEntity<?> getCustValues(@RequestParam Map<String, String> body) {
//        Map<String, String> respBody = new HashMap<>();
//        System.out.println("111111111");
//        System.out.println(body);
//        User user = userRepository.findByMobileNo(body.get("mobileNo"));
//        System.out.println(user);
//        System.out.println(user.getPassword());
//        respBody.putIfAbsent("password", user.getPassword());
//        return ResponseEntity.ok(respBody);
//    }
//
//    @PostMapping("/passwordEdit")
//    public ResponseEntity<?> passwordEdit(@RequestParam Map<String, String> body) {
//        Map<String, String> respBody = new HashMap<>();
//        System.out.println("55555555555555");
//        System.out.println("body:"+body);
//        User user = userRepository.findByMobileNo(body.get("mobileNo"));
//        System.out.println(user);
//        System.out.println(user.getPassword());
//        user.setPassword(body.get("password"));
//        System.out.println(user);
//        userRepository.save(user);
//        return ResponseEntity.ok(respBody);
//    }
@RequestMapping("/public/pwd")
public String changePwdHtmlForm(Model model, HttpSession session){
    System.out.println("password");
    if (session.getAttribute("loggedIn").equals("true") ){
        UserLog users = userLogRepository.findBySessionIdAndStatus(session.getId(), "active");
        System.out.println(users.getMobileNo());
        User user = (userRepository.findByMobileNo(users.getMobileNo()));
        model.addAttribute("user", user);
        System.out.println(user.getMobileNo());
    return "entryTemplates/changePassword";
}
    List messages = new ArrayList<>();
    messages.add("Login First");
    model.addAttribute("messages", messages);
    return "redirect:/loginPage";

}
    @PostMapping("/public/pwd")
    public String changePwd( HttpServletRequest request, Model model,@RequestParam String mobileNo,  @RequestParam String oldPassword, @RequestParam String newPassword,@RequestParam String con_newpassword,HttpSession session) {
        List<String>messages=new ArrayList<>();

        System.out.println(mobileNo);
        System.out.println(oldPassword);
        System.out.println(newPassword);
        System.out.println(con_newpassword);

        UserLog users = userLogRepository.findBySessionIdAndStatus(session.getId(), "active");
        User user = userRepository.findByMobileNo(users.getMobileNo());
        System.out.println("user:"+user);
        System.out.println("user1:"+users);
        if(user.getPassword().equals(oldPassword)&&(newPassword.equals(con_newpassword)))
        {
            model.addAttribute("messages","Successfully Changed Ur Password" );
            user.setPassword(newPassword);
            userRepository.save(user);
        }
        else {

            model.addAttribute("messages","Pls Enter Valid Credentials" );
            System.out.println("Pls Enter Valid Credentials");
        }

//        if(!oldPassword.equals(user.getPassword()))
//        {
//            model.addAttribute("messages","Pls Enter Valid Password" );
//        }


        if(newPassword.equals(oldPassword)){
            model.addAttribute("messages","Pls Don't Use Old Password as New Password" );
            System.out.println("Pls Don't Use OldPassword as NewPassword" );
        }
       if(!newPassword.equals(con_newpassword)){
            model.addAttribute("messages","Pls Re-Enter the New Password to Confirm." );
            System.out.println("Pls Re-Enter the New Password to Confirm .." );
        }
//        if(!user.getPassword().equals(oldPassword)) {
//            if (!newPassword.equals(con_newpassword)) {
//                model.addAttribute("messages", "Pls Enter Valid All Passwords");
//            } else if(newPassword.equals(con_newpassword)){
//                model.addAttribute("messages", "Pls Enter Corret Old Password");
//            }
//        }
       if(!mobileNo.equals(users.getMobileNo()))  {
            model.addAttribute("messages","Pls Enter Registered MobileNo" );
            System.out.println("Pls Enter Registered MobileNo" );
        }
//        if(user.getPassword().equals(oldPassword)||(!newPassword.equals(con_newpassword))){
//            model.addAttribute("messages","I Love India" );
//        }

        return "entryTemplates/changePassword";
    }
//    @GetMapping("/public/managePro")
//    public String showManageProForm(HttpSession session ,Model model) {
//        if (session.getAttribute("loggedIn").equals("true") ){
//            UserLog users = userLogRepository.findBySessionIdAndStatus(session.getId(), "active");
//            System.out.println("hi"+users.getMobileNo());
//            User user1 = (userRepository.findByMobileNo(users.getMobileNo()));
//            System.out.println("users"+user1);
//            model.addAttribute("user", user1);
//        return "entryTemplates/manageProfile";
//    }
//        List messages = new ArrayList<>();
//        messages.add("Login First");
//        model.addAttribute("messages", messages);
//        return "redirect:/loginPage";
//
//    }
@RequestMapping("/public/managePro")
public String changePwdHtmlForm1(Model model, HttpSession session){

    if (session.getAttribute("loggedIn").equals("true") ){
        UserLog userLog = userLogRepository.findBySessionIdAndStatus(session.getId(), "active");
        System.out.println(userLog.getMobileNo());
        String mobNo=userLog.getMobileNo();
        User user1 = userRepository.findByMobileNo(mobNo);
        System.out.println(user1.getMobileNo());
        model.addAttribute("user", user1);
        System.out.println(user1.getMobileNo());
        return "entryTemplates/manageProfile";
    }
    List messages = new ArrayList<>();
    messages.add("Login First");
    model.addAttribute("messages", messages);
    return "redirect:/loginPage";

}
    @PostMapping("/public/managePro")
    public String manageProUser(@ModelAttribute User user, HttpSession session,Model model) {
        // Process the user registration
        System.out.println("user:"+user);
        List<String> messages = new ArrayList<>();
        User user1=userRepository.findByMobileNo(user.getMobileNo());
        user1.setUserName(user.getUserName());
        user1.setEmail(user.getEmail());
        user1.setLocation(user.getLocation());
        user1.setCountry(user.getCountry());
       // user1.setAddress1(user.getAddress1());
        // user1.setAddress2(user.getAddress2());
        // user1.setAddress3(user.getAddress3());
        // user1.setAddress4(user.getAddress4());
       // user1.setUserType(user.getUserType());
        if (user.getReferral().equals("")){
            user.setUserType("NonMember");
        }else{
            System.out.println(referralRepository.findAll());

            Referral referral = referralRepository.findByCode(user1.getReferral().toString());

            user1.setUserType(referral.getType());
            messages.add(" you Registered Successfully");

        }
        userRepository.save(user1);
        session.setAttribute("verified" , null);
        session.setAttribute("mobileNo", null);
        model.addAttribute("messages",messages);

        // Redirect to a success page or perform other actions
        return "redirect:/"; // Name of the success page or URL
    }
}
