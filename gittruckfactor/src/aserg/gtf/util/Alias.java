package aserg.gtf.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.io.LineReader;

public class Alias {
	private String repository;
	private String dev1;
	private String dev2;
	
	public Alias(String repository, String dev1, String dev2) {
		super();
		this.repository = repository;
		this.dev1 = dev1;
		this.dev2 = dev2;
	}
	
	public static List<Alias> getAliasFromFile(String fileName) throws IOException{
		List<Alias> fileAliases =  new ArrayList<Alias>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		LineReader lineReader = new LineReader(br);
		String sCurrentLine;
		String[] values;
		int countcfs = 0;
		while ((sCurrentLine = lineReader.readLine()) != null) {
			values = sCurrentLine.split(";");
			if (values.length<3)
				System.err.println("Erro na linha " + countcfs);
			String rep = values[0];
			String dev1 = values[1];
			String dev2 = values[2];
			fileAliases.add(new Alias(rep, dev1, dev2));
			countcfs++;
		}
		return fileAliases;
	}
	
	public static boolean isAlias(String repository, String dev1, String dev2){
		if (notAliases == null)
			try {
				notAliases = readFile("notalias.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		Alias newNot = new Alias(repository, dev1, dev2);
		for (int i = 0; i < notAliases.length; i++) {
			if (notAliases[i].equals(newNot))
				return false;
		}
		return true;
	}
	@Override
	public boolean equals(Object obj) {
		Alias other = (Alias)obj;
		if(this.repository.equals(other.repository) && this.dev1.equals(other.dev1) && this.dev2.equals(other.dev2))
			return true;
		return false;
	}
	
	private static Alias[] readFile(String fileName) throws IOException{
		List<Alias> fileAliases =  new ArrayList<Alias>();
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		LineReader lineReader = new LineReader(br);
		String sCurrentLine;
		String[] values;
		int countcfs = 0;
		while ((sCurrentLine = lineReader.readLine()) != null) {
			values = sCurrentLine.split(";");
			if (values.length<3)
				System.err.println("Erro na linha " + countcfs);
			String rep = values[0];
			String dev1 = values[1];
			String dev2 = values[2];
			fileAliases.add(new Alias(rep, dev1, dev2));
			countcfs++;
		}
		return fileAliases.toArray(new Alias[0]);
	}
	
	public String getRepository() {
		return repository;
	}

	public String getDev1() {
		return dev1;
	}

	public String getDev2() {
		return dev2;
	}

	private static Alias[] notAliases = null;
//		{new NotAlias("rails/rails","Nick", "rick"), new NotAlias("rails/rails","Nick", "Nico"),
//		new NotAlias("ruby/ruby","kou", "knu"), new NotAlias("ruby/ruby","kou", "ko1"),new NotAlias("ruby/ruby","nahi", "nari"),
//		new NotAlias("ruby/ruby","eban", "evan")};  
}