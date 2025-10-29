
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.Order;
import io.qameta.allure.junit4.DisplayName;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.runners.Parameterized.*;
import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
@DisplayName("Тесты создания заказа")
public class CreatingOrderTest extends ApiTest {

    private final List<String> color;
    private String track;

    public CreatingOrderTest(List<String> color) {
        this.color = color;
    }

    @Parameters
    @DisplayName("Тестовые данные для создания заказа с разными цветами")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList("BLACK")},
                {Arrays.asList("GREY")},
                {Arrays.asList("BLACK", "GREY")},
                {null}
        });
    }

    @Test
    @DisplayName("Создание заказа и проверка получения трек-номера")
    public void creatingOrderReturnsTrack() {

        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355-35-35", 5, "2025-11-01", "Comment", color);

        ValidatableResponse response = orderApi.createOrder(order);

        response.assertThat().statusCode(SC_CREATED).and().body("track", notNullValue());
        track = response.extract().path("track").toString();
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderApi.cancelOrder(track);
            track = null;
        }
    }
}
