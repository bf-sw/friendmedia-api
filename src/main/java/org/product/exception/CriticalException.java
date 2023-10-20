package org.product.exception;

import lombok.Data;
import org.product.common.ResponseStatus;

@Data
public class CriticalException extends RuntimeException {

    private long status;
    private String title;

    public CriticalException() {
        super();
    }

    public CriticalException(String message, String title) {
        super(message);
        this.status = 500;
        this.title = title;
    }

    public CriticalException(long status, String message, String title) {
        super(message);
        this.status = status;
        this.title = title;
    }

    public CriticalException(ResponseStatus response, String title) {
        super(response.getMessage());
        this.status = response.getStatus();
        this.title = title;
    }
}
