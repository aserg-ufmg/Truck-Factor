package aserg.gtf.model.authorship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;


@Entity
public class Repository {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column(unique=true)
	private String fullName;
	
	@OneToMany(cascade = { CascadeType.ALL })
	private List<File> files;
	@OneToMany(cascade = { CascadeType.REFRESH })
	private List<Developer> developers = new ArrayList<Developer>();
//	@OneToMany(cascade = { CascadeType.ALL })
//	private List<AuthorshipInfo> authorships = new ArrayList<AuthorshipInfo>();;
	
	

//	@javax.persistence.OneToMany(cascade = CascadeType.ALL)
//	@javax.persistence.MapKey(name = "id")
	@Transient
	private Map<String, Developer> developerMap = new HashMap<String, Developer>(); 
	
//	@javax.persistence.OneToMany(cascade = CascadeType.ALL)
//	@javax.persistence.MapKey(name = "id")
	@Transient
	private Map<String, AuthorshipInfo> authorshipInfoMap = new HashMap<String, AuthorshipInfo>();

	@Enumerated(EnumType.STRING)
	private RepositoryStatus status;
	
	public Repository() {
	}
	public Repository(String fullName) {
		this.fullName = fullName;
	}
	
	private Developer addDeveloper(String name, String email, String userName) {
		Developer developer;
//		String userName = Developer.createUserName(name, email);
		if(developerMap.containsKey(userName))
			developer = developerMap.get(userName);
		else{
			developer = new Developer(name, email, userName);
			developerMap.put(userName, developer);
			developers.add(developer);
		}
		return developer;
		
	}
	
	private Developer addDeveloper(String userName) {
		Developer developer;
//		String userName = Developer.createUserName(name, email);
		if(developerMap.containsKey(userName))
			developer = developerMap.get(userName);
		else{
			developer = new Developer(userName);
			developerMap.put(userName, developer);
			developers.add(developer);
		}
		return developer;
		
	}

	public AuthorshipInfo getAuthorshipInfo(String name, String email, String userName, File file) {
		Developer developer = this.addDeveloper(name, email, userName);
		String authorshipKey = (file.getPath() + developer.getNewUserName());
		AuthorshipInfo authorshipInfo;
		if(authorshipInfoMap.containsKey(authorshipKey))
			authorshipInfo = authorshipInfoMap.get(authorshipKey);
		else {
			authorshipInfo =  new AuthorshipInfo(file, developer);
			authorshipInfoMap.put(authorshipKey, authorshipInfo);
//			authorships.add(authorshipInfo);
		}
		return authorshipInfo;		
	}
	
	
	
	
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public List<File> getFiles() {
		return files;
	}
	public void setFiles(List<File> files) {
		this.files = files;
	}
	public Long getId() {
		return id;
	}

	public Map<String, AuthorshipInfo> getAuthorshipInfoMap() {
		return authorshipInfoMap;
	}
	
	public void setAuthorshipInfoMap(Map<String, AuthorshipInfo> authorshipInfoMap) {
		this.authorshipInfoMap = authorshipInfoMap;
	}
	public List<Developer> getDevelopers() {
		return developers;
	}
	public void setDevelopers(List<Developer> developers) {
		this.developers = developers;
	}
//	public List<AuthorshipInfo> getAuthorships() {
//		return authorships;
//	}
//	public void setAuthorships(List<AuthorshipInfo> authorships) {
//		this.authorships = authorships;
//	}
	public RepositoryStatus getStatus() {
		return status;
	}
	public void setStatus(RepositoryStatus status) {
		this.status = status;
	}
	

	
	
	
}
