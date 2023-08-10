package com.heachi.admin.common.exception.jwt;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;
public class JwtException extends HeachiException {

    public JwtException(ExceptionMessage message) {
        super(message.getText());
    }
}
