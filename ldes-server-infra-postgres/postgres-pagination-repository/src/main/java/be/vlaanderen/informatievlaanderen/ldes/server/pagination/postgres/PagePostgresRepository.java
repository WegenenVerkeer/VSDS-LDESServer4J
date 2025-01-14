package be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres;

import be.vlaanderen.informatievlaanderen.ldes.server.pagination.entities.Page;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.batch.PaginationRowMapper;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.entity.PageEntity;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.postgres.repository.PageEntityRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.repositories.PageRelationRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.pagination.repositories.PageRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PagePostgresRepository implements PageRepository {
	private final JdbcTemplate jdbcTemplate;
	private final PageEntityRepository pageEntityRepository;
	private final PageRelationRepository pageRelationRepository;

	public PagePostgresRepository(JdbcTemplate jdbcTemplate, PageEntityRepository pageEntityRepository, PageRelationRepository pageRelationRepository) {
		this.jdbcTemplate = jdbcTemplate;
		this.pageEntityRepository = pageEntityRepository;
		this.pageRelationRepository = pageRelationRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public Page getOpenPage(long bucketId) {
		String sql = """
				select page_id, bucket_id, partial_url, page_size, assigned_members
				from open_pages
				where bucket_id = ?
				""";
		return jdbcTemplate.query(sql, new PaginationRowMapper(), bucketId)
				.stream()
				.findFirst()
				.orElseThrow();
	}

	@Override
	@Transactional
	public Page createNextPage(Page parentPage) {
		String partialUrl = parentPage.createChildPartialUrl().asString();
		PageEntity newPage = new PageEntity(parentPage.getBucketId(), partialUrl);

		pageEntityRepository.save(newPage);
		pageRelationRepository.insertGenericBucketRelation(parentPage.getId(), newPage.getId());
		pageEntityRepository.setPageImmutable(parentPage.getId());

		return new Page(newPage.getId(), parentPage.getBucketId(), partialUrl, parentPage.getPageSize());
	}

	@Override
	@Transactional
	public void markAllPagesImmutableByCollectionName(String collectionName) {
		pageEntityRepository.markAllPagesImmutableByCollectionName(collectionName);
	}
}
