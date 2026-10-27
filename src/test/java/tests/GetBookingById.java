package tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.clients.ApiClient;
import core.models.Booking;
import core.models.BookingById;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GetBookingById {
    private ApiClient apiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        apiClient = new ApiClient();
    }

    @Test
    public void getBookingShouldReturnListOfIds() throws JsonProcessingException {
        Response response = apiClient.getBookingById(2);
        assertThat(response.getStatusCode()).isEqualTo(200);

        BookingById booking = response.as(BookingById.class);
        assertThat(booking.getFirstname()).isNotBlank();
        assertThat(booking.getLastname()).isNotBlank();
        }
    }
