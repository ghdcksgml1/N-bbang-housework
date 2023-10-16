package com.heachi.admin.common.exception.group.member;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class GroupMemberException extends HeachiException {
    public GroupMemberException(ExceptionMessage message) {
        super(message.getText());
    }
}
