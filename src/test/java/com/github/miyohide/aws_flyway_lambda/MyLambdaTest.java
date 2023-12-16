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
    Input input = new Input("jdbcURL", "username", "password", "bucketName");

    // Mockとして返す値の設定
    String dummyOperationName = "migrate";
    String dummyInitialSchemaVersion = "10";
    String dummyTargetSchemaVersion = "20";
    doReturn(migrateResult).when(flywayOperation).runMigration(input);
    doReturn(dummyOperationName).when(migrateResult).getOperation();
    migrateResult.success = true;
    migrateResult.initialSchemaVersion = dummyInitialSchemaVersion;
    migrateResult.targetSchemaVersion = dummyTargetSchemaVersion;

    // 実行して値が想定通りであることを確認する
    Output output = myLambda.migrateContents(input);
    assertTrue(output.isSuccess());
    assertEquals(dummyInitialSchemaVersion, output.getInitialSchemaVersion());
    assertEquals(dummyTargetSchemaVersion, output.getTargetSchemaVersion());
    assertEquals(dummyOperationName, output.getOperation());
  }

  @Test
  void handleRequestTest() {
    Input input = new Input("jdbcURL", "username", "password", "bucketName");
    Context context = createContext();

    // Mockとして返す値の設定
    String dummyOperationName = "migrate";
    String dummyInitialSchemaVersion = "10";
    String dummyTargetSchemaVersion = "20";
    doReturn(migrateResult).when(flywayOperation).runMigration(input);
    doReturn(dummyOperationName).when(migrateResult).getOperation();
    migrateResult.success = true;
    migrateResult.initialSchemaVersion = dummyInitialSchemaVersion;
    migrateResult.targetSchemaVersion = dummyTargetSchemaVersion;

    // 実行して値が想定通りであることを確認する
    Output output = myLambda.handleRequest(input, context);
    assertTrue(output.isSuccess());
    assertEquals(dummyInitialSchemaVersion, output.getInitialSchemaVersion());
    assertEquals(dummyTargetSchemaVersion, output.getTargetSchemaVersion());
    assertEquals(dummyOperationName, output.getOperation());
  }
}
