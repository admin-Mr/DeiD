package ds.common.services;

import util.Authority;

public interface ProgramAuthorityService {
			
	public void initAuthority(String account, String programName);
		
	public boolean getAuth(Authority enumAuth);
	
}
