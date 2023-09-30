package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.github.miyohide.aws_flyway_lambda.MyLambda.Input;
import com.github.miyohide.aws_flyway_lambda.MyLambda.Output;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class MyLambda implements RequestHandler<Input, Output> {
  @Override
  public Output handleRequest(Input input, Context context) {
    final Output out = new Output();
    out.in = input;

    Flyway flyway = Flyway.configure()
            .dataSource(input.jdbcURL, input.userName, input.password).load();
    MigrateResult result = flyway.migrate();

    out.operation = result.getOperation();

    return out;
  }

  public String migrateContents(Input input) {
    return "";
  }

  public static class Input {
    public String bucket;
    public String key;
    public String jdbcURL;
    public String userName;
    public String password;
  }

  public static class Output {
    public Input in;
    public String operation;
  }
}
