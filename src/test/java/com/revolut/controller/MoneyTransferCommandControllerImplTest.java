package com.revolut.controller;

import com.revolut.RevolutMoneyTransferApp;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class MoneyTransferCommandControllerImplTest {
    private final String SERVICE_END_POINT = "http://localhost:4567/transfer";
    @
            BeforeClass
    public static void setUp() {
        RevolutMoneyTransferApp.startApp();

    }
    @AfterClass
    public static void tearDown() {
        RevolutMoneyTransferApp.stopApp();
    }

    @Test
    public void testRestServiceEndPoint()
    {

        given().
                when().
                body("{ \"fromAccountId\":1,\"toAccountId\":2,\"amount\":10,\"currency\":\"USD\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(200);

        given().
                when().
                body("{ \"fromAccountId\":3,\"toAccountId\":2,\"amount\":10,\"currency\":\"USD\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(400);

        given().
                when().
                body("{ \"fromAccountId\":1,\"toAccountId\":3,\"amount\":10,\"currency\":\"USD\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(400);

        given().
                when().
                body("{ \"fromAccountId\":1,\"toAccountId\":2,\"amount\":10,\"currency\":\"EUR\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(400);

    }
}
