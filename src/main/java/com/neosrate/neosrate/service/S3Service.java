package com.neosrate.neosrate.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service

public class S3Service {
    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public void copyImage(String sourceKey, String destinationKey) {
        CopyObjectRequest copyObjRequest = new CopyObjectRequest(bucketName, sourceKey, bucketName, destinationKey);
        s3client.copyObject(copyObjRequest);
    }

    public boolean uploadFile(String keyName, MultipartFile file) throws IOException {
        if (!isAllowedFileType(file)) {
            return false;
        }

        var putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), null);
        return true;
    }

    public boolean uploadPicture(String keyName, MultipartFile file) throws IOException {
        if (!isAllowedPictureType(file)) {
            return false;
        }

        var putObjectResult = s3client.putObject(bucketName, keyName, file.getInputStream(), null);
        return true;
    }

    private boolean isAllowedFileType(MultipartFile file) {
        List<String> allowedContentTypes = Arrays.asList(
                "image/jpeg",
                "image/png",
                "image/gif",
                "video/mp4"
        );

        return allowedContentTypes.contains(file.getContentType());
    }

    private boolean isAllowedPictureType(MultipartFile file) {
        List<String> allowedContentTypes = Arrays.asList(
                "image/jpeg",
                "image/png"
        );

        return allowedContentTypes.contains(file.getContentType());
    }

    public void deleteFile(String keyName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
    }
}
