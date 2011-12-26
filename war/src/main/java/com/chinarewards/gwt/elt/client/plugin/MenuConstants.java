/**
 * 
 */
package com.chinarewards.gwt.elt.client.plugin;

/**
 * Defined menu constants data here. Please careful the order of constants
 * parameter. It will be the order of menu list.
 * 
 * @author Cream
 * @since 0.2.0 2010-12-28
 */
public abstract class MenuConstants {

	private static int order = 0;

	// Welcome
	public static final int MENU_ORDER_REWARDS_ENTRY = order++;

	// 奖励创建
//	public static final int MENU_ORDER_REWARDS_CREATE = order++;

	// 奖励查询
	public static final int MENU_ORDER_REWARDS_SEARCH = order++;

	// 奖项创建
//	public static final int MENU_ORDER_REWARDS_ITEM_CREATE = order++;

	// 奖项查询
	public static final int MENU_ORDER_REWARDS_ITEM_SEARCH = order++;

	// 账号管理
	public static final int MENU_ORDER_USER_SEARCH = order++;

	// 员工信息维护
	public static final int MENU_ORDER_STAFF_SEARCH = order++;
	
	// 员工等级
	public static final int MENU_ORDER_STAFF_LEVEL = order++;

	// 员工添加
	public static final int MENU_ORDER_STAFF_CREATE = order++;

	// 员工信息更新
	public static final int MENU_ORDER_STAFF_UPDATE = order++;

	// 奖励类型维护
	public static final int MENU_ORDER_TRANSACTION_TYPE_SEARCH = order++;

	// 员工奖励充值
	public static final int MENU_ORDER_TRANSACTION_DO = order++;

	// 员工奖励查询
	public static final int MENU_ORDER_TRANSACTION_SEARCH = order++;

	// 员工奖励查询
	public static final int MENU_ORDER_DEPT_SEARCH = order++;

	// 奖励报表
	public static final int MENU_ORDER_TRANSACTION_REPORTS_EXPORT = order++;

	// 交易明细报表
	public static final int MENU_ORDER_TRADE_DETAIL_REPORTS = order++;

	// 奖励类型比例统计
	public static final int MENU_ORDER_TRADE_TYPE_RATIO_STATISTIC = order++;

	// 奖励类型金额统计
	public static final int MENU_ORDER_TRADE_TYPE_MONEY_STATISTIC = order++;

	// 奖励模板导入
	public static final int MENU_ORDER_IMPORT_REWARDS_TEMPLATE = order++;

	// 整个系统奖励模版
	public static final int MENU_ORDER_ALL_REWARDS_TEMPLATE = order++;

	// 新建预算
	public static final int MENU_ORDER_CREATE_BUDGET = order++;

	// 查询预算列表
	public static final int MENU_ORDER_BUDGET_LIST = order++;

	// 企业信息维护
	public static final int MENU_ORDER_CORPORATION_SEARCH = order++;

	// 企业内部信息发布
	public static final int MENU_ORDER_CORPORATION_INNER_INFO_PUBLISH = order++;
	//hr注册
	public static final int MENU_ORDER_Hr_SEARCH = order++;
	//新建奖项
	public static final int MENU_ORDER_REWARDSITEM_ADD = order++;
	//列表查询
	public static final int MENU_ORDER_REWARDSITEM_List = order++;
	//提名
	public static final int MENU_ORDER_MONINATE_SEARCH = order++;
	

	//获奖
	public static final int MENU_ORDER_AWARDREWARD_SEARCH = order++;
	//获奖详细
	public static final int MENU_ORDER_DETAILSOFAWARD_SEARCH = order++;
	
}
