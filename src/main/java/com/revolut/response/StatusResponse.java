package com.revolut.response;

public enum StatusResponse {
    SUCCESS ("Success"),
    ERROR ("Error");

    StatusResponse(String status)
    {
        this.status = status;
    }
    private String status;
}
