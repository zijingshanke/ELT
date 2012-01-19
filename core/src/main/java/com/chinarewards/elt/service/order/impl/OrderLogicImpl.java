package com.chinarewards.elt.service.order.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.elt.dao.gift.GiftDao;
import com.chinarewards.elt.dao.order.OrderDao;
import com.chinarewards.elt.domain.gift.Gift;
import com.chinarewards.elt.domain.order.Order;
import com.chinarewards.elt.domain.user.SysUser;
import com.chinarewards.elt.model.common.PageStore;
import com.chinarewards.elt.model.gift.search.GiftListVo;
import com.chinarewards.elt.model.order.search.OrderStatus;
import com.chinarewards.elt.model.order.search.OrderListVo;
import com.chinarewards.elt.service.order.OrderLogic;
import com.chinarewards.elt.util.DateUtil;
import com.chinarewards.elt.util.StringUtil;
import com.google.inject.Inject;

public class OrderLogicImpl implements OrderLogic{
	private OrderDao orderDao;
	private GiftDao giftDao;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	@Inject
	protected OrderLogicImpl(OrderDao orderDao,GiftDao giftDao){
		this.orderDao = orderDao;
		this.giftDao = giftDao;
	}
	
	@Override
	public Order save(SysUser caller, Order order) {
		Date currTime = DateUtil.getTime();
		String orderCode = DateUtil.formatData("yyyyMMddhhmmss", currTime);
		if (StringUtil.isEmptyString(order.getId())) {
			// Create
			order.setDeleted(0);//正常状态，没有删除为0
			order.setUserId(caller.getId());
			order.setName(caller.getStaff().getName());
			order.setExchangeDate(currTime);
			order.setRecorddate(currTime);
			order.setRecorduser(caller.getUserName());
			order.setStatus(OrderStatus.INITIAL);//初始时为没有付积分
			order.setOrderCode(orderCode);//用当前时间作为订单编号
			orderDao.save(order);
		} else {
			// Update
			order = orderDao.findById(Order.class, order.getId());
			order.setRecorddate(currTime);
			order.setRecorduser(caller.getUserName());
			orderDao.update(order);
		}

		return order;
	}
	

	@Override
	public Order findOrderById(String id) {
		
		return  orderDao.findById(Order.class, id);
	}

	@Override
	public String deleteOrder(SysUser caller,String id) {
		Date currTime = DateUtil.getTime();
		Order order = orderDao.findById(Order.class, id);
		order.setDeleted(1);
		order.setRecorddate(currTime);
		order.setRecorduser(caller.getUserName());
		order= orderDao.update(order);
		return order.getId();
	}

	@Override
	public PageStore<OrderListVo> OrderList(SysUser caller, OrderListVo orderVo) {
		
		PageStore<Order> pageStore = new PageStore<Order>();
		
		pageStore.setResultCount(orderDao.countOrder(orderVo));
		List<Order> OrderList = orderDao.OrderList(orderVo);
		List<OrderListVo> OrderVoList = new ArrayList<OrderListVo>();
		for (Order order : OrderList) {
			OrderVoList.add(convertFromOrderToVo(order));
		}
		PageStore<OrderListVo> storeVo = new PageStore<OrderListVo>();
		storeVo.setResultCount(pageStore.getResultCount());
		storeVo.setResultList(OrderVoList);

		return storeVo;
	}
	private OrderListVo convertFromOrderToVo(Order order) {
		OrderListVo orderVo = new OrderListVo();
		GiftListVo giftVo = new GiftListVo();
		orderVo.setAmount(order.getAmount());
		orderVo.setGiftId(order.getGiftId());
		orderVo.setId(order.getId());
		orderVo.setIntegral(order.getIntegral());
		orderVo.setName(order.getName());
		orderVo.setOrderCode(order.getOrderCode());
		orderVo.setRecorddate(order.getRecorddate());
		orderVo.setStatus(order.getStatus());
		orderVo.setUserId(order.getUserId());
		//================下面为礼品的信息
		Gift gift = giftDao.findById(Gift.class,order.getGiftId());
		giftVo.setAddress(gift.getAddress());
		giftVo.setBusiness(gift.getBusiness());
		giftVo.setDeleted(gift.isDeleted());
		giftVo.setExplains(gift.getExplains());
		giftVo.setId(gift.getId());
		giftVo.setIndate(gift.getIndate());
		giftVo.setName(gift.getName());
		giftVo.setPhoto(gift.getPhoto());
		giftVo.setSource(gift.getSource());
		giftVo.setStatus(gift.getStatus());
		giftVo.setStock(gift.getStock());
		giftVo.setTell(gift.getTell());
		giftVo.setType(gift.getType());
        giftVo.setRecorddate(gift.getRecorddate());
        giftVo.setRecorduser(gift.getRecorduser());
        giftVo.setUpdatetime(gift.getUpdatetime());
      	orderVo.setGiftvo(giftVo);
		return orderVo;
	}
	@Override
	public String updateStatus(SysUser caller,String id,OrderStatus status) {
		Date currTime = DateUtil.getTime();
		Order order = orderDao.findById(Order.class, id);
		order.setStatus(status);
		order.setRecorddate(currTime);
		order.setRecorduser(caller.getUserName());
		order= orderDao.update(order);
		return order.getId();
	}

	
}
