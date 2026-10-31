package core.settings;

// enum - набор именованных констант. У каждой есть поле path и геттер к нему
public enum ApiEndpoints {
    PING("/ping"),
    BOOKING("/booking"),
    AUTH("/auth");

    private final String path;

    // Конструктор enum вызывается автоматически для каждой константы
    ApiEndpoints(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

// Использование:
// ApiEndpoints.PING.getPath()    -> "/ping"
// ApiEndpoints.BOOKING.getPath() -> "/booking"
