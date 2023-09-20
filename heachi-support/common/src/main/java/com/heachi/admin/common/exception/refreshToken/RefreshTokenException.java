package com.heachi.admin.common.exception.refreshToken;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class RefreshTokenException extends HeachiException {
    public RefreshTokenException(ExceptionMessage message) {
        super(message.getText());
    }
}
