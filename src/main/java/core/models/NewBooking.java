package core.models;

// Модель бронирования для запроса и для вложенного объекта booking в ответе.
// bookingid здесь НЕ нужен - он приходит снаружи, в обёртке
public class NewBooking {
    private String firstname;
    private String lastname;
    private int totalprice;
    private boolean depositpaid;
    private BookingDates bookingdates;   // вложенная модель
    private String additionalneeds;

    // Геттеры и сеттеры для ВСЕХ полей: правая кнопка -> Generate -> Getter and Setter.
    // Без них Jackson не увидит поле ни при сериализации, ни при разборе
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public int getTotalprice() { return totalprice; }
    public void setTotalprice(int totalprice) { this.totalprice = totalprice; }
    public boolean isDepositpaid() { return depositpaid; }          // для boolean IDEA сгенерирует is...
    public void setDepositpaid(boolean depositpaid) { this.depositpaid = depositpaid; }
    public BookingDates getBookingdates() { return bookingdates; }
    public void setBookingdates(BookingDates bookingdates) { this.bookingdates = bookingdates; }
    public String getAdditionalneeds() { return additionalneeds; }
    public void setAdditionalneeds(String additionalneeds) { this.additionalneeds = additionalneeds; }
}