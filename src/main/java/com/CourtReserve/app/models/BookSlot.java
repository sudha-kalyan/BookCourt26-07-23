package com.CourtReserve.app.models;

import com.CourtReserve.app.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String branch;
    private String courtCode;
    private LocalDate bookingDate;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate gameDate;
    private String slotCode;
    private String bookedBy;
    private String remarksByUser;
    private LocalDateTime bookTime;
    private String confirmStatus;
    private String remarksByAdmin;
    private String approvedBy;
    private LocalDateTime approvedTime;
    private String bookingNo;
    private String refNo;
    private String startTime;
    private String endTime;
    private String gameName;

    private String gameMode="Singles";

    // Constructors, getters, and setters

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("branch", this.branch);
        map.put("courtCode", this.courtCode);
        map.put("bookingDate", this.bookingDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        map.put("gameDate", this.gameDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        map.put("slotCode", this.slotCode);
        map.put("bookedBy", this.bookedBy);
        map.put("remarksByUser", this.remarksByUser);
        map.put("bookTime", this.bookTime);
        map.put("confirmStatus", this.confirmStatus);
        map.put("remarksByAdmin", this.remarksByAdmin);
        map.put("gameMode", this.gameMode);
        map.put("approvedBy", this.approvedBy);
        map.put("approvedTime", this.approvedTime);
        map.put("bookingNo", this.bookingNo);
        map.put("refNo", this.refNo);
        return map;
    }
    public String[] getListValues(String supplierName){
        // {"gameDate","slotCode","gameMode","confirmStatus","bookedBy","bookTime","approvedBy","RemarksByUser","RemarksByAdmin"}
        String values[] = {this.gameDate.toString(), this.slotCode,this.gameMode,this.confirmStatus, this.bookedBy, this.bookedBy, String.valueOf(this.bookTime),this.approvedBy,this.remarksByUser, this.remarksByAdmin};
        return values;
    }
}