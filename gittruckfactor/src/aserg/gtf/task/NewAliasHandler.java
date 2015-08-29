package aserg.gtf.task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import aserg.gtf.dao.LogCommitDAO;
import aserg.gtf.dao.ProjectInfoDAO;
import aserg.gtf.model.LogCommitFileInfo;
import aserg.gtf.model.LogCommitInfo;
import aserg.gtf.model.NewFileInfo;
import aserg.gtf.model.ProjectInfo;
import aserg.gtf.model.authorship.Developer;
import aserg.gtf.util.Alias;
import aserg.gtf.util.CRLFLineReader;
import aserg.gtf.util.LineInfo;

public class NewAliasHandler{
	List<LineInfo> fileAliases;
	public NewAliasHandler(List<LineInfo> list) {
		this.fileAliases = list;
	}
	Map<String, List<LogCommitInfo>> devUsernameMap;
	Map<String, List<LogCommitInfo>> devNameMap;
	
	public Map<String, LogCommitInfo> execute(String repositoryName, Map<String, LogCommitInfo> commits){
		setUsername(commits);
		devUsernameMap = new HashMap<String, List<LogCommitInfo>>();
		
		for (LogCommitInfo commit : commits.values()) {
			String commitUsername = commit.getUserName();
			if (!devUsernameMap.containsKey(commitUsername))
				devUsernameMap.put(commitUsername, new ArrayList<LogCommitInfo>());
			devUsernameMap.get(commitUsername).add(commit);
		}
		
		
		//Set<String> devNames = new HashSet<String>();
		devNameMap = new HashMap<String, List<LogCommitInfo>>();
		for (LogCommitInfo commit : commits.values()) {
			//devNames.add(commit.getMainName());
			String commitDevName = commit.getNormMainName();
			if (!devNameMap.containsKey(commitDevName))
				devNameMap.put(commitDevName, new ArrayList<LogCommitInfo>());
			devNameMap.get(commitDevName).add(commit);
		}
		unifyUsernameByName();
		if (fileAliases!=null)
			treatFileAlias();
		return commits;
	}

	private void setUsername(Map<String, LogCommitInfo> commits) {
		for (LogCommitInfo commit : commits.values()) {
			if (!commit.getNormMainEmail().isEmpty())
				commit.setUserName(commit.getNormMainEmail());
			else if (!commit.getMainName().isEmpty())
				commit.setUserName(commit.getNormMainName());
			else{
				System.err.println("Empty usernam in commit  " + commit.getSha());
				commit.setUserName("");
			}
		}
		
	}


	private void treatFileAlias() {
		for (LineInfo info : fileAliases) {
			String rep = info.getRepositoryName();
			String dev1 = info.getValues().get(0);
			String dev2 = info.getValues().get(1);
			String usernameDev1 = devNameMap.get(dev1.toUpperCase()).get(0).getUserName();
			String usernameDev2 = devNameMap.get(dev2.toUpperCase()).get(0).getUserName();
			
			String newUsername = usernameDev1.contains(usernameDev2)?usernameDev1: (usernameDev2.contains(usernameDev1)?usernameDev2:usernameDev1+"$$"+usernameDev2);
			
			
			for (LogCommitInfo commit : devUsernameMap.get(usernameDev1)) {
				if (!commit.getUserName().equals(usernameDev2)){
					setNewUsername(commit, newUsername);
					//commit.setUserName(newUsername);					
				}
				
			}
			for (LogCommitInfo commit : devUsernameMap.get(usernameDev2)) {
				if (!commit.getUserName().equals(usernameDev1)){
					setNewUsername(commit, newUsername);
					//commit.setUserName(newUsername);					
				}
				
			}
			
		}
		
	}


	private void printAliases(Map<String, List<String>> aliases) {
		for (Entry<String, List<String>> entry : aliases.entrySet()) {
			for (String alias : entry.getValue()) {
				System.out.println(entry.getKey() + ";" + alias);
			}
		}
		
	}


	/** Treat different names for the same e-mail case */
	private void unifyUsernameByEmail(Map<String, List<LogCommitInfo>> usernameMap) {
		for (Entry<String, List<LogCommitInfo>> entry : usernameMap.entrySet()) {
			List<String> names = getNamesList(entry.getValue());
			String newUserName = names.get(0);
			if (names.size()>1)
				newUserName = getNewName(names);
			for (LogCommitInfo commit : entry.getValue()) {
				commit.setUserName(newUserName);
			}
		}
		
	}

	private List<String> getNamesList(Collection<LogCommitInfo> commits) {
		HashSet<String> nameSet = new HashSet<String>();
		List<String> names =  new ArrayList<String>();
		for (LogCommitInfo commit : commits) {
			String simplifyedName = commit.getNormMainName().replace(" ", "");
			if (!nameSet.contains(simplifyedName)){
				nameSet.add(simplifyedName);
				names.add(commit.getMainName());
			}
		}
		return names;
	}
	

	private String getNewName(Collection<String> names) {
		String newName = "";
		for (String name : names) {
			if (!newName.isEmpty())
				newName += "**";
			newName += name;
		}
		return newName;
	}
	
	/** Treat similar name aliases */
	private void unifyUsernameByName() {
		for (Entry<String, List<LogCommitInfo>> entry : devNameMap.entrySet()) {
			// Avoid to group developers with empty name 
			if (!entry.getKey().isEmpty()) {				
				List<String> userNames = getUserNamesList(entry.getValue());
				if (userNames.size() > 1) {
					String newUsername = getNewUserName(userNames);
					for (LogCommitInfo commit : entry.getValue()) {
						setNewUsername(commit, newUsername);
						
						//commit.setUserName(newUsername);
					}
				}
			}
		}
		
	}
	
	private void setNewUsername(LogCommitInfo commit, String newUsername) {
		List<LogCommitInfo> listCommits = devUsernameMap.get(commit.getUserName());
		devUsernameMap.remove(commit.getUserName());
		for (LogCommitInfo commitAux : listCommits) {
			commitAux.setUserName(newUsername);			
		}
		
		if (!devUsernameMap.containsKey(newUsername))
			devUsernameMap.put(newUsername, listCommits);
		else
			devUsernameMap.get(newUsername).addAll(listCommits);
	}

	private List<String> getUserNamesList(Collection<LogCommitInfo> commits) {
		Set<String> userNameSet = new HashSet<String>();
		for (LogCommitInfo commit : commits) {
			userNameSet.add(commit.getUserName());
		}
		return new ArrayList<String>(userNameSet);
	}

	
	private String getNewUserName(Collection<String> names) {
		String newName = "";
		for (String name : names) {
			if (!newName.isEmpty())
				newName += "||";
			newName += name;
		}
		return newName; 	
	}

	
	private static Map<String, List<String>> findAliases(List<String> allDevelopers, int distance, int minSize) {
		int newDistance = distance;
		List<String> copyList =  new CopyOnWriteArrayList<String>(allDevelopers);
		Map<String, List<String>> aliases =  new HashMap<String, List<String>>();
		for (String developer1 : copyList) {
			copyList.remove (developer1);
			for (String developer2 : copyList) {
				if(developer1.length()>=minSize){
					int localDistance = StringUtils.getLevenshteinDistance(convertToUTFLower(developer1), convertToUTFLower(developer2));
					if (distance == -1){
						newDistance = developer1.split(" ").length;
					}
					if (!developer1.equals(developer2) && localDistance<=newDistance){
						if(!aliases.containsKey(developer1))
							aliases.put(developer1, new ArrayList<String>());
						aliases.get(developer1).add(developer2);
						copyList.remove(developer2);
					}
				}
			}
		}
		return aliases;
	}

	private static CharSequence convertToUTFLower(String str) {
		String ret = null;
		try {
			ret = new String(str.getBytes("ISO-8859-1"), "UTF-8");
		}
		catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return ret.toLowerCase();
	}

	private class DevIdentification{
		private String name;
		private String email;
		private String username;
		
			
		public DevIdentification(String name, String email, String username) {
			super();
			this.name = name;
			this.email = email;
			this.username = username;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		
	}

}
