package com.CourtReserve.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLog {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private String mobileNo;
    private String ipAddress;
    private LocalDateTime loginTime;
    private String sessionId;
    private String status;
}
