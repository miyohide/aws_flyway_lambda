package com.github.miyohide.aws_flyway_lambda;

public class Input {
  private String jdbcURL;
  private String userName;
  private String password;
  private String bucketName;

  public Input() {
  }

  public Input(String jdbcURL, String userName, String password, String bucketName) {
    this.jdbcURL = jdbcURL;
    this.userName = userName;
    this.password = password;
    this.bucketName = bucketName;
  }

  public void setJdbcURL(String jdbcURL) {
    this.jdbcURL = jdbcURL;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setBucketName(String bucketName) {
    this.bucketName = bucketName;
  }

  public String getJdbcURL() {
    return jdbcURL;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getBucketName() {
    return bucketName;
  }

  @Override
  public String toString() {
    return "Input{" +
            "jdbcURL='" + jdbcURL + '\'' +
            ", userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            ", bucketName='" + bucketName + '\'' +
            '}';
  }
}
