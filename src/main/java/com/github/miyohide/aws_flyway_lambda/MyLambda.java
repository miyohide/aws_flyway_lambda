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
    return migrateContents(input);
  }

  public Output migrateContents(Input input) {
    Output o = new Output();
    o.input = input;
    Flyway flyway = Flyway.configure()
            .dataSource(input.jdbcURL, input.userName, input.password).load();
    MigrateResult result = flyway.migrate();
    o.success = result.success;
    o.operation = result.getOperation();
    o.initialSchemaVersion = result.initialSchemaVersion;
    o.targetSchemaVersion = result.targetSchemaVersion;
    return o;
  }

  public static class Input {
    public String jdbcURL;
    public String userName;
    public String password;
  }

  public static class Output {
    public Input input;
    public String operation;
    public boolean success;
    public String initialSchemaVersion;
    public String targetSchemaVersion;
  }
}
