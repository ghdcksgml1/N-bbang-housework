package com.heachi.admin.common.exception.state;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class LoginStateException extends HeachiException {
    public LoginStateException(ExceptionMessage message) {
        super(message.getText());
    }
}
