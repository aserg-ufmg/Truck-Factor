package aserg.gtf.util;

import java.util.List;

public class LineInfo{
	private String repositoryName;
	private List<String> values;
	
	public LineInfo(String repositoryName, List<String> values) {
		super();
		this.repositoryName = repositoryName;
		this.values = values;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public List<String> getValues() {
		return values;
	}		
}