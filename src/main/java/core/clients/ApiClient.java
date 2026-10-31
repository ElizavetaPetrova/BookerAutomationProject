package core.clients;

import core.settings.ApiEndpoints;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.restassured.filter.Filter;

public class ApiClient {

    // базовый адрес стенда: задаётся один раз при создании клиента
    private final String baseUrl;
    private String token;

    // конструктор: вызывается при new ApiClient()
    public ApiClient() {
        this.baseUrl = determineBaseUrl();
    }

    // Общая заготовка запроса: адрес + заголовки. given() пишем ОДИН раз - здесь
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .filter(addAuthTokenFilter())
                .filter(logRequestFilter());
    }


    // Определяем адрес: читаем параметр env и файл application-<env>.properties
    private String determineBaseUrl() {
        // по ключу "env" читаем окружение; если ничего не передали - "test"
        String environment = System.getProperty("env", "test");

        // собираем имя файла: application-test.properties, application-prod.properties ...
        String configFileName = "application-" + environment + ".properties";

        Properties properties = new Properties();   // класс Java для файлов ключ=значение
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (input == null) {
                // файла нет в resources - говорим об этом прямо
                throw new IllegalStateException("Configuration file not found: " + configFileName);
            }
            properties.load(input);                 // загружаем настройки из файла
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration file: " + configFileName, e);
        }
        return properties.getProperty("base.url");  // тот самый адрес из файла
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void createToken(String username, String password) {
        // %s -> username, второй %s -> password. Кавычки внутри строки экранируем: \"
        String body = String.format(
                "{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        Response response = getRequestSpec()
                .body(body)                            // тело запроса - наша строка
                .when()
                .post(ApiEndpoints.AUTH.getPath())     // POST /auth
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Достаём значение по ключу "token" и кладём в поле класса
        token = response.jsonPath().getString("token");
    }

    private Filter addAuthTokenFilter() {
        return (requestSpec, responseSpec, ctx) -> {
            // Если токен уже получен - добавляем cookie; если null - запрос уходит как есть
            if (token != null) {
                requestSpec.cookie("token", token);
            }
            // Передаём управление дальше: следующему фильтру или на сервер
            return ctx.next(requestSpec, responseSpec);
        };
    }

    private Filter logRequestFilter() {
        return (requestSpec, responseSpec, ctx) -> {
            System.out.println(requestSpec.getMethod() + " " + requestSpec.getURI());
            return ctx.next(requestSpec, responseSpec);
        };
    }

    public Response ping() {
        return getRequestSpec()                    // дано: адрес и заголовки
                .when()                            // когда: отправляем запрос
                .get(ApiEndpoints.PING.getPath())  // GET на /ping
                .then()                            // затем: проверяем ответ
                .statusCode(201)                   // ожидаем 201 Created
                .extract()                         // распаковываем...
                .response();                       // ...весь ответ целиком
    }

    @Step("Получить список бронирований")
    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)                   // здесь ожидаем 200 OK
                .extract()
                .response();
    }


    public Response deleteBooking(int bookingId){
        return deleteBooking(bookingId, 201);
    }

    @Step("Удалить бронирование с id {bookingId}")
    public Response deleteBooking(int bookingId, int expectedStatusCode) {
        return getRequestSpec()                 // фильтр добавит cookie, если токен есть
                .pathParam("id", bookingId)     // значение для {id}
                .when()
                .delete(ApiEndpoints.BOOKING.getPath() + "/{id}")   // -> /booking/7
                .then()
                .log().all()                    // печатаем ответ в консоль - удобно смотреть, что вернулось
                .statusCode(expectedStatusCode)                // в этом API успешное удаление = 201 Created
                .extract()
                .response();
    }

    // Перегрузка: две версии метода с разным набором параметров
    public Response getBookingById(int bookingId) {
        return getBookingById(bookingId, 200);       // по умолчанию ждём 200
    }

    @Step("Получить бронирование по id")
    public Response getBookingById(int bookingId, int expectedStatusCode) {
        return getRequestSpec()
                .pathParam("id", bookingId)
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/{id}")
                .then()
                .statusCode(expectedStatusCode)      // а здесь - что попросили
                .extract()
                .response();
    }

    @Step("Создать бронирование")
    public Response createBooking(String body) {
        return getRequestSpec()
                .body(body)                            // тело запроса - JSON-строка
                .log().all()                           // печатаем запрос в консоль (можно убрать)
                .when()
                .post(ApiEndpoints.BOOKING.getPath())  // POST /booking
                .then()
                .log().all()                           // печатаем ответ
                .statusCode(200)
                .extract()
                .response();
    }
}