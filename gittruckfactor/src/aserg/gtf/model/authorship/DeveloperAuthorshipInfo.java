package aserg.gtf.model.authorship;

import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
name = "developerauthorshipinfo",
uniqueConstraints = {@UniqueConstraint(columnNames = {"repositoryName", "userName"})}
)
public class DeveloperAuthorshipInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	private String repositoryName;
	private String userName;
	private int index;
	@ElementCollection
	private Set<String> fileNames;
	private Double focus;
	private int spread;
	private Double spreadNormalized;
	private int numFiles;
	public DeveloperAuthorshipInfo() {
		// TODO Auto-generated constructor stub
	}

	public DeveloperAuthorshipInfo(String userName, int index, Set<String> fileNames, String repositoryName) {
		super();
		this.userName = userName;
		this.index = index;
		this.fileNames = fileNames;
		this.repositoryName = repositoryName;
	}

	public String getUserName() {
		return userName;
	}

	public int getIndex() {
		return index;
	}

	public Set<String> getFileNames() {
		return fileNames;
	}

	public Double getFocus() {
		return focus;
	}

	public void setFocus(Double focus) {
		this.focus = focus;
	}

	public int getSpread() {
		return spread;
	}

	public void setSpread(int spread) {
		this.spread = spread;
	}

	public int getnFiles() {
		return fileNames.size();
	}
	
	public Long getId() {
		return id;
	}

	public int getNumFiles() {
		return numFiles;
	}

	public void setNumFiles(int numFiles) {
		this.numFiles = numFiles;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Double getSpreadNormalized() {
		return spreadNormalized;
	}

	public void setSpreadNormalized(Double spreadNormalized) {
		this.spreadNormalized = spreadNormalized;
	}

}