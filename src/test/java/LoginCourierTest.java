import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest extends ApiTest {

    private Courier testCourier;
    private boolean courierCreated = false;

    @Before
    public void setUp() {
        testCourier = new Courier("ninja", "1234", "Василий");
        createCourier(testCourier); // Создаю курьера перед каждым тестом
        courierCreated = true;
    }

    @After
    public void tearDown() {
        if (courierCreated) {
            deleteCourier(testCourier); // Удаляю курьера после каждого теста
        }
    }

    @Test
    @DisplayName("Курьер может успешно авторизоваться")
    public void courierCanLogin() {
        ValidatableResponse response = courierApi.loginCourier(testCourier);
        response.assertThat().statusCode(SC_OK).and().body("id", notNullValue()); // Проверка кода 200 при успешном входе
    }

    private void createCourier(Courier courier) {
        courierApi.createCourierRequest(courier).assertThat().statusCode(SC_CREATED); // Проверка кода 201 при создании
    }

    private void deleteCourier(Courier courier) {
        ValidatableResponse loginResponse = courierApi.loginCourier(courier);
        int courierId = loginResponse.extract().path("id");
        courierApi.deleteCourier(courier.getLogin());
    }

    @Test
    @DisplayName("Ошибка при попытке логина без обязательных полей")
    public void loginWithoutRequiredFieldsReturnsError() {
        Courier credentials = new Courier("ninja", null, "Василий");
        ValidatableResponse response = courierApi.loginCourier(credentials);
        response.assertThat().statusCode(SC_BAD_REQUEST).and().body("message", equalTo("Недостаточно данных для входа")); // Проверка кода 400
    }

    @Test
    @DisplayName("Ошибка при попытке логина с неверными учетными данными")
    public void loginWithIncorrectCredentialsReturnsError() {
        Courier credentials = new Courier("ninja", "wrongPassword", "Василий");
        ValidatableResponse response = courierApi.loginCourier(credentials);
        response.assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена")); // Проверка кода 404
    }

    @Test
    @DisplayName("Ошибка при попытке логина несуществующего курьера")
    public void loginNonExistentCourierReturnsError() {
        Courier credentials = new Courier("nonExistentUser", "password", "Василий");
        ValidatableResponse response = courierApi.loginCourier(credentials);
        response.assertThat().statusCode(SC_NOT_FOUND).and().body("message", equalTo("Учетная запись не найдена")); // Проверка кода 404
    }
}
