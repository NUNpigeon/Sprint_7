import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Step;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.apache.http.HttpStatus.*;

public class OrderListTest extends ApiTest {

    @Test
    @DisplayName("Запрос списка заказов возвращает заказы")
    public void getOrderListReturnsOrders() {
        // Получаем ответ на запрос списка заказов, используя метод с аннотацией @Step
        ValidatableResponse response = getOrders();

        // Проверяем, что вернулся код 200 (OK)
        response.assertThat().statusCode(SC_OK);

        // Проверяем, что тело ответа не пустое
        response.assertThat().body(notNullValue());

        // Проверяем, что "orders" - это список (List)
        response.assertThat().body("orders", instanceOf(List.class));

        // Проверяем, что размер списка "orders" больше или равен 0
        response.assertThat().body("orders.size()", greaterThanOrEqualTo(0));
    }

    @Step("Получение списка заказов")
    private ValidatableResponse getOrders() {
        return orderApi.getOrders(); // Вызов метода API для получения списка заказов
    }
}
