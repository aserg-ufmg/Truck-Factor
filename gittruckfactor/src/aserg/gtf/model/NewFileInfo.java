package aserg.gtf.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Index;

@Entity
@Table(
name = "NEWFILEINFO",
uniqueConstraints = {@UniqueConstraint(columnNames = {"repositoryName", "path"})}
)
public class NewFileInfo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	@Index(name="NEWFILEPATHINDEX")
	private String path;
	private Boolean filtered;
	private String filterInfo;
	private int size;
	@Enumerated(EnumType.STRING)
	private FileType kind;
	private String language;

	private String module = "NONE";

	
	
	@Index(name="NEWFILEREPOSITORYNAMEINDEX")
	private String repositoryName;
	
	public NewFileInfo() {
		// TODO Auto-generated constructor stub
	}

	public NewFileInfo(String repositoryName, String path) {
		this.repositoryName = repositoryName;
		this.path = path;
		this.filtered = false;		
	}
	public NewFileInfo(String repositoryName, String path, String language) {
		this.repositoryName = repositoryName;
		this.path = path;
		this.filtered = false;		
		this.language = language;
	}
	
//	public NewFileInfo(String repositoryName, String path, FileType kind, String language) {
//		super();
//		this.repositoryName = repositoryName;
//		this.path = path;
//		this.filtered = false;
//		this.filterInfo = new String();
//		this.kind = kind;
//		this.setLanguage(language);
//	}
	
	public Long getId() {
		return id;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	public Boolean getFiltered() {
		return filtered;
	}

	public void setFiltered(Boolean filtered) {
		this.filtered = filtered;
	}

	public String getFilterInfo() {
		return filterInfo;
	}

	public void setFilterInfo(String filterInfo) {
		this.filterInfo = filterInfo;
	}

	public FileType getKind() {
		return kind;
	}

	public void setKind(FileType kind) {
		this.kind = kind;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return this.path + "("+this.filtered+")";
	}
	
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

}
