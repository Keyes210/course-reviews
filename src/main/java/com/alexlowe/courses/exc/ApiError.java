package com.alexlowe.courses.exc;

/**
 * Created by Keyes on 6/22/2016.
 */
public class ApiError extends RuntimeException {
    private final int status;

    public ApiError(int status, String msg){
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
