package com.heachi.admin.common.exception.oauth;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class OAuthException extends HeachiException {

    public OAuthException(ExceptionMessage message) {
        super(message.getText());
    }
}
