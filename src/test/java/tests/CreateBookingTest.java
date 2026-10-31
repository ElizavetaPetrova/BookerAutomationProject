package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.ApiClient;
import core.models.BookingDates;
import core.models.CreatedBooking;
import core.models.NewBooking;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;       // assertThat - из AssertJ
import static org.junit.jupiter.api.Assertions.assertEquals;    // assertEquals - из JUnit

public class CreateBookingTest {
    private ApiClient apiClient;
    private ObjectMapper objectMapper;
    private CreatedBooking createdBooking;   // то, что вернул сервер (заполнится в тесте)
    private NewBooking newBooking;           // то, что отправляем

    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
        objectMapper = new ObjectMapper();

        // Объект бронирования без конструктора - через сеттеры. Так тоже можно
        newBooking = new NewBooking();
        newBooking.setFirstname("John");
        newBooking.setLastname("Doe");
        newBooking.setTotalprice(150);
        newBooking.setDepositpaid(true);
        newBooking.setBookingdates(new BookingDates("2025-01-10", "2025-01-15")); // даты в одну строку
        newBooking.setAdditionalneeds("Breakfast");
    }

    @Test
    @DisplayName("Создание бронирования с корректными данными")
    @Description("Сценарий 1 из файла с заданием: POST /booking, проверка ответа, удаление")
    public void createBookingWithValidData() throws JsonProcessingException {
        // Шаг 1. Объект -> JSON-строка (сериализация)
        String requestBody = objectMapper.writeValueAsString(newBooking);

        // Шаг 2. Отправляем запрос и проверяем код ответа
        Response response = apiClient.createBooking(requestBody);
        assertThat(response.getStatusCode()).isEqualTo(200);

        // Шаг 3. JSON-ответ -> объект (десериализация)
        String responseBody = response.asString();
        createdBooking = objectMapper.readValue(responseBody, CreatedBooking.class);
        assertThat(createdBooking).isNotNull();

        // Шаг 4. Сверяем: то, что создалось, совпадает с тем, что отправляли.
        // Порядок аргументов: сначала ожидаемое (что отправили), потом фактическое (что пришло)
        assertEquals(newBooking.getFirstname(), createdBooking.getBooking().getFirstname());
        assertEquals(newBooking.getLastname(), createdBooking.getBooking().getLastname());
        assertEquals(newBooking.getTotalprice(), createdBooking.getBooking().getTotalprice());
        assertEquals(newBooking.isDepositpaid(), createdBooking.getBooking().isDepositpaid());
        assertEquals(newBooking.getBookingdates().getCheckin(),
                createdBooking.getBooking().getBookingdates().getCheckin());
        assertEquals(newBooking.getBookingdates().getCheckout(),
                createdBooking.getBooking().getBookingdates().getCheckout());
        assertEquals(newBooking.getAdditionalneeds(), createdBooking.getBooking().getAdditionalneeds());
    }

    @AfterEach
    public void tearDown() {
        // Постусловие. Для DELETE нужен токен - по правилам API он обязателен для PUT и DELETE
        apiClient.createToken("admin", "password123");

        if (createdBooking != null) {                     // удаляем, только если бронирование создалось
            int bookingId = createdBooking.getBookingid();
            apiClient.deleteBooking(bookingId);
            // И убеждаемся, что такого бронирования больше нет: ждём 404
            apiClient.getBookingById(bookingId, 404);
        }
    }
}
// Вывод: тест зелёный; в консоли благодаря log().all() видно запрос и ответ; бронирование удалено
