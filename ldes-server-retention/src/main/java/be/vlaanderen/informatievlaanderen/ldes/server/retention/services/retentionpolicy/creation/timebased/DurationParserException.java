package be.vlaanderen.informatievlaanderen.ldes.server.retention.services.retentionpolicy.creation.timebased;

public class DurationParserException extends RuntimeException {

	private final String toBeParsed;

	public DurationParserException(String toBeParsed) {
		super();
		this.toBeParsed = toBeParsed;
	}

	@Override
	public String getMessage() {
		return "String could not be parsed to duration or period: " + toBeParsed;
	}

}
