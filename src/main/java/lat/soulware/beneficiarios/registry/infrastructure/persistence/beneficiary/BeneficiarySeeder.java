package lat.soulware.beneficiarios.registry.infrastructure.persistence.beneficiary;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

/**
 * Fills an empty registry with the beneficiaries in {@code seed/beneficiaries.sql} at startup. A
 * registry holding anyone at all is left as it is, so the seed loads once per database.
 */
@Component
public class BeneficiarySeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(BeneficiarySeeder.class);

    private final JdbcTemplate jdbc;
    private final DataSource dataSource;
    private final Resource seed;

    public BeneficiarySeeder(
        JdbcTemplate jdbc,
        DataSource dataSource,
        @Value("classpath:seed/beneficiaries.sql") Resource seed
    ) {
        this.jdbc = jdbc;
        this.dataSource = dataSource;
        this.seed = seed;
    }

    @Override
    public void run(String... args) {
        Long held = this.jdbc.queryForObject("SELECT count(*) FROM beneficiaries", Long.class);
        boolean isEmpty = held == null || held == 0;
        if (!isEmpty) {
            log.info("Registry holds {} beneficiaries, seed skipped", held);
            return;
        }

        new ResourceDatabasePopulator(this.seed).execute(this.dataSource);
        log.info("Registry seeded from {}", this.seed.getFilename());
    }
}
