package com.heachi.s3.api.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.s3.AwsS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    @Value("${cloud.aws.region.static}")
    public String region;

    public String uploadImage(MultipartFile multipartFile) {
        String url = "https://s3." + region + ".amazonaws.com/" + bucket + "/";

        // 파일의 크기가 10MB가 넘어가면 Exception 발생
        if (multipartFile.getSize() > 1024 * 1024 * 10) {
            log.warn(">>>> 사진의 용량이 10MB를 초과합니다.");

            throw new AwsS3Exception(ExceptionMessage.S3_FILE_SIZE_LIMIT_EXCEEDED);
        }

        String fileName = generateFileName((multipartFile.getOriginalFilename()));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), objectMetadata);

            return url + fileName;
        } catch (IOException e) {
            log.warn(">>>> AWS S3 파일 업로드에 실패했습니다.");

            throw new AwsS3Exception(ExceptionMessage.S3_FILE_UPLOAD_FAILED);
        }
    }

    public boolean deleteImage(String fileName) {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));

            return true;
        } catch (AmazonClientException e) {
            log.warn(">>>> 이미지 삭제에 실패했습니다.");

            return false;
        }
    }

    private String generateFileName(String fileName) {

        return UUID.randomUUID().toString() + getFileExtension(fileName);
    }

    private String getFileExtension(String fileName) {
        try {

            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            log.warn(">>>> 잘못된 확장자명입니다.");

            throw new AwsS3Exception(ExceptionMessage.S3_MALFORMED_FILE);
        }
    }
}
