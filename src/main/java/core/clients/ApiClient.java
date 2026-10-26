package core.clients;

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
}