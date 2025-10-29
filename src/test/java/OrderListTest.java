import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.*;

import java.util.List;

import static org.apache.http.HttpStatus.*;

public class OrderListTest extends ApiTest {

    @Test
    @DisplayName("Запрос списка заказов возвращает заказы")
    public void getOrderListReturnsOrders() {
        ValidatableResponse response = orderApi.getOrders();

        response.assertThat().statusCode(SC_OK); // Проверяю, что вернулся код 200 (OK)

        response.assertThat().body(notNullValue());

        response.assertThat().body("orders", instanceOf(List.class));

        response.assertThat().body("orders.size()", greaterThanOrEqualTo(0));
    }
}
