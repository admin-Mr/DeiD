
package ds.common.services;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletContext;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import util.Common;
import util.Authority;
import ds.common.services.ProgramAuth;
import ds.common.services.ProgramAuthorityService;


public class ProgramAuthorityServiceImpl implements ProgramAuthorityService,Serializable{
	private static final long serialVersionUID = 1L;

	public void initAuthority(String account, String programName){
		ServletContext sc =
			    (ServletContext) Sessions.getCurrent().getWebApp().getServletContext();
		String strSYSID = sc.getInitParameter("projectID");
		PreparedStatement psData = null;
		ResultSet rs = null;
		Connection conn = null;
		String strSql = "Select PB_ID, Case SUM(PB_RH01) When 0 Then 'N' Else 'Y' End AS PB_RH01," + 
				"Case SUM(PB_RH02) When 0 Then 'N' Else 'Y' End AS PB_RH02," +
				"Case SUM(PB_RH03) When 0 Then 'N' Else 'Y' End AS PB_RH03," + 
				"Case SUM(PB_RH04) When 0 Then 'N' Else 'Y' End AS PB_RH04," +				
				"Case SUM(PB_RH08) When 0 Then 'N' Else 'Y' End AS PB_RH08," +				
				"Case SUM(PB_RH09) When 0 Then 'N' Else 'Y' End AS PB_RH09," + 
				"Case SUM(PB_RH10) When 0 Then 'N' Else 'Y' End AS PB_RH10 From ( " +
					"Select PB_ID, Case NVL(PB_RH01,'N') When 'Y' then 1 Else 0 End AS PB_RH01," + 
						"Case NVL(PB_RH02,'N') When 'Y' Then 1 Else 0 End AS PB_RH02," + 
						"Case NVL(PB_RH03,'N') When 'Y' Then 1 Else 0 End AS PB_RH03," + 
						"Case NVL(PB_RH04,'N') When 'Y' Then 1 Else 0 End AS PB_RH04," +
						"Case NVL(PB_RH08,'N') When 'Y' Then 1 Else 0 End AS PB_RH08," + 
						"Case NVL(PB_RH09,'N') When 'Y' Then 1 Else 0 End AS PB_RH09," + 
						"Case NVL(PB_RH10,'N') When 'Y' Then 1 Else 0 End AS PB_RH10 " + 
					"From DSPB01 Where PB_USERID='" + account + "' and PB_ID in (Select PB_ID From DSPB00_NEW Where PB_SYSID='" + strSYSID + "') " + 
					"union all " +
					"Select PB_ID, Case NVL(PB_RH01,'N') When 'Y' Then 1 Else 0 End AS PB_RH01," + 
						"Case NVL(PB_RH02,'N') When 'Y' Then 1 Else 0 End AS PB_RH02," + 
						"Case NVL(PB_RH03,'N') When 'Y' Then 1 Else 0 End AS PB_RH03," + 
						"Case NVL(PB_RH04,'N') When 'Y' Then 1 Else 0 End AS PB_RH04," +
						"Case NVL(PB_RH08,'N') When 'Y' Then 1 Else 0 End AS PB_RH08," + 
						"Case NVL(PB_RH09,'N') When 'Y' Then 1 Else 0 End AS PB_RH09," + 
						"Case NVL(PB_RH10,'N') When 'Y' Then 1 Else 0 End AS PB_RH10 " + 
						"From DSPB01_GRP Where PB_GROUPID in (Select PB_GROUPID From DSPB04 Where PB_USERID='" + account + 
						"' and PB_GROUPID in (Select PB_GROUPID From DSPB03 Where PB_SYSID='" + strSYSID + "'))" +
					") Where PB_ID = (Select PB_ID From DSPB00_NEW Where PB_PRGNAME='" + programName + "' and PB_SYSID='" + strSYSID + "') " +
					"Group by PB_ID";
		try{
			String strAccount = "";
			String strPrgName = "";
			String strAuthQuery = "";
			String strAuthInsert = "";
			String strAuthEdit = "";
			String strAuthDelete = "";
			String strAuthEmail = "";
			String strAuthPrint = "";
			String strAuthExport = "";
			conn = Common.getDbConnection();
			psData = conn.prepareStatement(strSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = psData.executeQuery();
			while (rs.next()){
				if (rs != null){
					strAccount = account;
					strPrgName = programName;
					strAuthInsert = Common.getValue(rs.getObject("PB_RH01"));
					strAuthQuery = Common.getValue(rs.getObject("PB_RH02"));
					strAuthDelete = Common.getValue(rs.getObject("PB_RH03"));
					strAuthEdit = Common.getValue(rs.getObject("PB_RH04"));
					strAuthEmail = Common.getValue(rs.getObject("PB_RH08"));
					strAuthPrint = Common.getValue(rs.getObject("PB_RH09"));
					strAuthExport = Common.getValue(rs.getObject("PB_RH10"));
				}				
			}
			Session sessAuth = Sessions.getCurrent();
			ProgramAuth prgAuth = new ProgramAuth(strAccount, strPrgName, strAuthQuery, strAuthInsert, strAuthEdit, strAuthDelete, strAuthPrint, strAuthExport, strAuthEmail);
			sessAuth.setAttribute("programAuthority", prgAuth);
		} catch (Exception e){
			e.printStackTrace();
			System.out.println("[ERROR]CheckAuthority: " + e.getMessage());
		} finally {
			try{
				if (rs != null)
					rs.close();
				if (psData != null)
					psData.close();
				if (conn != null)
					Common.closeConnection(conn);
			}catch(Exception ex){
				System.out.println("[ERROR]CheckAuthority_Finally: " + ex.getMessage());
			}
		}	
	}

	public boolean getAuth(Authority enumAuth){
		Boolean bHasAuth = false;
		Session sessAuth = Sessions.getCurrent();
		ProgramAuth prgAuth = (ProgramAuth) sessAuth.getAttribute("programAuthority");
		switch(enumAuth){
			case QUERY:
				bHasAuth = prgAuth.getAuthQuery();
				break;
			case INSERT:
				bHasAuth = prgAuth.getAuthInsert();
				break;
			case EDIT:
				bHasAuth = prgAuth.getAuthEdit();
				break;
			case DELETE:
				bHasAuth = prgAuth.getAuthDelete();
				break;
			case PRINT:
				bHasAuth = prgAuth.getAuthPrint();
				break;
			case EXPORT:
				bHasAuth = prgAuth.getAuthExport();
				break;
			case EMAIL:
				bHasAuth = prgAuth.getAuthEmail();
				break;
		}
		return bHasAuth;
	}
}
