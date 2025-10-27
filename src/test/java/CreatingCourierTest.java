import io.qameta.allure.Step;
import org.example.Courier;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.hamcrest.CoreMatchers.equalTo;

@DisplayName("Тесты создания курьера")
public class CreatingCourierTest extends ApiTest {

    @Step("Создание курьера с валидными данными")
    @Test
    @DisplayName("Создание курьера с валидными данными")
    public void createCourierAndCheckStatusCode() {
        Courier courier = createCourier("ninja", "1234", "saske");
        createCourierRequest(courier)
                .assertThat().statusCode(201)
                .and()
                .body("ok", equalTo(true));
    }

    @Step("Создание курьера без обязательного поля")
    @Test
    @DisplayName("Создание курьера без обязательного поля")
    public void createCourierWithoutRequiredField() {
        Courier courier = createCourier(null, "1234", "saske");
        createCourierRequest(courier)
                .assertThat().statusCode(400)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создание курьера с существующим логином")
    @Test
    @DisplayName("Создание курьера с существующим логином")
    public void createCourierWithExistingLogin() {
        String login = "existingLogin";
        Courier courier1 = createCourier(login, "123Asd", "Alex");
        createCourierRequest(courier1).assertThat().statusCode(409);
        Courier courier2 = createCourier(login, "188N70a", "Boris");
        createCourierRequest(courier2)
                .assertThat().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется"));
    }

    @Step("Создание объекта Courier с: логином - {login}, паролем - {password}, именем - {name}")
    private Courier createCourier(String login, String password, String name) {
        return new Courier(login, password, name);
    }
}