import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import io.restassured.specification.RequestSpecification;

@DisplayName("Базовый класс для API тестов")
public class ApiTest {

    protected static final String BASE_URI = "https://qa-scooter.praktikum-services.ru/";
    protected static final String COURIER_ENDPOINT = "/api/v1/courier";
    protected static final String LOGIN_ENDPOINT = "/api/v1/courier/login";
    protected static final String ORDERS_ENDPOINT = "/api/v1/orders";
    protected String courierLogin;

    @Before
    @DisplayName("Настройка базового URI перед тестом")
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierLogin = null;   // Сбрасываем значения логина курьера перед каждым тестом
    }

    @After
    public void tearDown() {   // Метод выполняется после каждого теста.
        if (courierLogin != null) {
            deleteCourier(courierLogin);
            courierLogin = null;
        }
    }
    protected RequestSpecification spec() {  // это можно вынести в отдельный метод
        return given().header("Content-type", "application/json");
    }
    @Step("Создание запроса на создание курьера")
    protected ValidatableResponse createCourierRequest(Courier courier) {
        return spec().body(courier)
                .when()
                .post(COURIER_ENDPOINT)
                .then();
    }

    @Step("Создание запроса на логин курьера")
    protected ValidatableResponse loginCourier(Courier credentials) {
        return spec().body(credentials)
                .when()
                .post(LOGIN_ENDPOINT)
                .then();
    }

    @Step("Создание запроса на получение списка заказов")
    protected ValidatableResponse getOrders() {
        return spec()
                .when()
                .get(ORDERS_ENDPOINT)
                .then();
    }

    @Step("Удаление курьера")
    protected void deleteCourier(String login) { // Метод для удаления курьера
        Courier credentials = new Courier(login, "1234", "saske");
        ValidatableResponse loginResponse = loginCourier(credentials);
        Integer id = loginResponse.extract().path("id");   // Берем id из ответа

        spec().when()
                .delete("/api/v1/courier/" + id)
                .then()
                .assertThat().statusCode(200);    // Попытка удалить курьера.
    }
}
