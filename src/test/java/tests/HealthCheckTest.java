package tests;

import core.clients.ApiClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthCheckTest {
    private ApiClient apiClient;

    @BeforeEach                      // выполняется перед КАЖДЫМ тестом класса
    public void setUp() {
        apiClient = new ApiClient(); // свежий клиент для каждого теста
    }

    @Test
    public void pingShouldReturn201() {
        Response response = apiClient.ping();

        // AssertJ: проверяем, что выражение в скобках даёт true
        assertThat(response.getStatusCode() == 201).isTrue();
    }
}
// Вывод при запуске: зелёная галочка, 1 test passed