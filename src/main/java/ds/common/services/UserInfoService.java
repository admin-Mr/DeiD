
package ds.common.services;

import ds.common.domain.User;

public interface UserInfoService {

	public User findUserDB(String account);
	
	/** find user by account **/
	public User findUser(String account);
	
	/** update user **/
	public User updateUser(User user);
}
