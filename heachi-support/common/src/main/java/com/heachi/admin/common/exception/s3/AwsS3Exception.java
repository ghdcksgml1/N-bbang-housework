package com.heachi.admin.common.exception.s3;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class AwsS3Exception extends HeachiException {
    public AwsS3Exception(ExceptionMessage message) {
        super(message.getText());
    }
}
