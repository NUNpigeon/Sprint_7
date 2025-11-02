import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import io.qameta.allure.junit4.DisplayName;

@DisplayName("Базовый класс для API тестов")
public class ApiTest {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    protected String courierLogin;
    protected CourierApi courierApi;
    protected OrderApi orderApi;

    @Before
    @DisplayName("Настройка базового URI и CourierApi перед тестом")
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierLogin = null;
        courierApi = new CourierApi();
        orderApi = new OrderApi();
    }

    @After
    public void tearDown() {
        if (courierLogin != null) {
            courierApi.deleteCourier(courierLogin);
            courierLogin = null;
        }
    }
}