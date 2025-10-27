
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.Order;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Step;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
@DisplayName("Тесты создания заказа")
public class CreatingOrderTest extends ApiTest {

    private final List<String> color;
    private String track;

    public CreatingOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    @DisplayName("Тестовые данные для создания заказа с разными цветами")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Step("Создание заказа и проверка получения трек-номера с цветом: {color}")
    @Test
    @DisplayName("Создание заказа и проверка получения трек-номера")
    public void creatingOrderReturnsTrack() {
        ValidatableResponse response = createOrder("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355-35-35", 5, "2025-11-01", "Comment", color);
        response.assertThat().statusCode(201).and().body("track", notNullValue());
        track = response.extract().path("track").toString(); // Сохраняем трек номер.
    }

    @After
    public void tearDown() {
        if (track != null) {
            cancelOrder(track); // Если заказ был создан, отменяем его.
            track = null; // Сбрасываем значение track.
        }
    }

    @Step("Отправка запроса на создание заказа с данными: firstName={firstName}, lastName={lastName}, address={address}, metroStation={metroStation}, phone={phone}, rentTime={rentTime}, deliveryDate={deliveryDate}, comment={comment}, color={color}")
    private ValidatableResponse createOrder(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, List<String> color) {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then();
    }

    @Step("Отмена заказа с track: {track}")
    private void cancelOrder(String track) {
        given()
                .header("Content-type", "application/json")
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then()
                .statusCode(200);
    }
}
