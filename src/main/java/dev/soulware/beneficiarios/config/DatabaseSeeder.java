package dev.soulware.beneficiarios.config;

import dev.soulware.beneficiarios.infrastructure.persistence.jpa.repositories.BeneficiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final BeneficiaryRepository beneficiaryRepository;
    private final DataSource dataSource;

    @Value("classpath:data-init.sql")
    private Resource dataInitScript;

    public DatabaseSeeder(BeneficiaryRepository beneficiaryRepository, DataSource dataSource) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) {
        log.info("Checking if database seeding is required...");

        long count = beneficiaryRepository.count();

        if (count == 0) {
            log.info("Database is empty. Populating data from script...");
            try {
                ResourceDatabasePopulator populator = new ResourceDatabasePopulator(dataInitScript);
                populator.execute(dataSource);
                log.info("Data initialization completed successfully.");
            } catch (Exception e) {
                log.error("Failed to populate database with mock data: {}", e.getMessage());
            }
        } else {
            log.info("Database already contains {} records. Skipping data initialization.", count);
        }
    }
}