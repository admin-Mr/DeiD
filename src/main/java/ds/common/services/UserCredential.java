
package ds.common.services;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletContext;

import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import ds.dspb.domain.DSPB03;
import util.Common;

public class UserCredential implements Serializable {
	private static final long serialVersionUID = 1L;
	@WireVariable
	private CRUDService CRUDService;
	
	String account;
	String name;
	String email;

	Set<String> roles = new HashSet<String>();
	Set<String> auth = new HashSet<String>();
	
	ServletContext sc = (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
	String strSYSID = sc.getInitParameter("projectID");
	
	public UserCredential(String account, String name) {
		this.account = account;
		this.name = name;
		
		String sSQL = "Select * From DSPB03 Where PB_SYSID='" + strSYSID + "' and PB_GROUPID in (Select PB_GROUPID From DSPB04 Where PB_USERID='" + account + "')";
		CRUDService CRUDService = (CRUDService) Sessions.getCurrent().getAttribute("CRUDService");		
		EntityManager em = CRUDService.getEmf().createEntityManager();
		try{			
			List lst = em.createNativeQuery(sSQL, DSPB03.class).getResultList();
			for (Object obj : lst){
				DSPB03 e = (DSPB03) obj;
				if (e != null){
					this.roles.add(e.getPB_GROUPNA().toUpperCase());
				}
			}
			
			Query qry = em.createNativeQuery("SELECT PB_EMAIL FROM DSPB02 WHERE PB_USERID = :PB_USERID");
			qry.setParameter("PB_USERID", account);
			if (qry.getSingleResult() != null) {
				this.email = qry.getSingleResult().toString();
			}			
		}finally{
			em.close();
		}
		PreparedStatement psData = null;
		ResultSet rs = null;
		Connection conn = null;
		sSQL = "Select PB_PRGNAME, Case SUM(PB_RH01) When 0 Then 'N' Else 'Y' End AS PB_RH01, " +
					"Case SUM(PB_RH02) When 0 Then 'N' Else 'Y' End AS PB_RH02, " +
					"Case SUM(PB_RH03) When 0 Then 'N' Else 'Y' End AS PB_RH03, " +
					"Case SUM(PB_RH04) When 0 Then 'N' Else 'Y' End AS PB_RH04, " +
					"Case SUM(PB_RH08) When 0 Then 'N' Else 'Y' End AS PB_RH08, " +
					"Case SUM(PB_RH09) When 0 Then 'N' Else 'Y' End AS PB_RH09, " +
					"Case SUM(PB_RH10) When 0 Then 'N' Else 'Y' End AS PB_RH10 From (" + 
					"Select DSPB00_NEW.PB_PRGNAME, Case NVL(PB_RH01,'N') When 'Y' then 1 Else 0 End AS PB_RH01, " +
					"Case NVL(PB_RH02,'N') When 'Y' Then 1 Else 0 End AS PB_RH02, " +
					"Case NVL(PB_RH03,'N') When 'Y' Then 1 Else 0 End AS PB_RH03, " +
					"Case NVL(PB_RH04,'N') When 'Y' Then 1 Else 0 End AS PB_RH04, " +
					"Case NVL(PB_RH08,'N') When 'Y' Then 1 Else 0 End AS PB_RH08, " +
					"Case NVL(PB_RH09,'N') When 'Y' Then 1 Else 0 End AS PB_RH09, " +
					"Case NVL(PB_RH10,'N') When 'Y' Then 1 Else 0 End AS PB_RH10 " +
					"From DSPB01 LEFT JOIN DSPB00_NEW ON DSPB01.PB_ID = DSPB00_NEW.PB_ID " + 
					"Where PB_USERID='" + account + "' and PB_SYSID='" + strSYSID + "' AND PB_PRGNAME IS NOT null " +
					"union all " +
					"Select DSPB00_NEW.PB_PRGNAME, Case NVL(PB_RH01,'N') When 'Y' Then 1 Else 0 End AS PB_RH01, " +
					"Case NVL(PB_RH02,'N') When 'Y' Then 1 Else 0 End AS PB_RH02, " +
					"Case NVL(PB_RH03,'N') When 'Y' Then 1 Else 0 End AS PB_RH03, " +
					"Case NVL(PB_RH04,'N') When 'Y' Then 1 Else 0 End AS PB_RH04, " +
					"Case NVL(PB_RH08,'N') When 'Y' Then 1 Else 0 End AS PB_RH08, " +
					"Case NVL(PB_RH09,'N') When 'Y' Then 1 Else 0 End AS PB_RH09, " +
					"Case NVL(PB_RH10,'N') When 'Y' Then 1 Else 0 End AS PB_RH10 " +
					"From DSPB01_GRP LEFT JOIN DSPB00_NEW ON DSPB01_GRP.PB_ID = DSPB00_NEW.PB_ID " +
					"Where PB_GROUPID in (Select PB_GROUPID From DSPB04 Where PB_USERID='" + account + "' " +
						"and PB_GROUPID in (Select PB_GROUPID From DSPB03 Where PB_SYSID='" + strSYSID + "')) " +
						"and PB_PRGNAME IS NOT null)" +
					"GROUP BY PB_PRGNAME";
		conn = Common.getDbConnection();
		try{	
			psData = conn.prepareStatement(sSQL, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = psData.executeQuery();
			while (rs.next()){
				if (rs != null){
					auth.add(Common.getValue(rs.getObject("PB_PRGNAME")) + "," +
							Common.getValue(rs.getObject("PB_RH01")) + "," +
							Common.getValue(rs.getObject("PB_RH02")) + "," +
							Common.getValue(rs.getObject("PB_RH03")) + "," +
							Common.getValue(rs.getObject("PB_RH04")) + "," +
							Common.getValue(rs.getObject("PB_RH08")) + "," +
							Common.getValue(rs.getObject("PB_RH09")) + "," +
							Common.getValue(rs.getObject("PB_RH10")));
				}				
			}
		} catch (Exception e){
			System.out.println("[ERROR]GetAuthority: " + e.getMessage());
		} finally {
			try{
				if (rs != null)
					rs.close();
				if (psData != null)
					psData.close();
				if (conn != null)
					Common.closeConnection(conn);
			}catch(Exception ex){
				System.out.println("[ERROR]GetAuthority_Finally: " + ex.getMessage());
			}
		}			
	}

	public UserCredential() {
		this.account = "anonymous";
		this.name = "Anonymous";
		roles.add("anonymous");
	}

	public boolean isAnonymous() {
		return hasRole("anonymous") || "anonymous".equals(account);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Set<String> getRoles(){
		return this.roles;
	}
	
	public void setRoles(Set<String> Roles){
		this.roles = Roles;
	}
		
	public Set<String> getAuth(){
		return this.auth;
	}
	
	public void setAuth(Set<String> Auth){
		this.auth = Auth;
	}
	
	public boolean hasRole(String role) {
		return roles.contains(role.toUpperCase());
	}

	public void addRole(String role) {
		roles.add(role);
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
