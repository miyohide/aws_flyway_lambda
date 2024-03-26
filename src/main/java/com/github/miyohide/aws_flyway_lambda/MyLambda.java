package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyLambda implements RequestHandler<Input, String> {
  private final S3Client s3Client = S3Client.builder().region(Region.AP_NORTHEAST_1).build();

  @Override
  public String handleRequest(Input input, Context context) {
    LambdaLogger logger = context.getLogger();
    logger.log("Handler Start", LogLevel.INFO);
    // S3の操作
    S3Utils s3Utils = new S3Utils(s3Client);
    logger.log("S3Utils#copyFromS3bucket() start", LogLevel.DEBUG);
    s3Utils.copyFromS3bucket(input.getBucketName());

    // JDBCを使ってRDS（PostgreSQL）に接続する
    logger.log("Connect to the database", LogLevel.DEBUG);
    try (
      Connection c = DriverManager.getConnection(input.getJdbcURL(), input.getUserName(), input.getPassword());
      Statement st = c.createStatement();
    ) {
      logger.log(c.getCatalog(), LogLevel.INFO);
      st.execute("CREATE TABLE IF NOT EXISTS people (id BIGSERIAL PRIMARY KEY, name VARCHAR(100))");
      st.execute("INSERT INTO people (name) VALUES ('testpeople01')");
      st.execute("INSERT INTO people (name) VALUES ('testpeople02')");

      ResultSet rs = st.executeQuery("SELECT id, name FROM people");
      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        logger.log(String.format("id=%d, name=%s", id, name), LogLevel.INFO);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "success";
  }
}
