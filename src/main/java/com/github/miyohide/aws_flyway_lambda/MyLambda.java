package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.flywaydb.core.api.output.MigrateResult;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.nio.file.Path;

public class MyLambda implements RequestHandler<Input, Output> {
  private FlywayOperation flywayOperation;

  public MyLambda() {
    this.flywayOperation = new FlywayOperation();
  }

  @Override
  public Output handleRequest(Input input, Context context) {
    return migrateContents(input);
  }

  public Output migrateContents(Input input) {
    Output o = new Output();
    o.setInput(input);
    MigrateResult result = flywayOperation.runMigration(input);
    o.setSuccess(result.success);
    o.setOperation(result.getOperation());
    o.setInitialSchemaVersion(result.initialSchemaVersion);
    o.setTargetSchemaVersion(result.targetSchemaVersion);
    return o;
  }

  public String s3Objects(String bucketName) {
    S3Client s3 = S3Client.builder().region(Region.AP_NORTHEAST_3).build();

    ListObjectsV2Response listResponse = s3.listObjectsV2(builder -> builder.bucket(bucketName));
    StringBuilder result = new StringBuilder();

    for (S3Object object : listResponse.contents()) {
      result.append("File Name: ").append(object.key()).append(", File Size: ")
              .append(object.size()).append("\n");
    }
    return result.toString();
  }

  public void copyMigrationFiles(String bucketName) {
    String downloadPath = "/tmp/";

    S3Client s3 = S3Client.builder().region(Region.AP_NORTHEAST_3).build();
    ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
    ListObjectsV2Response listObjectsV2Response = s3.listObjectsV2(listObjectsV2Request);

    for (S3Object s3Object : listObjectsV2Response.contents()) {
      String objectKey = s3Object.key();
      String filePath = downloadPath + objectKey;

      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
              .bucket(bucketName)
              .key(objectKey)
              .build();

      s3.getObject(getObjectRequest, Path.of(filePath));
    }
    s3.close();
  }
}
