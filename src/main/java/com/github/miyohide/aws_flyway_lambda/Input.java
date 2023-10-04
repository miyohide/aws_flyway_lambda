package com.github.miyohide.aws_flyway_lambda;

public class Input {
  private final String jdbcURL;
  private final String userName;
  private final String password;

  public Input(String jdbcURL, String userName, String password) {
    this.jdbcURL = jdbcURL;
    this.userName = userName;
    this.password = password;
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
}
