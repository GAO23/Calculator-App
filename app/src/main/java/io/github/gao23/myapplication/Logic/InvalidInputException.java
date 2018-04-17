package io.github.gao23.myapplication.Logic;

/**
 * Created by GAO on 5/30/2017.
 */

public class InvalidInputException extends Exception {
    private int errorCode;
    public InvalidInputException(int errorCode){
        this.errorCode = errorCode;
    }
    public int getErrorCode(){
        return errorCode;
    }
}
