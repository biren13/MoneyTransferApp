package com.revolut.controller;

import com.revolut.response.StandardResponse;
import spark.Request;
import spark.Response;

public interface AccountTransactionController {
    StandardResponse listAllTransaction(Request request, Response response) throws Exception;
}
