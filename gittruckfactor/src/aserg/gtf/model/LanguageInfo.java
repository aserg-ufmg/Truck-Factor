package aserg.gtf.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class LanguageInfo extends AbstractEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	private String language;
	private long size;

	public LanguageInfo() {
		// TODO Auto-generated constructor stub
	}
	

	public LanguageInfo(String language, long size) {
		super();
		this.language = language;
		this.size = size;
	}



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public long getSize() {
		return size;
	}



	public void setSize(long size) {
		this.size = size;
	}



	public Long getId() {
		return id;
	}




}
