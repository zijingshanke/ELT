/**
 * 
 */
package com.chinarewards.gwt.elt.client.nominate;

import net.customware.gwt.dispatch.shared.Action;

/**
 * An action which perform request to search user.
 * 
 * @author nicho
 * @since 2011年12月12日 
 */
public class NominateAddRequest implements Action<NominateAddResponse> {

	private String awardsId;//奖项ID

	public String getAwardsId() {
		return awardsId;
	}

	public void setAwardsId(String awardsId) {
		this.awardsId = awardsId;
	}

	
	/**
	 * For serialization
	 */
	public NominateAddRequest() {
	}

	public NominateAddRequest(String awardsId) {
		this.awardsId=awardsId;
	}
}
