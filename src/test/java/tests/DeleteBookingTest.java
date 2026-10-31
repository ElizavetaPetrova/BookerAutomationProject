package tests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.ApiClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// Каркас теста: что и в каком порядке вызывать. Наполнение - твоё
public class DeleteBookingTest {
    private ApiClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
    }

    @Test
    public void deleteExistingBooking() {
        // 1. получить список id бронирований (getBooking) и выбрать один
        Response response = apiClient.getBooking();
        int bookingId = response.jsonPath().getInt("[0].bookingid");
        // 2. вызвать apiClient.deleteBooking(id)
        apiClient.createToken("admin", "password123");
        Response deleteResponse = apiClient.deleteBooking(bookingId);
        // 3. проверить, что бронирования больше нет: GET /booking/{id} -> 404
        Response getResponse = apiClient.getBookingById(bookingId, 404);
    }

    @Test
    public void deleteExistingBookingWithoutToken() {
        // 1. получить список id бронирований (getBooking) и выбрать один
        Response response = apiClient.getBooking();
        int bookingId = response.jsonPath().getInt("[0].bookingid");
        // 2. вызвать apiClient.deleteBooking(id)
        Response deleteResponse = apiClient.deleteBooking(bookingId, 403);
    }
}
