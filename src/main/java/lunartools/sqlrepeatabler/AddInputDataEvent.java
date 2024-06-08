package lunartools.sqlrepeatabler;

public class AddInputDataEvent {
	private String inputdata;

	public AddInputDataEvent(String inputdata) {
		this.inputdata=inputdata;
	}

	public String getInputdata() {
		return inputdata;
	}

}
