package com.heachi.admin.common.exception.housework;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class HouseworkException extends HeachiException {
    public HouseworkException(ExceptionMessage message) {
        super(message.getText());
    }
}
