package com.CourtReserve.app.repositories;

import com.CourtReserve.app.models.BookSlot;
import com.azure.spring.data.cosmos.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookSlotRepository extends CrudRepository<BookSlot, Integer> {
    List<BookSlot> findByGameDateAndGameModeAndSlotCode(LocalDate gameDate, String gameMode, String slotCode);
    List<BookSlot> findByGameDateAndConfirmStatus(LocalDate gameDate, String confirmStatus);
    List<BookSlot> findByBookedByOrderBySlotCodeAscGameDateAscCourtCodeAsc(String bookedBy);
    List<BookSlot> findByConfirmStatusAndGameDateAfterOrGameDateAndConfirmStatusOrderBySlotCodeAscGameDateAsc(String confirmStatus, LocalDate gameDate, LocalDate gameDate1, String confirmStatus1);
    List<BookSlot> findByConfirmStatusAndGameDateOrGameDateAfterAndConfirmStatusOrderByGameDateAsc(String confirmStatus, LocalDate gameDate, LocalDate gameDate1, String confirmStatus1);
    List<BookSlot> findByGameDateAfterOrGameDateAndBookedBy(LocalDate gameDate, LocalDate gameDate1, String bookedBy);
    BookSlot findByGameDateAndSlotCodeAndBookedBy(LocalDate gameDate, String slotCode, String bookedBy);
    List<BookSlot> findByGameDateBeforeAndBookedBy(LocalDate gameDate, String bookedBy);
    List<BookSlot> findByGameDateAfter(LocalDate gameDate);
    List<BookSlot> findByGameDateBefore(LocalDate gameDate);
    BookSlot findBySlotCodeAndGameDateAndConfirmStatus(String slotCode, LocalDate gameDate, String confirmStatus);
    List<BookSlot> findBySlotCodeAndGameDate(String slotCode, LocalDate gameDate);


    List<BookSlot> findByBookedByAndGameDateOrGameDateAfterAndBookedBy(String mobileNo, LocalDate now, LocalDate now1, String mobileNo1);

    List<BookSlot> findByBookedByAndGameDateOrGameDateBeforeAndBookedBy(String mobileNo, LocalDate now, LocalDate now1, String mobileNo1);

    List<BookSlot> GameDateBeforeAndBookedBy(LocalDate now, String mobileNo);


    List<BookSlot> findByGameDateAndSlotCode(LocalDate date, String slotCode);

    List<BookSlot> findByBookedByOrderBySlotCodeDescGameDateDescCourtCodeDesc(String mobileNo);

    List<BookSlot> findByGameDateAndConfirmStatusAndSlotCode(LocalDate dateModified, String pending, String slotCode);

    List<BookSlot> findByGameDate(LocalDate dateModified);

    List<BookSlot> findByBookedByOrderBySlotCodeDescGameDateDesc(String mobileNo);

    List<BookSlot> findByGameDateAndConfirmStatusAndSlotCodeAndGameMode(LocalDate gameDate, String accepted, String slotCode,String gameMode);

    List<BookSlot> findByBookedByOrderByGameDateDesc(String mobileNo);

    List<BookSlot> findByBookedByAndGameDate(String bookedBy,LocalDate gameDate);
    @Query("select * from  book_slot where  game_date =?1 and booked_by =?2")
    List<BookSlot> findByGameDateAndBookedBy(LocalDate gameDate,String bookedBy);
    @Query("select * from  book_slot where  booked_by =?1 and game_date =?2")
        // List<BookSlot> findByBookedByAndGameDate(String bookedBy,LocalDate gameDate);
        // @Query("select * from book_slot where booked_by=?1")
    BookSlot findByBookedBy(String bookedBy);
}