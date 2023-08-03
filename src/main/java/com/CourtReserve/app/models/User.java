package com.CourtReserve.app.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userType; //User Type  - Admin / Member / Non member / VIP / TopMgmt
    private String mobileNo;
    private String email;
    private String userName;
    private String password;
    private String location;
    private String country; // Countries -
    private String referral = "";
    private LocalDateTime lastLogin;

    // Constructors, getters, and setters

    public Map getDict(){
        Map dict = new HashMap<>();
        dict.putIfAbsent("id",id);
        dict.putIfAbsent("userType", userType);
        dict.putIfAbsent("userName", userName);
        dict.putIfAbsent("mobileNo", mobileNo);
        dict.putIfAbsent("email", email);
        dict.putIfAbsent("location", location);
        dict.putIfAbsent("country", country);

        return dict;
    }
}
