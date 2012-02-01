package com.chinarewards.gwt.elt.client.orderConfirmation.presenter;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.chinarewards.gwt.elt.client.awardShop.plugin.AwardShopListConstants;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.gift.model.GiftClient;
import com.chinarewards.gwt.elt.client.mvp.BasePresenter;
import com.chinarewards.gwt.elt.client.mvp.ErrorHandler;
import com.chinarewards.gwt.elt.client.mvp.EventBus;
import com.chinarewards.gwt.elt.client.orderConfirmation.presenter.OrderConfirmationPresenter.OrderConfirmationDisplay;
import com.chinarewards.gwt.elt.client.orderConfirmation.request.OrderConfirmationAddRequest;
import com.chinarewards.gwt.elt.client.orderConfirmation.request.OrderConfirmationAddResponse;
import com.chinarewards.gwt.elt.client.orderConfirmation.request.OrderConfirmationRequest;
import com.chinarewards.gwt.elt.client.orderConfirmation.request.OrderConfirmationResponse;
import com.chinarewards.gwt.elt.client.orderSubmit.model.OrderSubmitClient;
import com.chinarewards.gwt.elt.client.orderSubmit.plugin.OrderSubmitConstants;
import com.chinarewards.gwt.elt.client.support.SessionManager;
import com.chinarewards.gwt.elt.client.win.Win;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class OrderConfirmationPresenterImpl extends BasePresenter<OrderConfirmationDisplay>
		implements OrderConfirmationPresenter {

	final DispatchAsync dispatch;
	final ErrorHandler errorHandler;
	final SessionManager sessionManager;
	final Win win;
	String giftId;
double balance;
	@Inject
	public OrderConfirmationPresenterImpl(EventBus eventBus, DispatchAsync dispatch,
			ErrorHandler errorHandler, SessionManager sessionManager,
			OrderConfirmationDisplay display, Win win) {
		super(eventBus, display);
		this.dispatch = dispatch;
		this.errorHandler = errorHandler;
		this.sessionManager = sessionManager;
		this.win = win;

	}

	@Override
	public void bind() {
		init();

	}

	private void init() {
		display.getMessage().setVisible(false);
		dispatch.execute(new OrderConfirmationRequest(giftId,sessionManager.getSession().getStaffId()),
				new AsyncCallback<OrderConfirmationResponse>() {
					@Override
					public void onFailure(Throwable e) {
						errorHandler.alert(e.getMessage());
					}

					@Override
					public void onSuccess(OrderConfirmationResponse response) {
						GiftClient client=response.getResult();
						display.getShopImage().setUrl("/imageshow?imageName="+client.getPhoto());
						display.setShopText(client.getName());
						display.setTotal(client.getIntegral()+"");
						display.setUnitprice(client.getIntegral()+"");
						display.setSource(client.getSource()+"");
						display.setNumber("1");
						balance=response.getStaffBalance();
						display.setMybalance(balance+"");
						
					}

				});
		
	display.getNumberChange().addChangeHandler(new ChangeHandler() {
		
		@Override
		public void onChange(ChangeEvent event) {
			try {
				int price=Integer.parseInt(display.getUnitprice());
				int num=Integer.parseInt(display.getNumber().getValue());
				display.setTotal((price*num)+"");
				if(balance<price*num)
				{
					display.getMessage().setVisible(true);
					display.getConfirmbuttonObj().setEnabled(false);
				}
				else
				{
					display.getMessage().setVisible(false);
					display.getConfirmbuttonObj().setEnabled(true);
				}
			} catch (Exception e) {
				display.getNumberChange().setText("0");
				display.setTotal("0");
			}

			
		}
	});
	display.getConfirmbutton().addClickHandler(new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			try {
				int price=Integer.parseInt(display.getUnitprice());
				int num=Integer.parseInt(display.getNumber().getValue());
				if(balance<price*num)
				{
					display.getMessage().setVisible(true);
					display.getConfirmbuttonObj().setEnabled(false);
					return;
				}
				OrderConfirmationAddRequest addrequest=new OrderConfirmationAddRequest();
				addrequest.setAddress(display.getAddress().getValue());
				addrequest.setGiftId(giftId);
				addrequest.setName(display.getName().getValue());
				addrequest.setNumber(num);
				addrequest.setOrderDefinition(display.getOrderDefinition().getValue());
				addrequest.setPhone(display.getPhone().getValue());
				addrequest.setStaffId(sessionManager.getSession().getStaffId());
				addrequest.setTotal(price*num);
				addrequest.setUserId(sessionManager.getSession().getToken());
				addrequest.setZipCode(display.getZipCode().getValue());
				addrequest.setUserBalance(balance);
				dispatch.execute(addrequest,
						new AsyncCallback<OrderConfirmationAddResponse>() {
							@Override
							public void onFailure(Throwable e) {
								errorHandler.alert(e.getMessage());
							}

							@Override
							public void onSuccess(OrderConfirmationAddResponse response) {
					
								win.alert("添加成功!");
								OrderSubmitClient orderClient=new OrderSubmitClient();
								orderClient.setOrderId(response.getOrderId());
								orderClient.setGiftImage(response.getGiftImage());
								orderClient.setGiftName(response.getGiftName());
								orderClient.setTotal(response.getTotal());
								orderClient.setIntegral(response.getIntegral());
								orderClient.setSource(response.getSource());
								orderClient.setNumber(response.getNumber());
								orderClient.setUserBalance(response.getUserBalance());
								orderClient.setName(response.getName());
								orderClient.setPhone(response.getPhone());
								orderClient.setZipCode(response.getZipCode());
								orderClient.setOrderDefinition(response.getOrderDefinition());
								orderClient.setAddress(response.getAddress());
								Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										OrderSubmitConstants.EDITOR_ORDERSUBMIT_SEARCH,
										"EDITOR_ORDERSUBMIT_SEARCH_DO_ID", orderClient);
							}

						});
			} catch (Exception e) {
				display.getNumberChange().setText("0");
				display.setTotal("0");
			}

			
		}
	});
	display.getReturnbutton().addClickHandler(new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			Platform.getInstance()
			.getEditorRegistry()
			.openEditor(
					AwardShopListConstants.EDITOR_AWARDSHOPLIST_SEARCH,
					"EDITOR_AWARDSHOPLIST_SEARCH_DO_ID", null);
			
		}
	});
	}

	@Override
	public void initOrderConfirmation(String giftId) {
		this.giftId=giftId;
		
	}



}
