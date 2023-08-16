package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.User;
import com.CourtReserve.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class entryRestController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/public/sendOtp")
    public ResponseEntity<Map> sendOtp(@RequestParam Map<String, String> body, HttpSession session) {
        System.out.println("body"+body);
        String mobileNumber = body.get("mobileNo");
        User user1 =userRepository.findByMobileNo(mobileNumber);
        System.out.println("user1:"+user1);
        if (mobileNumber == "") {
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter  Valid MobileNo");
            System.out.println("pls enter MobileNo");

            return ResponseEntity.ok(response);
        }
        else if(mobileNumber.length()!=10){
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter 10 Digit MobileNo");
            System.out.println("pls enter  1o digit  MobileNo");
            return ResponseEntity.ok(response);
        }
        else if(user1==null){
            Map response = new HashMap();
            response.putIfAbsent("msg", user1.getMobileNo());
            System.out.println("Pls Enter Registered MobileNo");
            return ResponseEntity.ok(response);
        }

        else {

            int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
            System.out.println("otp:"+otp);
            session.setAttribute("otp", otp);


            Map response = new HashMap();
            response.putIfAbsent("msg",user1.getMobileNo());
            response.putIfAbsent("otp", otp);
            return ResponseEntity.ok(response);
        }

    }
    @PostMapping("/public/sendcOtp")
    public ResponseEntity<Map> sendcOtp(@RequestParam Map<String, String> body, HttpSession session) {
        System.out.println("body"+body);
        String mob = body.get("mobileNo");
        User user2 =userRepository.findByMobileNo(mob);
        System.out.println("user2:"+user2);
        if (mob == "") {
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter  Valid MobileNo");
            System.out.println("pls enter MobileNo");

            return ResponseEntity.ok(response);
        }
        else if(mob.length()!=10){
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter 10 Digit MobileNo");
            System.out.println("pls enter  1o digit  MobileNo");
            return ResponseEntity.ok(response);
        }
        else if(user2!=null){
            Map response = new HashMap();
            response.putIfAbsent("msg", user2.getMobileNo());
            System.out.println("Pls Enter UnRegistered MobileNo");
            return ResponseEntity.ok(response);
        }

        else  {

            int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
            System.out.println("otp:"+otp);
            session.setAttribute("otp", otp);


            Map response = new HashMap();
            response.putIfAbsent("msg", "sent Otp Successfully");
            response.putIfAbsent("otp", otp);
            return ResponseEntity.ok(response);
        }



    }
//    @PostMapping("/public/sendOldPassword")
//    public ResponseEntity<Map> sendOldPassword(@RequestParam Map<String, String> body, HttpSession session) {
//        System.out.println("body"+body);
//        String mob = body.get("mobileNo");
//        User user3 =userRepository.findByMobileNo(mob);
//        System.out.println("user2:"+user3);
//        if (mob == "") {
//            Map response = new HashMap();
//            response.putIfAbsent("msg", "Pls Enter  Valid MobileNo");
//            System.out.println("pls enter MobileNo");
//        }
//        else if(mob.length()!=10){
//            Map response = new HashMap();
//            response.putIfAbsent("msg", "Pls Enter 10 Digit MobileNo");
//            System.out.println("pls enter  1o digit  MobileNo");
//            return ResponseEntity.ok(response);
//        }
//        else if(user3==null){
//            Map response = new HashMap();
//            response.putIfAbsent("msg", user3.getMobileNo());
//            System.out.println("Pls Enter Registered MobileNo");
//            return ResponseEntity.ok(response);
//        }
//        else {
//
//            User user = userRepository.findByMobileNo(body.get("mobileNo"));
//            System.out.println("user:"+user);
//            String user4=user.getPassword();
//            session.setAttribute("user4", user4);
//
//
//            Map response = new HashMap();
//            response.putIfAbsent("msg",user3.getMobileNo());
//            response.putIfAbsent("user4", user4);
//            return ResponseEntity.ok(response);
//        }
  //  }



    @PostMapping("/public/checkOtp")
    public ResponseEntity<Map> checkOtp(@RequestParam Map<String, String> body, HttpSession session) {
        String mobileNumer = body.get("mobileNo");
        String otp = body.get("otp");
        System.out.println(otp);
        System.out.println(session.getAttribute("otp").getClass());

        Map response = new HashMap<>();
        if ( otp.toString().equals(session.getAttribute("otp").toString())){
            session.setAttribute("verified", "true");
            response.putIfAbsent("msg","Successfully verified");
            session.setAttribute("mobileNo", mobileNumer);

            System.out.println(response);
            return ResponseEntity.status(203).body(response);
        }
        response.putIfAbsent("msg","Retry");
        System.out.println(response);
        response.putIfAbsent("status",204);
        return ResponseEntity.status(204).body(response);
    }

}
