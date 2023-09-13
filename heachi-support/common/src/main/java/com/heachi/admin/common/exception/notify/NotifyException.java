package com.heachi.admin.common.exception.notify;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class NotifyException extends HeachiException {
    public NotifyException(ExceptionMessage message) {
        super(message.getText());
    }
}
