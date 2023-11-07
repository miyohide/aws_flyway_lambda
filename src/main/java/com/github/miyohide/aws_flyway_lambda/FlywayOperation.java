package com.github.miyohide.aws_flyway_lambda;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class FlywayOperation {
  public MigrateResult runMigration(Input input) {
    Flyway flyway = Flyway.configure()
            .dataSource(input.getJdbcURL(), input.getUserName(), input.getPassword())
            .locations("/tmp")
            .load();
    return flyway.migrate();
  }
}
