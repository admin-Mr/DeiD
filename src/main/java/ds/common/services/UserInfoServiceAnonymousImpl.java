package ds.common.services;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ds.common.domain.User;
import util.Common;

public class UserInfoServiceAnonymousImpl implements UserInfoService, Serializable {
	private static final long serialVersionUID = 1L;
	static protected List<User> userList = new ArrayList<User>();
	
	public User findUserDB(String account) {
		
		//Q20161229004 2016/12/30 Jassie
		String sql="Select * from DSPB02 where PB_USERID ='"+ account + "' and PB_LOCK='N'";
		
		try {
			Connection conn = Common.getDbConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()){
				if (rs != null){
					final String pwd = Common.getValue(rs.getObject("PB_PASSENCRY"));
					final String name = Common.getValue(rs.getObject("PB_NAME"));
					final String mail = Common.getValue(rs.getObject("PB_EMAIL"));
					final String unpwd = Common.getValue(rs.getObject("PB_PASS"));
					User u = new User(account, pwd, name, mail, unpwd);
					System.out.println(rs + " user record(s) found.");
					return User.clone(u);
				}				
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("findUserDB error");
		}
		return null;
	}

	public synchronized User findUser(String account) {
		int s = userList.size();
		for (int i = 0; i < s; i++) {
			User u = userList.get(i);
			if (account.equals(u.getAccount())) {
				return User.clone(u);
			}
		}
		return null;
	}

	public synchronized User updateUser(User user) {
		int s = userList.size();
		for (int i = 0; i < s; i++) {
			User u = userList.get(i);
			if (user.getAccount().equals(u.getAccount())) {
				userList.set(i, u = User.clone(user));
				return u;
			}
		}
		throw new RuntimeException("user not found " + user.getAccount());
	}
}
