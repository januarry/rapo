package com.tms.flyway;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
@Component
public class FlywayRunner {

    @Autowired
    DataSource dataSource;
    @Bean
    public void FlywayDBMigrationRunner() {
            System.out.printf("----------Flyway Migration Start-----------");
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("db/migration").validateOnMigrate(false).load();
            flyway.migrate();
            System.out.printf("----------Flyway Migration End----------");
    }
}