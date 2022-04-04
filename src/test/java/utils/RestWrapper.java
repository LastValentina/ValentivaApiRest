package utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import pojos.UserLogin;

public class RestWrapper {
    private final static String URI = "https://reqres.in/api";
    private final static String EP_USERS = "/users";

    private static RequestSpecification REC_SPEC;
    private Cookies cookies;

    private RestWrapper(Cookies cookies){
        this.cookies = cookies;

        RequestSpecification REC_SPEC =
                new RequestSpecBuilder()
                        .addCookies(cookies)
                        .setBaseUri(URI)
                        .setBasePath(EP_USERS)
                        .setContentType(ContentType.JSON)
                        .build();
    }

    public static RestWrapper loginAs(String login, String password){
        Cookies cookies = RestAssured.given()
                .baseUri(URI).basePath(EP_USERS).contentType(ContentType.JSON)
                .body(new UserLogin(login, password))
                .post()
                .getDetailedCookies();
        return new RestWrapper(cookies);
    }
}
