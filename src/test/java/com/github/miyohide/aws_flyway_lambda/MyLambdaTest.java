package com.github.miyohide.aws_flyway_lambda;

import com.amazonaws.services.lambda.runtime.Context;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyLambdaTest {
  @Mock
  private FlywayOperation flywayOperation;
  @Mock
  private MigrateResult migrateResult;

  @InjectMocks
  private MyLambda myLambda;

  private Context createContext() {
    return new TestContext();
  }

  @Test
  void migrateContentsTest() {
    Input input = new Input("jdbcURL", "username", "password");

    doReturn(migrateResult).when(flywayOperation).runMigration(input);
    doReturn("migrate").when(migrateResult).getOperation();
    migrateResult.success = true;
    migrateResult.initialSchemaVersion = "10";
    migrateResult.targetSchemaVersion = "20";

    Output output = myLambda.migrateContents(input);
    assertTrue(output.isSuccess());
    assertEquals("10", output.getInitialSchemaVersion());
    assertEquals("20", output.getTargetSchemaVersion());
    assertEquals("migrate", output.getOperation());
  }
}
