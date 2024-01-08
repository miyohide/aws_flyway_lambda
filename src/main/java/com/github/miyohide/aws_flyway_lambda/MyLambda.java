package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

public class MyLambda implements RequestHandler<Input, Output> {
  private final S3Client s3Client;
  private final Logger log = LogManager.getLogger(MyLambda.class);
  private FlywayOperation flywayOperation;

  public MyLambda() {
    this.s3Client = S3Client.builder().region(Region.AP_NORTHEAST_1).build();
    this.flywayOperation = new FlywayOperation();
  }

  @Override
  public Output handleRequest(Input input, Context context) {
    MyLambda myLambda = new MyLambda();
    myLambda.copyFromS3bucket(input.getBucketName());
    return migrateContents(input);
  }

  public Output migrateContents(Input input) {
    Output o = new Output();
    o.setInput(input);
    flywayOperation.runQuery(input);
    //    MigrateResult result = flywayOperation.runMigration(input);
    //    o.setSuccess(result.success);
    //    o.setOperation(result.getOperation());
    //    o.setInitialSchemaVersion(result.initialSchemaVersion);
    //    o.setTargetSchemaVersion(result.targetSchemaVersion);
    return o;
  }

  /**
   * 指定されたS3 bucketにあるファイルすべてを/tmpにコピーする
   *
   * @param bucketName コピー元のS3 bucket名
   */
  private void copyFromS3bucket(String bucketName) {
    // ウォームスタートでは/tmpが残っているので誤作動を防ぐために削除する
    deleteTmp();
    ListObjectsV2Response listResponse =
        s3Client.listObjectsV2(builder -> builder.bucket(bucketName));

    for (S3Object object : listResponse.contents()) {
      GetObjectRequest objectRequest =
          GetObjectRequest.builder().key(object.key()).bucket(bucketName).build();
      saveFileToTmp(objectRequest);
    }
  }

  /** /tmp以下のファイルを全て削除する */
  private void deleteTmp() {
    Arrays.stream(Objects.requireNonNull(new File("/tmp").listFiles()))
        .filter(Predicate.not(File::isDirectory))
        .forEach(File::delete);
  }

  /**
   * 指定したkey(filename)を/tmpにコピーする
   *
   * @param objectRequest 対象のGetObjectRequestのインスタンス
   */
  private void saveFileToTmp(GetObjectRequest objectRequest) {
    ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
    byte[] data = objectBytes.asByteArray();
    File myFile = new File(Paths.get("/tmp", objectRequest.key()).toString());
    try (OutputStream os = new FileOutputStream(myFile)) {
      os.write(data);
    } catch (IOException ex) {
      log.warn(ex.getMessage());
    }
  }
}
