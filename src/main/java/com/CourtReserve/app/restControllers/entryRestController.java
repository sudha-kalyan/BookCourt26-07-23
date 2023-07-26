package com.CourtReserve.app.restControllers;

import com.CourtReserve.app.models.User;
import com.CourtReserve.app.repositories.UserRepository;
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
    UserRepository userRepository;

    @PostMapping("/public/sendOtp")
    public ResponseEntity<Map> sendOtp(@RequestParam Map<String, String> body, HttpSession session) {
        System.out.println(body);
        String mobileNubmer = body.get("mobileNo");
        int otp = ThreadLocalRandom.current().nextInt(100000, 1000000);
        Map response = null;
        if (mobileNubmer == "") {
            response.putIfAbsent("msg", "Please Enter MobileNo");
            response.putIfAbsent("otp", otp);
            return ResponseEntity.ok(response);
        } else {
            session.setAttribute("otp", otp);
            response = null;
            response = new HashMap();
            response.putIfAbsent("msg", "sent Otp Successfully");
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
            response.putIfAbsent("msg","Successfully ");
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
