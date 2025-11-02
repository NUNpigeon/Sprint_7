import org.example.Courier;
import org.junit.After;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты создания курьера")
public class CreatingCourierTest extends ApiTest {

    private String courierLogin;

    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void createCourierAndCheckStatusCode() {
        Courier courier = createCourier("ninja", "1234", "saske"); // Создаю курьера с данными
        courierApi.createCourierRequest(courier)
                .assertThat().statusCode(SC_CREATED) // Проверяю код ответа - 201 Created
                .and()
                .body("ok", equalTo(true));
        courierLogin = "ninja";
    }

    @Test
    @DisplayName("Создание курьера без обязательного поля")
    public void createCourierWithoutRequiredField() {
        Courier courier = createCourier(null, "1234", "saske"); // Создаю курьерера без логина
        courierApi.createCourierRequest(courier)
                .assertThat().statusCode(SC_BAD_REQUEST) // Проверяю код ответа - 400 Bad Request
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void createCourierWithExistingLogin() {
        String login = "existingLogin";
        Courier courier1 = createCourier(login, "123Asd", "Alex"); // Создаю первого курьера
        courierApi.createCourierRequest(courier1).assertThat().statusCode(SC_CREATED); // Проверяю, что первый курьер создан

        Courier courier2 = createCourier(login, "188N70a", "Boris"); // Создаю второго курьера с тем же логином
        courierApi.createCourierRequest(courier2)
                .assertThat().statusCode(SC_CONFLICT) // Проверяю код ответа - 409 Conflict
                .and()
                .body("message", equalTo("Этот логин уже используется")); // Проверяю сообщение об ошибке
        courierLogin = login;
    }

    private Courier createCourier(String login, String password, String name) {
        return new Courier(login, password, name);
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPassword() {
        Courier courier = createCourier("loginWithoutPassword", null, "saske");
        courierApi.createCourierRequest(courier)
                .assertThat().statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        if (courierLogin != null) {
            courierApi.deleteCourier(courierLogin); // Удаляю курьера после выполнения теста
            courierLogin = null;
        }
    }
}
