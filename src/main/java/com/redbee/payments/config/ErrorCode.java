package com.redbee.payments.config;

public enum ErrorCode {

    INTERNAL_ERROR(100, "Internal server error"),
    BAD_REQUEST(101, "Bad request"),
    RESOURCE_NOT_FOUND(102, "Resource not found"),
    ACCOUNT_NOT_AVAILABLE(103, "Another operation is already in progress"),
    INSUFFICIENT_FUNDS(104, "Not enough funds for the transfer"),
    DEPOSIT_FAILED(105, "Could not transfer the money"),
    SAME_ACCOUNTS(106, "Source and target accounts cannot be the same");

    private final int value;
    private final String reasonPhrase;

    ErrorCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

}
