package core.clients;

import core.settings.ApiEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiClient {

    // базовый адрес стенда: задаётся один раз при создании клиента
    private final String baseUrl;

    // конструктор: вызывается при new ApiClient()
    public ApiClient() {
        this.baseUrl = determineBaseUrl();
    }

    // Общая заготовка запроса: адрес + заголовки. given() пишем ОДИН раз - здесь
    private RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .baseUri(baseUrl)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
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

    public Response ping() {
        return getRequestSpec()                    // дано: адрес и заголовки
                .when()                            // когда: отправляем запрос
                .get(ApiEndpoints.PING.getPath())  // GET на /ping
                .then()                            // затем: проверяем ответ
                .statusCode(201)                   // ожидаем 201 Created
                .extract()                         // распаковываем...
                .response();                       // ...весь ответ целиком
    }

    public Response getBooking() {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath())
                .then()
                .statusCode(200)                   // здесь ожидаем 200 OK
                .extract()
                .response();
    }

    public Response getBookingById(int id) {
        return getRequestSpec()
                .when()
                .get(ApiEndpoints.BOOKING.getPath() + "/" + id)
                .then()
                .statusCode(200)                   // здесь ожидаем 200 OK
                .extract()
                .response();
    }
}