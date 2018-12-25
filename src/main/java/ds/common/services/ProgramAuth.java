
package ds.common.services;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ProgramAuth implements Serializable {
	private static final long serialVersionUID = 1L;

	String account;
	String ProgramName;
	Boolean AuthQuery;
	Boolean AuthInsert;
	Boolean AuthEdit;
	Boolean AuthDelete;
	Boolean AuthPrint;
	Boolean AuthExport;
	Boolean AuthEmail;	

	Set<String> roles = new HashSet<String>();

	public ProgramAuth(String account, String programName, String authQuery, String authInsert, String authEdit, String authDelete, String authPrint, String authExport, String authEmail) {
		this.account = account;
		this.ProgramName = programName;
		this.AuthQuery = authQuery.equals("Y")? true:false;
		this.AuthInsert = authInsert.equals("Y")? true:false;
		this.AuthEdit = authEdit.equals("Y")? true:false;
		this.AuthDelete = authDelete.equals("Y")? true:false;
		this.AuthPrint = authPrint.equals("Y")? true:false;
		this.AuthExport = authExport.equals("Y")? true:false;
		this.AuthEmail = authEmail.equals("Y")? true:false;		
	}

	public ProgramAuth() {
		this.account = "";
		this.ProgramName = "";
		this.AuthQuery = false;
		this.AuthInsert = false;
		this.AuthEdit = false;
		this.AuthDelete = false;
		this.AuthPrint = false;
		this.AuthExport = false;
		this.AuthEmail = false;		
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getProgramName() {
		return ProgramName;
	}

	public void setProgramName(String programName) {
		this.ProgramName = programName;
	}

	public Boolean getAuthQuery(){
		return AuthQuery;		
	}
	
	public void setAuthQuery(boolean authQuery){
		this.AuthQuery = authQuery;
	}

	public Boolean getAuthInsert(){
		return AuthInsert;
	}
	
	public void setAuthInsert(boolean authInsert){
		this.AuthInsert = authInsert;
	}
	
	public Boolean getAuthEdit(){
		return AuthEdit;
	}
	
	public void setAuthEdit(boolean authEdit){
		this.AuthEdit = authEdit;
	}
	
	public Boolean getAuthDelete(){
		return AuthDelete;
	}
	
	public void setAuthDelete(boolean authDelete){
		this.AuthDelete = authDelete;
	}
	
	public Boolean getAuthPrint(){
		return AuthPrint;		
	}
	
	public void setAuthPrint(boolean authPrint){
		this.AuthPrint = authPrint;
	}
	
	public Boolean getAuthExport(){
		return AuthExport;
	}
	
	public void setAuthExport(boolean authExport){
		this.AuthExport = authExport;
	}
	
	public Boolean getAuthEmail(){
		return AuthEmail;
	}
	
	public void setAuthEmail(boolean authEmail){
		this.AuthEmail = authEmail;
	}

}
