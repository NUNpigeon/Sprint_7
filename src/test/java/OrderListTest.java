import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import java.util.List;

public class OrderListTest extends ApiTest {

    @Test
    @DisplayName("Запрос списка заказов возвращает заказы")
    public void getOrderListReturnsOrders() {

        // Отправляем запрос для получения списка заказов
        ValidatableResponse response = getOrders();

        // Проверяем, что статус код ответа 200 (OK)
        response.assertThat().statusCode(200);

        // Проверяем, что тело ответа не пустое
        response.assertThat().body(notNullValue());

        // Проверяем, что поле "orders" в теле ответа является списком (List)
        response.assertThat().body("orders", instanceOf(List.class));

        // Проверяем, что размер списка заказов больше или равен 0 (то есть список не пустой)
        response.assertThat().body("orders.size()", greaterThanOrEqualTo(0));
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrders() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then().log().all();
    }
}
