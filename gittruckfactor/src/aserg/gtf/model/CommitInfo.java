package aserg.gtf.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.eclipse.persistence.annotations.Index;

@Entity
@Table(
name = "COMMITINFO",
uniqueConstraints = {@UniqueConstraint(columnNames = {"repositoryName", "sha"})}
)
public class CommitInfo extends AbstractEntity implements Comparable<CommitInfo>{
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		protected Long id;
		private String sha;
		@Index(name="REPOSITORYNAMEINDEX")
		private String repositoryName;
		@Lob
		private String message;
		private String name;
		private String email;
		
		private String nameCommiter;
		private String emaiCommiter;
		
		
		@Temporal(TemporalType.TIMESTAMP)
		private Date date;
		@OneToMany(cascade = { CascadeType.ALL })
		private List<CommitFileInfo> commitFiles;
		@ElementCollection
		private List<String> parentsSha;

		@OneToMany(cascade = { CascadeType.ALL })
		private List<LogCommitFileInfo> logCommitFiles;

		public CommitInfo() {
			// TODO Auto-generated constructor stub
		}

		public CommitInfo(String repositoryName, String sha, String message, String name,
				String email, Date date, List<CommitFileInfo> commitFiles, List<String> parentsSha) {
			super();
			this.repositoryName = repositoryName;
			this.sha = sha;
			this.message = message;
			this.name = name;
			this.email = email;
			this.date = date;
			this.commitFiles = commitFiles;
			this.parentsSha = parentsSha;
		}
		
		
		public CommitInfo(Date date, String repositoryName, String sha, String message, String name,
				String email, List<LogCommitFileInfo> logCommitFiles, List<String> parentsSha) {
			super();
			this.repositoryName = repositoryName;
			this.sha = sha;
			this.message = message;
			this.name = name;
			this.email = email;
			this.date = date;
			this.logCommitFiles = logCommitFiles;
			this.parentsSha = parentsSha;
		}
		
		public String getSha() {
			return sha;
		}

		public void setSha(String sha) {
			this.sha = sha;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public List<CommitFileInfo> getCommitFiles() {
			return commitFiles;
		}

		public void setCommitFiles(List<CommitFileInfo> commitFiles) {
			this.commitFiles = commitFiles;
		}

		@Override
		public int compareTo(CommitInfo o) {
			return this.date.compareTo(o.date);
		}

		public String getRepositoryName() {
			return repositoryName;
		}

		public void setRepositoryName(String repositoryName) {
			this.repositoryName = repositoryName;
		}
		
		public List<LogCommitFileInfo> getLogCommitFiles() {
			return logCommitFiles;
		}

		public void setLogCommitFiles(List<LogCommitFileInfo> logCommitFiles) {
			this.logCommitFiles = logCommitFiles;
		}

		public Long getId() {
			return id;
		}

		public List<String> getParentsSha() {
			return parentsSha;
		}

		public void setParentsSha(List<String> parentsSha) {
			this.parentsSha = parentsSha;
		}

		public String getNameCommiter() {
			return nameCommiter;
		}

		public void setNameCommiter(String nameCommiter) {
			this.nameCommiter = nameCommiter;
		}

		public String getEmaiCommiter() {
			return emaiCommiter;
		}

		public void setEmaiCommiter(String emaiCommiter) {
			this.emaiCommiter = emaiCommiter;
		}
		
		
}
