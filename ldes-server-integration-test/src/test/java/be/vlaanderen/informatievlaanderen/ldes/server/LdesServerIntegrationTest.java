package be.vlaanderen.informatievlaanderen.ldes.server;

import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.repository.UnprocessedViewRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.ingest.postgres.repository.MemberEntityRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.PageRelationPostgresRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.repository.PageEntityRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.repository.PageRelationEntityRepository;
import io.cucumber.spring.CucumberContextConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import jakarta.persistence.EntityManager;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

@AutoConfigureObservability
@CucumberContextConfiguration
@EnableAutoConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES,
		replace = AutoConfigureEmbeddedDatabase.Replace.ANY)
@ActiveProfiles("postgres-test")
@ContextConfiguration(classes = {MemberEntityRepository.class, PageRelationPostgresRepository.class, PageRelationEntityRepository.class})
@ComponentScan(value = {"be.vlaanderen.informatievlaanderen.ldes.server"})
@TestPropertySource(properties = {
		"ldes-server.fragmentation-cron=*/1 * * * * *",
		"ldes-server.maintenance-cron=*/10 * * * * *",
		"ldes-server.compaction-duration=PT1S"
})
@SuppressWarnings("java:S2187")
public class LdesServerIntegrationTest {
	@Autowired
	MockMvc mockMvc;

	@Autowired
	EntityManager entityManager;
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	UnprocessedViewRepository unprocessedViewRepository;
	@Autowired
	PageRelationEntityRepository pageRelationEntityRepository;
	@Autowired
	PageEntityRepository pageEntityRepository;

	@Autowired
	DataSource dataSource;

	@Autowired
	JobExplorer jobExplorer;
}
