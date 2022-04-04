import io.qameta.allure.Attachment;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pojos.CreateUserReq;
import pojos.CreateUserResponse;
import pojos.UserPojo;
import utils.RestWrapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Feature("API Users")
public class RestTest {
    private static RestWrapper api;

    private final static String URI = "https://reqres.in/api";
    private final static String EP_USERS = "/users";

    private static final RequestSpecification REC_SPEC =
            new RequestSpecBuilder()
                    .addFilter(new AllureRestAssured())
                    .setBaseUri(URI)
                    .setBasePath(EP_USERS)
                    .setContentType(ContentType.JSON)
                    .build();

    @BeforeClass
    // api -> не изменены тесты - нужно переписать под степы.
    public static void prepareClient(){
        api = RestWrapper.loginAs("eve.holt@reqres.in", "cityslicks");
    }

    @Test
    @Story("get all users")
    // api -> не изменены тесты - нужно переписать под степы.
    // api.getUsers().extracting...
    public void getUsers(){
        RestAssured.given()
  //              .baseUri(URI).basePath(EP_USERS).contentType(ContentType.JSON)
                .spec(REC_SPEC)  // заменяем этим методом повторяющийся фрагмент
                .when().get().prettyPeek()
                .then().statusCode(200)
        //.body("data.find{it.email=='george.bluth@reqres.in'}.first_name", equalTo("George"));
        .body("data[0].email", equalTo("george.bluth@reqres.in"));
    }

    @Test
    public void getUsersAndEmails(){
        List<UserPojo> users = RestAssured.given()
                //.baseUri(URI).basePath(EP_USERS).contentType(ContentType.JSON)
                .spec(REC_SPEC)  // заменяем этим методом повторяющийся фрагмент
                .when().get().prettyPeek()
                .then().statusCode(200)
                .extract().jsonPath().getList("data", UserPojo.class);

        assertThat(users).extracting(UserPojo::getEmail).contains("george.bluth@reqres.in");

        System.out.println(users.get(1).getEmail());
    }

    @Test
    public void createUser(){
  //      CreateUserReq rq = new CreateUserReq();
  //      rq.setName("Ivan");
  //      rq.setJob("automator");
        CreateUserReq rq = CreateUserReq.builder()
                .name("Ivan")
                .job("automator")
                .build();

        CreateUserResponse rs = RestAssured.given()
                //.baseUri(URI).basePath(EP_USERS).contentType(ContentType.JSON)
                .spec(REC_SPEC)  // заменяем этим методом повторяющийся фрагмент
                .body(rq)
                .when().post().prettyPeek()
                .then().extract().as(CreateUserResponse.class);

        assertThat(rs)
                .isNotNull()
                .extracting(CreateUserResponse::getName)
                .isEqualTo(rq.getName());
    }

    @Test
    public void getEmails(){
        List<String> emails = RestAssured.given()
                //.baseUri(URI).basePath(EP_USERS).contentType(ContentType.JSON)
                .spec(REC_SPEC)  // заменяем этим методом повторяющийся фрагмент
                .when().get().prettyPeek()
                .then().statusCode(200)
                .extract().jsonPath().getList("data.email");

        System.out.println(emails.toString() + " " + emails.get(0));
    }
}
