package com.revolut.response;

public class StandardResponse {
    private StatusResponse status;
    private String message;
    private Object data;

    public StandardResponse(StatusResponse status) {
    this.status = status;
    }
    public StandardResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;

    }
    public StandardResponse(StatusResponse status,Object data) {
        this.status = status;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    @Override
    public String toString() {
        return "StandardResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
