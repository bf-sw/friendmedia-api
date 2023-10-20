package org.product.exception;

import lombok.Data;
import org.product.common.ResponseStatus;

@Data
public class ApiException extends RuntimeException {

    private long status;

    public ApiException() { super(); }

    public ApiException(String message) {
        super(message);
        this.status = 400;
    }

    public ApiException(long status, String message) {
        super(message);
        this.status = status;
    }

    public ApiException(ResponseStatus response) {
        super(response.getMessage());
        this.status = response.getStatus();
    }
}
