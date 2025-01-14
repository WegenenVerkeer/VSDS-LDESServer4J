package be.vlaanderen.informatievlaanderen.ldes.server.maintenance.repository;

import be.vlaanderen.informatievlaanderen.ldes.server.domain.model.ViewName;

import java.util.List;

public interface PageMemberRepository {
	void setPageMembersToNewPage(long newPageId, List<Long> pageIds);

	void deleteByViewNameAndMembersIds(ViewName viewName, List<Long> memberIds);
}
