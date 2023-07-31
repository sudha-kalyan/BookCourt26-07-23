package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class entryRestController {
    UserRepository userRepository;

    @PostMapping("/public/sendOtp")
    public ResponseEntity<Map> sendOtp(@RequestParam Map<String, String> body, HttpSession session) {
        System.out.println(body);
        String mobileNubmer = body.get("mobileNo");
        if (mobileNubmer == "") {
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter  Valid MobileNo");
            //System.out.println("Pls enter MobileNo");

            return ResponseEntity.ok(response);
        }
        else if(mobileNubmer.length()!=10){
            Map response = new HashMap();
            response.putIfAbsent("msg", "Pls Enter 10 Digit MobileNo");
            //System.out.println("Pls Enter  10 Digit  MobileNo");
            return ResponseEntity.ok(response);
        }
        else {

            int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
           // System.out.println("otp:"+otp);
            session.setAttribute("otp", otp);


            Map response = new HashMap();
            response.putIfAbsent("msg", "Sent Otp Successful");
            response.putIfAbsent("otp", otp);
            return ResponseEntity.ok(response);
        }

    }
    @PostMapping("/public/checkOtp")
    public ResponseEntity<Map> checkOtp(@RequestParam Map<String, String> body, HttpSession session) {
        String mobileNumer = body.get("mobileNo");
        String otp = body.get("otp");
        System.out.println(otp);
        System.out.println(session.getAttribute("otp").getClass());

        Map response = new HashMap<>();
        if ( otp.toString().equals(session.getAttribute("otp").toString())){
            session.setAttribute("verified", "true");
            response.putIfAbsent("msg","Successful ");
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
