package be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset2;

import be.vlaanderen.informatievlaanderen.ldes.server.domain.converter.LocalDateTimeConverter;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset2.config.AppConfigChangeset2;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset2.config.LdesConfig;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset2.entities.LdesMemberEntityV2;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset2.entities.LdesMemberEntityV3;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.impl.LiteralImpl;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFParserBuilder;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;

import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

@ChangeUnit(id = "member-updater-changeset-2", order = "2023-04-03 00:00:00", author = "VSDS")
public class MemberUpdaterChange {

	private final MongoTemplate mongoTemplate;
	private final String timeStampPath;
	private final String versionOfPath;
	private final LocalDateTimeConverter localDateTimeConverter = new LocalDateTimeConverter();

	public MemberUpdaterChange(MongoTemplate mongoTemplate, AppConfigChangeset2 appConfigChangeset2) {
		this.mongoTemplate = mongoTemplate;

		if (appConfigChangeset2.getCollections() != null && appConfigChangeset2.getCollections().size() == 1) {
			LdesConfig ldesConfig = appConfigChangeset2.getCollections().get(0);
			timeStampPath = ldesConfig.getTimestampPath();
			versionOfPath = ldesConfig.getVersionOfPath();
		} else {
			timeStampPath = "";
			versionOfPath = "";
		}
	}

	/**
	 * This is the method with the migration code
	 **/
	@Execution
	public void changeSet() {
		mongoTemplate.stream(new Query(), LdesMemberEntityV2.class).forEach(ldesMember -> {
			Model ldesMemberModel = RDFParserBuilder.create().fromString(ldesMember.getModel()).lang(Lang.NQUADS)
					.toModel();
			String versionOf = extractVersionOf(ldesMemberModel);
			LocalDateTime timestamp = extractTimestamp(ldesMemberModel);

			mongoTemplate
					.save(new LdesMemberEntityV3(ldesMember.getId(), versionOf, timestamp, ldesMember.getModel(),
							ldesMember.getTreeNodeReferences()));
		});
	}

	@RollbackExecution
	public void rollback() {
		mongoTemplate.stream(new Query(), LdesMemberEntityV3.class).forEach(ldesMember -> mongoTemplate
				.save(new LdesMemberEntityV2(ldesMember.getId(), ldesMember.getModel(),
						ldesMember.getTreeNodeReferences())));
	}

	private LocalDateTime extractTimestamp(Model memberModel) {
		LiteralImpl literalImpl = memberModel
				.listStatements(null, createProperty(timeStampPath), (RDFNode) null)
				.nextOptional()
				.map(statement -> (LiteralImpl) statement.getObject())
				.orElse(null);
		if (literalImpl == null) {
			return null;
		}
		return localDateTimeConverter.getLocalDateTime(literalImpl);

	}

	private String extractVersionOf(Model memberModel) {
		return memberModel
				.listStatements(null, createProperty(versionOfPath), (RDFNode) null)
				.nextOptional()
				.map(statement -> statement.getObject().toString())
				.orElse(null);
	}
}
