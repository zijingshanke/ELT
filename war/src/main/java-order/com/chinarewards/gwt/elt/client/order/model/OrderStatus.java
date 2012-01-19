package com.chinarewards.gwt.elt.client.order.model;


public  enum OrderStatus {

	/* 初始时，为没付积分 */
	 INITIAL,
	  
	/* 没发货 */	
	NUSHIPMENTS,
	
	/* 已发货 */	
	SHIPMENTS,
	
	/* 确认发货 */	
	AFFIRM,
	
	/* 问题订单 */
	ERRORORDER;
}
