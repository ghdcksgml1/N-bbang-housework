package com.heachi.admin.common.exception.item;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class ItemException extends HeachiException {
    public ItemException(ExceptionMessage message) {
        super(message.getText());
    }
}
