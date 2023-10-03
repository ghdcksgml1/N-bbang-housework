package com.heachi.admin.common.exception.s3;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.HeachiException;

public class S3FileSizeLimitExceededException extends HeachiException {
    public S3FileSizeLimitExceededException(ExceptionMessage message) {
        super(message.getText());
    }
}
