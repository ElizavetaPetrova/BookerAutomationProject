package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.ApiClient;
import core.models.Booking;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingTest {
    private ApiClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
    }

    @Test
    public void getBookingShouldReturnListOfIds() throws JsonProcessingException {
        Response response = apiClient.getBooking();
        assertThat(response.getStatusCode()).isEqualTo(200);

        // 1. Тело ответа -> обычная строка
        String responseBody = response.getBody().asString();

        // 2. Строка -> список объектов Booking
        List<Booking> bookings = objectMapper.readValue(
                responseBody, new TypeReference<List<Booking>>() {});

        // 3. Список не пустой, и каждый id - положительное число
        assertThat(bookings).isNotEmpty();
        for (Booking booking : bookings) {
            assertThat(booking.getBookingid()).isGreaterThan(0);
        }
    }
}
