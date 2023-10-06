package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.flywaydb.core.api.output.MigrateResult;

public class MyLambda implements RequestHandler<Input, Output> {
  private FlywayOperation flywayOperation = new FlywayOperation();
  @Override
  public Output handleRequest(Input input, Context context) {
    return migrateContents(input);
  }

  public Output migrateContents(Input input) {
    Output o = new Output();
    o.setInput(input);
    MigrateResult result = flywayOperation.runMigration(input);
    o.setSuccess(result.success);
    o.setOperation(result.getOperation());
    o.setInitialSchemaVersion(result.initialSchemaVersion);
    o.setTargetSchemaVersion(result.targetSchemaVersion);
    return o;
  }

}
