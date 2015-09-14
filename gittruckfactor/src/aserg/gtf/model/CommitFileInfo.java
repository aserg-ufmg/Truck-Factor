package aserg.gtf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CommitFileInfo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	private String newFileSha;
	private String oldFileSha;
	@Column(length = 1000)
	private String oldFileName;
	@Column(length = 1000)
	private String newFileName;
	@Enumerated(EnumType.STRING)
	private Status status;
//	private int additions;
//	private int deletions;
//	private int commitId;

	public CommitFileInfo() {
		// TODO Auto-generated constructor stub
	}


	public CommitFileInfo(String oldFileName, String newFileName, Status status, String oldFileSha, String newFileSHA) {
		super();
		this.oldFileName = oldFileName;
		this.newFileName = newFileName;
		this.status = status;
		this.oldFileSha = oldFileSha;
		this.newFileSha = newFileSHA;
//		setShaKey(commitSha+oldFileSha+newFileSHA);
	}

//	public Long getId() {
//		id = null;
//		return id;
//	}
//
//	public void setId(final Long id) {
//		this.id = null;
//	}

	@Override
	public String toString() {
		return newFileSha  + ", " + newFileName + ", " + status; //+ ", " + additions  + ", " + deletions + ", " + sha + ", " + commitId + ", " + message; 
	}
	public String getNewFileName() {
		return newFileName;
	}

	public void setNewFileName(String fileName) {
		this.newFileName = fileName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

//	@Override
//	public int compareTo(CommitFileInfo o) {
////		if (this.status == Status.ADDED)
////			if (o.status == Status.ADDED)
////				return 0;
////			else 
////				return -1;
//		return this.date.compareTo(o.date);
//	}
	

	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String getNewFileSha() {
		return newFileSha;
	}

	public void setNewFileSha(String fileSha) {
		this.newFileSha = fileSha;
	}

	public String getOldFileSha() {
		return oldFileSha;
	}

	public void setOldFileSha(String oldFileSha) {
		this.oldFileSha = oldFileSha;
	}


	public Long getId() {
		return id;
	}




}
