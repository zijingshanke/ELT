package com.chinarewards.gwt.elt.client.rewardItem.presenter;



import net.customware.gwt.dispatch.client.DispatchAsync;

import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.mvp.BasePresenter;
import com.chinarewards.gwt.elt.client.mvp.ErrorHandler;
import com.chinarewards.gwt.elt.client.mvp.EventBus;
import com.chinarewards.gwt.elt.client.rewardItem.plugin.RewardsItemConstants;
import com.chinarewards.gwt.elt.client.rewardItem.request.SearchRewardsItemByIdRequest;
import com.chinarewards.gwt.elt.client.rewardItem.request.SearchRewardsItemByIdResponse;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsItemClient;
import com.chinarewards.gwt.elt.client.support.SessionManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class RewardsItemViewPresenterImpl extends
		BasePresenter<RewardsItemViewPresenter.RewardsItemViewDisplay> implements		RewardsItemViewPresenter {
	   String instanceId;//修改时传过来的ID
	
	
	private final DispatchAsync dispatcher;
	private final ErrorHandler errorHandler;
	
	
	@Inject
	public RewardsItemViewPresenterImpl(EventBus eventBus,
			RewardsItemViewDisplay display,DispatchAsync dispatcher,ErrorHandler errorHandler,SessionManager sessionManager	) {
		super(eventBus, display);
		this.dispatcher=dispatcher;
		this.errorHandler = errorHandler;
		
	}

		@Override
		public void initInstanceId(String instanceId,RewardsItemClient item) {
			this.instanceId = instanceId;
			initDataToEditRewardsItem( item);
		}
		
		private void initDataToEditRewardsItem(final RewardsItemClient item) {
			String id = item.getId();
						
			{
				dispatcher.execute(new SearchRewardsItemByIdRequest(id),
				new AsyncCallback<SearchRewardsItemByIdResponse>() {
					@Override
					public void onFailure(Throwable arg0) {
						errorHandler.alert("查询奖项出错!");
						Platform.getInstance()
						.getEditorRegistry()
						.closeEditor(RewardsItemConstants.EDITOR_REWARDSITEM_ADD,instanceId);
					}

					@Override
					public void onSuccess(SearchRewardsItemByIdResponse response) {
						RewardsItemClient item = response.getRewardsItem();
						display.showRewardsItem(item);
					}

				});
		}
		}
				
				
     }