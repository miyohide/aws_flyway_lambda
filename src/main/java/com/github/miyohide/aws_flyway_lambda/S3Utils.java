package com.github.miyohide.aws_flyway_lambda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public class S3Utils {
  private final S3Client s3Client;
  private final Logger log = LoggerFactory.getLogger(S3Utils.class);

  public S3Utils(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  /**
   * 指定されたS3 bucketにあるファイルすべてを/tmpにコピーする
   *
   * @param bucketName コピー元のS3 bucket名
   */
  public void copyFromS3bucket(String bucketName) {
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
  public void deleteTmp() {
    Arrays.stream(Objects.requireNonNull(new File("/tmp").listFiles()))
            .filter(Predicate.not(File::isDirectory))
            .forEach(File::delete);
  }

  /**
   * 指定したkey(filename)を/tmpにコピーする
   *
   * @param objectRequest 対象のGetObjectRequestのインスタンス
   */
  public void saveFileToTmp(GetObjectRequest objectRequest) {
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
