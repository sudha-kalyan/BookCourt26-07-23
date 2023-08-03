package com.CourtReserve.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String slotCode; //
    private String courtCode;
    private String dayType;// Regular / PH / WEND / Spl / dayOff / SplPH
    private String startHour;// 24 hr fomrat
    private String endHour;
    private int slotLength = 60; // in minutes

    public Map<String, java.io.Serializable> getDict(){
        Map<String, java.io.Serializable> map = new HashMap<String, java.io.Serializable>();
        map.putIfAbsent("id",id);
        map.putIfAbsent("slotCode", slotCode);
        map.putIfAbsent("courtCode", courtCode);
        map.putIfAbsent("dayType", dayType);
        map.putIfAbsent("startHour", startHour);
        map.putIfAbsent("endHour", endHour);
        map.putIfAbsent("slotLenght", slotLength);

        return map;
    }
}
