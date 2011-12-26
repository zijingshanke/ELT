/**
 * 
 */
package com.chinarewards.gwt.elt.client.rewardItem.request;

import net.customware.gwt.dispatch.shared.Action;

/**
 * @author lw
 * @since 0.2.0 2011-12-27
 */
public class SearchRewardsItemByIdRequest implements
		Action<SearchRewardsItemByIdResponse> {

	private String id;

	public SearchRewardsItemByIdRequest() {
	}

	public SearchRewardsItemByIdRequest(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
