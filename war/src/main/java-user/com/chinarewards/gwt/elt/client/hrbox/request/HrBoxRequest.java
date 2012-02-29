/**
 * 
 */
package com.chinarewards.gwt.elt.client.hrbox.request;

import net.customware.gwt.dispatch.shared.Action;

import com.chinarewards.gwt.elt.client.support.UserSession;

/**
 * @author lw
 */
public class HrBoxRequest implements Action<HrBoxResponse> {

	
	private UserSession userSession;
    private String  status;
	public HrBoxRequest() {
	}

	public HrBoxRequest(UserSession userSession,String  status) {
		this.status = status;
		this.userSession = userSession;

	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UserSession getUserSession() {
		return userSession;
	}

	public void setUserSession(UserSession userSession) {
		this.userSession = userSession;
	}

	

}
