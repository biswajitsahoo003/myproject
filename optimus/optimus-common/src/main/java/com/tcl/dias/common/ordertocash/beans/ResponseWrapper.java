package com.tcl.dias.common.ordertocash.beans;

/**
 * Generic class to map success and failure response.
 *
 * @param <T>
 * @param <U>
 * @author VISHESH AWASTHI
 */
public class ResponseWrapper<T, U> {
    private Boolean isSucess;
    private T successRespose;
    private U failureResponse;

    public Boolean getIsSucess() {
        return isSucess;
    }

    public void setIsSucess(Boolean isSucess) {
        this.isSucess = isSucess;
    }

    public T getSuccessRespose() {
        return successRespose;
    }

    public void setSuccessRespose(T successRespose) {
        this.successRespose = successRespose;
    }

    public U getFailureResponse() {
        return failureResponse;
    }

    public void setFailureResponse(U failureResponse) {
        this.failureResponse = failureResponse;
    }
}
