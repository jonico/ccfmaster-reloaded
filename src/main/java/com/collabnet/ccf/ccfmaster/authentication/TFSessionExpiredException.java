package com.collabnet.ccf.ccfmaster.authentication;

import java.io.Serial;

public class TFSessionExpiredException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8573854072367732269L;

    public TFSessionExpiredException() {
    }

    public TFSessionExpiredException(String message) {
        super(message);
    }

    public TFSessionExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TFSessionExpiredException(Throwable cause) {
        super(cause);
    }

}
