package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLambdaTest {

  private Context createContext() {
    return new TestContext();
  }

  @Test
  void handleRequest() {
    MyLambda myLambda = new MyLambda();

    Input input = new Input("jdbcURL", "username", "password");
    Output output = myLambda.handleRequest(input, createContext());
    assertEquals(false, output.isSuccess());
  }
}