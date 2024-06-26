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

import org.flywaydb.core.Flyway;

import java.sql.PreparedStatement;

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

    // Flywayを使ってDatabase Migrationを実行する
    logger.log("Flyway#migrate() start", LogLevel.INFO);
    Flyway flyway = Flyway.configure()
    .dataSource(input.getJdbcURL(), input.getUserName(), input.getPassword())
    .locations("/tmp/sql")
    .load();

    flyway.migrate();
    // JDBCを使ってRDS（PostgreSQL）にpeopleテーブルを作成する
    // logger.log("Connect to the database", LogLevel.INFO);
    // try (
    //   Connection c = DriverManager.getConnection(input.getJdbcURL(), input.getUserName(), input.getPassword());
    //   Statement st = c.createStatement();
    // ) {
    //   logger.log("Create people table", LogLevel.INFO);
    //   c.setAutoCommit(false);
    //   st.execute("CREATE TABLE IF NOT EXISTS people (id BIGSERIAL PRIMARY KEY, name VARCHAR(100))");
    //   c.commit();
    // } catch (SQLException e) {
    //   e.printStackTrace();
    //   return "fail";
    // }

    // JDBCを使ってRDS（PostgreSQL）のpeopleテーブルにデータを挿入し、結果を参照する
    try (
      Connection c = DriverManager.getConnection(input.getJdbcURL(), input.getUserName(), input.getPassword());
      PreparedStatement insertSt = c.prepareStatement("INSERT INTO people (name) VALUES (?)");
      PreparedStatement st = c.prepareStatement("SELECT id, name FROM people");
    ) {
      logger.log("insert data to people table", LogLevel.INFO);
      c.setAutoCommit(false);
      insertSt.setString(1, "testpeople01");
      insertSt.execute();
      insertSt.setString(1, "testpeople02");
      insertSt.execute();
      c.commit();

      logger.log("select people table", LogLevel.INFO);
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        logger.log(String.format("id=%d, name=%s", id, name), LogLevel.INFO);
      }
      rs.close();
    } catch (SQLException e) {
      e.printStackTrace();
      return "fail";
    }
    return "success";
  }
}
