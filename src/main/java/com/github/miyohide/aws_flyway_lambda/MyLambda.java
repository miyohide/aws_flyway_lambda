package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class MyLambda implements RequestHandler<Input, String> {
  private final S3Client s3Client = S3Client.builder().region(Region.AP_NORTHEAST_1).build();

  @Override
  public String handleRequest(Input input, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log("Handler Start", LogLevel.INFO);
    S3Utils s3Utils = new S3Utils(s3Client);
    logger.log("S3Utils#copyFromS3bucket() start", LogLevel.DEBUG);
    s3Utils.copyFromS3bucket(input.getBucketName());
    // TODO /tmp 以下のファイル一覧を出力させたい
    logger.log("S3Utils#deleteTmp() start", LogLevel.DEBUG);
    s3Utils.deleteTmp();
    return "success";
  }
}
