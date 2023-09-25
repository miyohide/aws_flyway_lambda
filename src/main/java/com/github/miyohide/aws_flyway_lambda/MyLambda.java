package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.github.miyohide.aws_flyway_lambda.MyLambda.Input;
import com.github.miyohide.aws_flyway_lambda.MyLambda.Output;

public class MyLambda implements RequestHandler<Input, Output> {
  @Override
  public Output handleRequest(Input input, Context context) {
    final Output out = new Output();
    out.in = input;
    out.fullName = input.firstName + "_" + input.lastName;

    return out;
  }

  public static class Input {
    public String firstName;
    public String lastName;
  }

  public static class Output {
    public Input in;
    public String fullName;
  }
}
