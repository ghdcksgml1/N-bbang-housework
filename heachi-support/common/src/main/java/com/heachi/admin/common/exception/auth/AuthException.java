package com.heachi.admin.common.exception.auth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class AuthException extends HeachiException {
    public AuthException(ExceptionMessage message) {
        super(message.getText());
    }
}
