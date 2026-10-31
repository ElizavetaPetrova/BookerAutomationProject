package core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Booking {
    private int bookingid;

    // @JsonCreator говорит Jackson: «объект создавай через этот конструктор»,
    // @JsonProperty связывает ключ JSON с параметром конструктора
    @JsonCreator
    public Booking(@JsonProperty("bookingid") int bookingid) {
        this.bookingid = bookingid;
    }

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) { // сеттер пока не нужен, но так принято
        this.bookingid = bookingid;
    }
}