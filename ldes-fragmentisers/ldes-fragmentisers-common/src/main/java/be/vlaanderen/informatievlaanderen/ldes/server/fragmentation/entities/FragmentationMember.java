package be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.entities;

import org.apache.jena.rdf.model.Model;

public record FragmentationMember(String id, Model model) {
}
