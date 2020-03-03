package com.revolut.controller;

import com.revolut.response.StandardResponse;
import spark.Request;
import spark.Response;

public interface MoneyTransferCommandController {
    StandardResponse doMoneyTransfer(Request request, Response response) throws Exception;
}
