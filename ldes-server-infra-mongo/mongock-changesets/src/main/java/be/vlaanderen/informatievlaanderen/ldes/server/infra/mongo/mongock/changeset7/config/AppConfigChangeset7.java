package be.vlaanderen.informatievlaanderen.ldes.server.infra.mongo.mongock.changeset7.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties
@Configuration
public class AppConfigChangeset7 {
	private List<LdesConfig> collections;

	public AppConfigChangeset7(List<LdesConfig> collections) {
		this.collections = collections != null ? collections : new ArrayList<>();
	}

	public AppConfigChangeset7() {
	}

	public List<LdesConfig> getCollections() {
		return collections;
	}

	public void setCollections(List<LdesConfig> collections) {
		this.collections = collections != null ? collections : new ArrayList<>();
	}

	public LdesConfig getLdesConfig(String collectionName) {
		return getCollections()
				.stream()
				.filter(ldes -> ldes.getCollectionName().equals(collectionName))
				.findFirst()
				.orElse(new LdesConfig());
	}

}
