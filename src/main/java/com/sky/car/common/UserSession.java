
package com.sky.car.common;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserSession implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String tokenId; 
	private Date loginTime;
	
	private String userid;
	private String username;
	private String password;
	
	private String belongorg;
	
	private boolean isTrueUser;
    private String userMessage;
    
	//机构
    private String orgid;

    private String orgname;

    private String orglevel;
	
	//角色
    private List<String> roleTable;
    //权限
    private List<String> rightTable;

	public UserSession(){

	}
	
    public UserSession(UserSession userSession) {
    	this.userid = userSession.getUserid();
    	this.username = userSession.getUsername();
        this.tokenId = userSession.getTokenId();
        this.isTrueUser = userSession.isTrueUser;
        this.roleTable = userSession.getRoleTable();
        this.rightTable = userSession.getRightTable();
        this.orgid = userSession.getOrgid();
        this.orgname = userSession.getOrgname();
        this.orglevel = userSession.getOrglevel();
    }

	public boolean hasRole(String sRoleID) {
        return this.getRoleTable().contains(sRoleID);
    }

    public boolean hasRole(String[] sRoleIDs) {
        for(int i = 0; i < sRoleIDs.length; ++i) {
            if (this.hasRole(sRoleIDs[i])) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasRight(String sRightID){
        return this.getRightTable().contains(sRightID);
    }
	
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	} 
	
	public List<String> getRoleTable() {
		return roleTable;
	}

	public void setRoleTable(List<String> roleTable) {
		this.roleTable = roleTable;
	}

	public List<String> getRightTable() {
		return rightTable;
	}

	public void setRightTable(List<String> rightTable) {
		this.rightTable = rightTable;
	}
	
	public boolean isTrueUser() {
		return isTrueUser;
	}

	public void setTrueUser(boolean isTrueUser) {
		this.isTrueUser = isTrueUser;
	}

	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBelongorg() {
		return belongorg;
	}

	public void setBelongorg(String belongorg) {
		this.belongorg = belongorg;
	}

	public String getOrgid() {
		return orgid;
	}

	public void setOrgid(String orgid) {
		this.orgid = orgid;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getOrglevel() {
		return orglevel;
	}

	public void setOrglevel(String orglevel) {
		this.orglevel = orglevel;
	}
	
}
