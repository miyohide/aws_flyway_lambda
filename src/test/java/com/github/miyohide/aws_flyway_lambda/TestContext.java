package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class TestContext implements Context {
  public TestContext() {}

  @Override
  public String getAwsRequestId() {
    return "aws_request_id";
  }

  @Override
  public String getLogGroupName() {
    return "aws_log_group_name";
  }

  @Override
  public String getLogStreamName() {
    return "aws_log_stream_name";
  }

  @Override
  public String getFunctionName() {
    return "aws_function_name";
  }

  @Override
  public String getFunctionVersion() {
    return "aws_function_version";
  }

  @Override
  public String getInvokedFunctionArn() {
    return "aws_invoked_function_arn";
  }

  @Override
  public CognitoIdentity getIdentity() {
    return null;
  }

  @Override
  public ClientContext getClientContext() {
    return null;
  }

  @Override
  public int getRemainingTimeInMillis() {
    return 0;
  }

  @Override
  public int getMemoryLimitInMB() {
    return 128;
  }

  @Override
  public LambdaLogger getLogger() {
    return null;
  }
}
