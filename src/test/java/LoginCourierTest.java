import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest extends ApiTest {

    private Courier testCourier;
    private boolean courierCreated = false;

    @Before
    public void setUp() {
        // Создаем курьера для тестов
        testCourier = new Courier("ninja", "1234", "Василий");
        createCourier(testCourier);
        courierCreated = true;
    }

    @After
    public void tearDown() {
        if (courierCreated) { //если курьер был создан, удаляем его
            deleteCourier(testCourier); // Удаляем курьера через API
        }
    }

    @Test
    @DisplayName("Курьер может успешно авторизоваться")
    public void courierCanLogin() {
        // Успешный запрос возвращает id
        ValidatableResponse response = loginCourier(testCourier);
        response.assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Step("Создание курьера")
    private void createCourier(Courier courier) {
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then();
    }

    @Step("Удаление курьера")
    private void deleteCourier(Courier courier) {
        //сначала нужно залогиниться, чтобы получить ID
        ValidatableResponse loginResponse = loginCourier(courier);
        int courierId = loginResponse.extract().path("id");

        given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/" + courierId);
    }


    @Step("Логин курьера")
    public ValidatableResponse loginCourier(Courier credentials) {
        return given()
                .header("Content-type", "application/json")
                .body(credentials)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all();
    }

    @Test
    @DisplayName("Ошибка при попытке логина без обязательных полей")
    public void loginWithoutRequiredFieldsReturnsError() {
        // Система возвращает ошибку, если не указан пароль
        Courier credentials = new Courier("ninja", null, "Василий");
        ValidatableResponse response = loginCourier(credentials);
        response.assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при попытке логина с неверными учетными данными")
    public void loginWithIncorrectCredentialsReturnsError() {
        // Система возвращает ошибку, если неправильно указан пароль
        Courier credentials = new Courier("ninja", "wrongPassword", "Василий");
        ValidatableResponse response = loginCourier(credentials);
        response.assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при попытке логина несуществующего курьера")
    public void loginNonExistentCourierReturnsError() {
        // Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку
        Courier credentials = new Courier("nonExistentUser", "password", "Василий");
        ValidatableResponse response = loginCourier(credentials);
        response.assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }
}
