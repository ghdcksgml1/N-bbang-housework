package com.heachi.admin.common.exception.group.info;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class GroupInfoException extends HeachiException {
    public GroupInfoException(ExceptionMessage message) {
        super(message.getText());
    }
}
