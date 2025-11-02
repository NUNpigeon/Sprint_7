import io.restassured.response.ValidatableResponse;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class OrderApi {

    private static final String ORDERS_ENDPOINT = "/api/v1/orders";

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        // Отправляю GET-запрос для получения списка заказов
        return given()
                .header("Content-type", "application/json") // Устанавливаю заголовок Content-Type
                .when()
                .get(ORDERS_ENDPOINT) // Выполняю GET-запрос к эндпоинту
                .then().log().all(); // Логирую все детали ответа в консоль для отладки
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Object order) {
        // Отправляю POST-запрос для создания заказа
        return given()
                .header("Content-type", "application/json") // Устанавливаю заголовок Content-Type
                .body(order) // Устанавливаю тело запроса (данные заказа)
                .when()
                .post(ORDERS_ENDPOINT) // Выполняю POST-запрос к эндпоинту
                .then(); // Возвращаю объект ValidatableResponse для дальнейших проверок
    }

    @Step("Отмена заказа с трек-номером: {track}")
    public void cancelOrder(String track) {
        // Отправляю PUT-запрос для отмены заказа
        given()
                .header("Content-type", "application/json") // Устанавливаю заголовок Content-Type
                .when()
                .put("/api/v1/orders/cancel?track=" + track) // Выполняю PUT-запрос к эндпоинту отмены заказа
                .then()
                .statusCode(SC_OK);  // Првоеряю, что статус код ответа равен 200
    }
}
