package com.revolut.controller;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.revolut.RevolutMoneyTransferApp;
import com.revolut.dom.AccountTransaction;
import com.revolut.response.StandardResponse;
import com.revolut.util.JsonParser;
import com.revolut.util.JsonParserImpl;
import io.restassured.response.Response;
import org.junit.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountTransactionControllerImplTest {
    private final String SERVICE_END_POINT = "http://localhost:4567/transfer";
    private JsonParser jsonParser;

    @BeforeClass
    public static void setupServer()
    {
        RevolutMoneyTransferApp.startApp();

    }
    @Before
    public void setUp() {
        jsonParser = new JsonParserImpl();

    }

    @AfterClass
    public static  void  tearDown() {
        RevolutMoneyTransferApp.stopApp();
    }

    @Test
    public void testEmptyTransactionList() {

        Response res = get(SERVICE_END_POINT+"/1");
        String body = res.asString();
        StandardResponse response = jsonParser.toPojoFromJson(res.asString(),StandardResponse.class);

        List transactionList= (List)response.getData();
        Assert.assertTrue(transactionList.size() == 0);
        assertEquals("SUCCESS",response.getStatus().name());

    }

    @Test
    public void testTransactionListAfterMoneyTransfer()
    {
        given().
                when().
                body("{ \"fromAccountId\":1,\"toAccountId\":2,\"amount\":10,\"currency\":\"USD\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(200);
        given().
                when().
                body("{ \"fromAccountId\":1,\"toAccountId\":2,\"amount\":5,\"currency\":\"USD\"}").
                post(SERVICE_END_POINT).
                then().
                assertThat().statusCode(200);
        Response res = get(SERVICE_END_POINT+"/1");
        String body = res.asString();
        StandardResponse response = jsonParser.toPojoFromJson(res.asString(),StandardResponse.class);

        List transactionList= (List)response.getData();

        Assert.assertTrue(transactionList.size() == 2);
        LinkedTreeMap jsonMap1 = (LinkedTreeMap)transactionList.get(0);

        String creditDebitIndicator =(String)jsonMap1.get("creditDebitIndicator");
        assertEquals("D",creditDebitIndicator);

        LinkedTreeMap jsonMap2 = (LinkedTreeMap)transactionList.get(1);

        Double amount =(Double) jsonMap2.get("amount");
        assertTrue(amount.doubleValue() == 5.0d);

    }
}
