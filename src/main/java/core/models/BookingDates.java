package core.models;

// Модель дат: две строки. Дата в JSON - обычная строка, поэтому String, а не LocalDate
public class BookingDates {
    private String checkin;
    private String checkout;

    // Пустой конструктор - нужен Jackson, чтобы СОБРАТЬ объект из ответа
    public BookingDates() {
    }

    // Конструктор с параметрами - нужен нам, чтобы создать даты в одну строку в тесте
    public BookingDates(String checkin, String checkout) {
        this.checkin = checkin;
        this.checkout = checkout;
    }

    public String getCheckin() { return checkin; }
    public void setCheckin(String checkin) { this.checkin = checkin; }
    public String getCheckout() { return checkout; }
    public void setCheckout(String checkout) { this.checkout = checkout; }
}