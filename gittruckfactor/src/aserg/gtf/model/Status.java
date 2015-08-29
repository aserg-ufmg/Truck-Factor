package aserg.gtf.model;


public enum Status {
	ADDED, MODIFIED, RENAMED, REMOVED, COPIED, DELETED, RENAMED_TREATED;
	
	public static Status getStatus(String str){
		if (str.equalsIgnoreCase("added")||str.equalsIgnoreCase("add"))
			return ADDED; 
		if (str.equalsIgnoreCase("modified")||str.equalsIgnoreCase("modify"))
			return MODIFIED; 
		if (str.equalsIgnoreCase("renamed")||str.equalsIgnoreCase("rename"))
			return RENAMED; 
		if (str.equalsIgnoreCase("removed")||str.equalsIgnoreCase("remove"))
			return REMOVED;
		if (str.equalsIgnoreCase("copy"))
			return COPIED;
		if (str.equalsIgnoreCase("delete"))
			return DELETED;
		if (str.equalsIgnoreCase("RENAMED_TREATED"))
			return RENAMED_TREATED;
		
		return null;				
	}
}
