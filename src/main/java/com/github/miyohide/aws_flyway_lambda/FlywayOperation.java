package com.github.miyohide.aws_flyway_lambda;

import java.sql.*;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;

public class FlywayOperation {
  public MigrateResult runMigration(Input input) {
    Flyway flyway =
        Flyway.configure()
            .dataSource(input.getJdbcURL(), input.getUserName(), input.getPassword())
            .locations("/tmp")
            .load();
    return flyway.migrate();
  }

  public String runQuery(Input input) {
    String sql = "SELECT 1";
    StringBuilder sb = new StringBuilder();

    try (Connection con =
            DriverManager.getConnection(
                input.getJdbcURL(), input.getUserName(), input.getPassword());
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(sql); ) {
      while (rs.next()) {
        sb.append(rs.getString(1));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return sb.toString();
  }
}
