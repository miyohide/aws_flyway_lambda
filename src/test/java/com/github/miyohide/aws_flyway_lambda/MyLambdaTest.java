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
    MyLambda.Input input = new MyLambda.Input();
    input.lastName = "lastname";
    input.firstName = "firstname";
    MyLambda.Output output = myLambda.handleRequest(input, createContext());
    assertEquals("firstname_lastname", output.fullName);
  }
}