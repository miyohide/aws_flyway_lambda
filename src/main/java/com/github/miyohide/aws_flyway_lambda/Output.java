package com.github.miyohide.aws_flyway_lambda;

public class Output {
  private Input input;
  private String operation;
  private boolean success;
  private String initialSchemaVersion;
  private String targetSchemaVersion;

  public Input getInput() {
    return input;
  }

  public void setInput(Input input) {
    this.input = input;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getInitialSchemaVersion() {
    return initialSchemaVersion;
  }

  public void setInitialSchemaVersion(String initialSchemaVersion) {
    this.initialSchemaVersion = initialSchemaVersion;
  }

  public String getTargetSchemaVersion() {
    return targetSchemaVersion;
  }

  public void setTargetSchemaVersion(String targetSchemaVersion) {
    this.targetSchemaVersion = targetSchemaVersion;
  }
}
