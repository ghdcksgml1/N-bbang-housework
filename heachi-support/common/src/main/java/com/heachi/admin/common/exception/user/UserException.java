package com.heachi.admin.common.exception.user;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class UserException extends HeachiException {
    public UserException(ExceptionMessage message) {
        super(message.getText());
    }
}
