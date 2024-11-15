package be.vlaanderen.informatievlaanderen.ldes.server.rest.caching;

import be.vlaanderen.informatievlaanderen.ldes.server.fetching.entities.Member;
import be.vlaanderen.informatievlaanderen.ldes.server.fetching.entities.TreeNode;
import be.vlaanderen.informatievlaanderen.ldes.server.fetching.valueobjects.LdesFragmentIdentifier;
import be.vlaanderen.informatievlaanderen.ldes.server.fetching.valueobjects.TreeRelation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static be.vlaanderen.informatievlaanderen.ldes.server.domain.constants.ServerConfig.HOST_NAME_KEY;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Component
public class EtagCachingStrategy implements CachingStrategy {

	private final String hostName;

	public EtagCachingStrategy(@Value(HOST_NAME_KEY) String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String generateCacheIdentifier(String collectionName, String language) {
		return sha256Hex(hostName + "/" + collectionName + "?lang=" + language);
	}

	@Override
	public String generateCacheIdentifier(TreeNode treeNode, String language) {
		return sha256Hex(treeNode.getFragmentId()
				+ treeNode.getRelations().stream()
						.map(TreeRelation::treeNode)
						.map(LdesFragmentIdentifier::asDecodedFragmentId)
						.collect(Collectors.joining(""))
				+ treeNode.getMembers().stream()
						.map(Member::subject)
						.collect(Collectors.joining(""))
				+ language);
	}
}