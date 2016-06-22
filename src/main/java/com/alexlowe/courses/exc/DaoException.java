package com.alexlowe.courses.exc;

/**
 * Created by Keyes on 6/20/2016.
 */
public class  DaoException extends Exception {
    private final Exception originalException;

    public DaoException(Exception orginalException, String msg){
        super(msg);
        this.originalException = orginalException;
    }
}
