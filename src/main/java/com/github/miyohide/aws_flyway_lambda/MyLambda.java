package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.flywaydb.core.api.output.MigrateResult;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

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
}
