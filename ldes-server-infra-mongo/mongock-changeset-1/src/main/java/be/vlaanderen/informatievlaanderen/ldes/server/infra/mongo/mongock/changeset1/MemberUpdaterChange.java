package be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset1;

import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset1.entities.LdesFragmentEntityV1;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset1.entities.LdesMemberEntityV1;
import be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset1.entities.LdesMemberEntityV2;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@ChangeUnit(id = "member-updater-changeset-1", order = "1", author = "VSDS")
public class MemberUpdaterChange {
	private static final String MEMBERS = "members";
	private final MongoTemplate mongoTemplate;

	public MemberUpdaterChange(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * This is the method with the migration code
	 **/
	@Execution
	public void changeSet() {
		List<LdesMemberEntityV1> ldesMemberEntityV1s = mongoTemplate.find(new Query(), LdesMemberEntityV1.class);
		ldesMemberEntityV1s.forEach(ldesMember -> {
			Query query = new Query();
			query.addCriteria(Criteria.where(MEMBERS).is(ldesMember.getId()));
			List<String> treeNodeReferences = mongoTemplate.find(query, LdesFragmentEntityV1.class).stream()
					.map(LdesFragmentEntityV1::getId).toList();
			mongoTemplate
					.save(new LdesMemberEntityV2(ldesMember.getId(), ldesMember.getLdesMember(), treeNodeReferences));
		});
	}

	@RollbackExecution
	public void rollback() {
		List<LdesMemberEntityV2> ldesMemberEntities = mongoTemplate.find(new Query(), LdesMemberEntityV2.class);
		ldesMemberEntities.forEach(ldesMember -> mongoTemplate
				.save(new LdesMemberEntityV1(ldesMember.getId(), ldesMember.getModel())));
	}
}