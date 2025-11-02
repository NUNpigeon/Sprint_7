import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

public class CourierApi {
    private static final String COURIER_ENDPOINT = "/api/v1/courier";
    private static final String LOGIN_ENDPOINT = "/api/v1/courier/login";


    private ValidatableResponse postRequest(Object body, String endpoint) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(endpoint)
                .then();
    }

    public ValidatableResponse createCourierRequest(Object courier) {

        return postRequest(courier, COURIER_ENDPOINT);
    }

    public ValidatableResponse loginCourier(Object credentials) {

        return postRequest(credentials, LOGIN_ENDPOINT);
    }

    public void deleteCourier(String login) {

        ValidatableResponse loginResponse = loginCourier(new org.example.Courier(login, "1234", "saske") );
        Integer id = loginResponse.extract().path("id");


        given()
                .header("Content-type", "application/json")
                .when()
                .delete(COURIER_ENDPOINT + "/" + id)
                .then()
                .statusCode(SC_OK);
    }
}
