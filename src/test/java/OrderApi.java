import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class OrderApi {

    private static final String ORDERS_ENDPOINT = "/api/v1/orders";

    // Получение списка заказов
    public ValidatableResponse getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS_ENDPOINT)
                .then().log().all();
    }

    // Создание заказа
    public ValidatableResponse createOrder(Object order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDERS_ENDPOINT)
                .then();
    }

    // Отмена заказа
    public void cancelOrder(String track) {
        given()
                .header("Content-type", "application/json")
                .when()
                .put("/api/v1/orders/cancel?track=" + track)
                .then()
                .statusCode(SC_OK);  // Проверяю, что ответ имеет статус 200 (OK) при отмене
    }
}

