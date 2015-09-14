package aserg.gtf.model;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@SuppressWarnings("serial")
@Entity
public class GitRepository extends AbstractEntity{
	@Id
	private String repositoryName;
	
	@OneToOne(cascade = { CascadeType.REFRESH })
	ProjectInfo projectInfo;
	@OneToMany(cascade = { CascadeType.ALL })
	List<CommitInfo> commits;

	public GitRepository() {
	}

	public GitRepository(ProjectInfo projectInfo, List<CommitInfo> commits) {
		this.projectInfo =  projectInfo;
		this.commits = commits;
		this.repositoryName = projectInfo.getFullName();
	}

	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}

	public void setProjectInfo(ProjectInfo projectInfo) {
		this.projectInfo = projectInfo;
		this.repositoryName = projectInfo.getFullName();
	}

	public List<CommitInfo> getCommits() {
		return commits;
	}

	public void setCommits(List<CommitInfo> commits) {
		this.commits = commits;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

}
