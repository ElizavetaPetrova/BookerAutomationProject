package core.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingById {
    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public static class Bookingdates{
        public String checkin;
        public String checkout;

        @JsonCreator
        public Bookingdates(
                @JsonProperty("checkin") String checkin,
                @JsonProperty("checkout") String checkout
        )
        {
            this.checkin = checkin;
            this.checkout = checkout;
        }
    }

    public String firstname;
    public String lastname;
    public int totalprice;
    public boolean depositpaid;
    public Bookingdates bookingdates;
    public String additionalneeds;


    @JsonCreator
    public BookingById(
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("totalprice") int totalprice,
            @JsonProperty("depositpaid") boolean depositpaid,
            @JsonProperty("bookingdates") Bookingdates bookingdates,
            @JsonProperty("additionalneeds") String additionalneeds


    )
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;

    }

}
