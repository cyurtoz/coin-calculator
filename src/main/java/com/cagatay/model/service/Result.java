package com.cagatay.model.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private ServiceError error;
    private T body;
    private boolean success;
    private String message;

    public static <T> Result<T> success(T body) {
        Result<T> resp = new Result<>();
        resp.body = body;
        resp.success = true;
        return resp;
    }

    public static <T> Result<T> successWithMessage(T body, String message) {
        Result<T> success = success(body);
        success.message = message;
        return success;
    }

    public static <T> Result<T> failWithMessageArgs(ServiceError err, Object[] messageArgs) {
        Result<T> resp = new Result<>();
        resp.error = err;
        resp.success = false;
        resp.message = String.format(err.getMessageTemplate(), messageArgs);
        return resp;
    }

    public static <T> Result<T> failedWithError(ServiceError err) {
        return failWithMessageArgs(err, new Object[]{});
    }

    public static <T, Y> Result<T> failedWithResult(Result<Y> validatedResult) {
        Result<T> resp = new Result<>();
        resp.error = validatedResult.getError();
        resp.success = false;
        resp.message = validatedResult.getMessage();
        return resp;
    }
}
