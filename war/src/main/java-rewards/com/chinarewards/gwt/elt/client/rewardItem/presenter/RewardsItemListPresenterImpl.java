package com.chinarewards.gwt.elt.client.rewardItem.presenter;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.dataprovider.RewardsItemListViewAdapter;
import com.chinarewards.gwt.elt.client.mvp.BasePresenter;
import com.chinarewards.gwt.elt.client.mvp.ErrorHandler;
import com.chinarewards.gwt.elt.client.mvp.EventBus;
import com.chinarewards.gwt.elt.client.rewardItem.plugin.RewardsItemConstants;
import com.chinarewards.gwt.elt.client.rewardItem.request.ActivationRewardsItemRequest;
import com.chinarewards.gwt.elt.client.rewardItem.request.ActivationRewardsItemResponse;
import com.chinarewards.gwt.elt.client.rewardItem.request.DeleteRewardsItemRequest;
import com.chinarewards.gwt.elt.client.rewardItem.request.DeleteRewardsItemResponse;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsItemClient;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsItemCriteria;
import com.chinarewards.gwt.elt.client.support.SessionManager;
import com.chinarewards.gwt.elt.client.ui.HyperLinkCell;
import com.chinarewards.gwt.elt.client.view.constant.ViewConstants;
import com.chinarewards.gwt.elt.client.widget.EltNewPager;
import com.chinarewards.gwt.elt.client.widget.EltNewPager.TextLocation;
import com.chinarewards.gwt.elt.client.widget.GetValue;
import com.chinarewards.gwt.elt.client.widget.ListCellTable;
import com.chinarewards.gwt.elt.client.widget.Sorting;
import com.chinarewards.gwt.elt.client.win.Win;
import com.chinarewards.gwt.elt.client.win.confirm.ConfirmHandler;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * 
 * 
 * @author lw
 * @since 0.2.0 2011-12-23
 */
public class RewardsItemListPresenterImpl extends
		BasePresenter<RewardsItemListPresenter.RewardsItemListDisplay>
		implements RewardsItemListPresenter {

	EltNewPager pager;
	ListCellTable<RewardsItemClient> resultTable;
	RewardsItemListViewAdapter listViewAdapter;

	final DispatchAsync dispatch;
	final ErrorHandler errorHandler;
	final SessionManager sessionManager;
    final Win win;
	DateTimeFormat dateFormatAll = DateTimeFormat.getFormat(ViewConstants.date_format_all);
	DateTimeFormat dateFormat = DateTimeFormat.getFormat(ViewConstants.date_format);


	// 是否部门管理员
	boolean isHr = false;
	boolean isDepartmentManager = false;

	@Inject

	public RewardsItemListPresenterImpl(EventBus eventBus,Win win,
			RewardsItemListDisplay display, DispatchAsync dispatch,
			ErrorHandler errorHandler, SessionManager sessionManager) {
		super(eventBus, display);
		this.dispatch = dispatch;
		this.errorHandler = errorHandler;
		this.sessionManager = sessionManager;
		this.win = win;

	}

	@Override
	public void bind() {
		init();
		buildTable();

		registerHandler(display.getSearchClick().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						doSearch();
					}
				}));
		registerHandler(display.getAddBut().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Platform.getInstance()
						.getEditorRegistry()
						.openEditor(
								RewardsItemConstants.EDITOR_REWARDSITEM_ADD,
								"EDITOR_REWARDSITEM_ADD_DO_ID", null);
			}
		}));
	}

	private void buildTable() {
		resultTable = new ListCellTable<RewardsItemClient>();
		initTableColumns();
		pager = new EltNewPager(TextLocation.CENTER);
		pager.setDisplay(resultTable);
		resultTable.setWidth(ViewConstants.page_width);
		resultTable.setPageSize(ViewConstants.per_page_number_in_dialog);
		listViewAdapter = new RewardsItemListViewAdapter(dispatch,
				errorHandler, sessionManager,display);
		listViewAdapter.addDataDisplay(resultTable);
		resultTable.getColumn(0).setCellStyleNames("divTextLeft");
		display.getDataContainer().clear();
		display.getDataContainer().add(resultTable);
		display.getDataPager().add(pager);
	}

	// init department name
	private void init() {
		// if (!GWT.isScript()) {
		// isDepartmentManager = false;
		// } else {
		// List<UserRoleVo> roleList =
		// Arrays.asList(sessionManager.getSession().getUserRoles());
		// if (roleList.contains(UserRoleVo.CORP_ADMIN)) { isHr = true;
		// } else if (roleList.contains(UserRoleVo.DEPT_MGR)) {
		// isDepartmentManager = true;
		// }
		// if (isHr) {
		// //display.showDept(null);
		// } else if (isDepartmentManager) {
		// dispatch.execute(new DepartmentIdRequest(),
		// new AsyncCallback<DepartmentIdResponse>() {
		//
		// @Override
		// public void onFailure(Throwable t) {
		// errorHandler.alert(t);
		// }
		//
		// @Override
		// public void onSuccess(DepartmentIdResponse resp) {
		// display.showDept(resp.getDeptIds());
		// }
		// });
		// } else {
		// // local test
		// display.showDept(null);
		// }
		// }

		// comboTree = new DepartmentComboTree(dispatch, errorHandler,
		// sessionManager);
		// display.getDepartmentPanel().add(comboTree);
		// deptId = comboTree.getSelectedItem().getId();
		// setRewardsTypeList();
		Map<String, String> map = new HashMap<String, String>();
		map.put("true", "已激活");
		map.put("false", "未激活");
		display.initStatus(map);
		
	}
	
	private void initTableColumns() {
		Sorting<RewardsItemClient> ref = new Sorting<RewardsItemClient>() {
			@Override
			public void sortingCurrentPage(
					Comparator<RewardsItemClient> comparator) {
				listViewAdapter.sortCurrentPage(comparator);
			}

			@Override
			public void sortingAll(String sorting, String direction) {
				listViewAdapter.sortFromDateBase(sorting, direction);
			}
		};
		resultTable.addColumn("奖项名称", new HyperLinkCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient object) {
						return object.getName();
					}
				}, new FieldUpdater<RewardsItemClient, String>() {
					@Override
					public void update(int index, RewardsItemClient object,
							String value) {
						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										RewardsItemConstants.EDITOR_REWARDSITEM_View,
										"EDITOR_REWARDS_ITEM_VIEW"
												+ object.getId(), object);
					}
				}, ref, "name");

		resultTable.addColumn("自动", new TextCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient object) {
						return (object.isAuto()) ? "是" : "否";
					}
				});
		resultTable.addColumn("频率", new TextCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient object) {
						return (object.isPeriodEnable()) ? "有" : "无";
					}
				});

		resultTable.addColumn("创建时间", new DateCell(dateFormatAll),
				new GetValue<RewardsItemClient, Date>() {
					@Override
					public Date getValue(RewardsItemClient object) {
						return object.getCreateAt();
					}
				});



		resultTable.addColumn("应用次数", new TextCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient rewards) {
						return rewards.getDegree() + "";
					}
				}, ref, "nexRunBatchTime");
		resultTable.addColumn("修改", new HyperLinkCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient arg0) {
						return "修改";
					}
				}, new FieldUpdater<RewardsItemClient, String>() {
					@Override
					public void update(int index, final RewardsItemClient object,	String value) {
						
						Platform.getInstance()
						.getEditorRegistry()
						.openEditor(
								RewardsItemConstants.EDITOR_REWARDSITEM_ADD,
								"EDITOR_REWARDS_ITEM_ADD"+ object.getId(), object);
					

					}
				});
		resultTable.addColumn("查看", new HyperLinkCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient arg0) {
						return "查看详细";
					}
				}, new FieldUpdater<RewardsItemClient, String>() {
					@Override
					public void update(int index, RewardsItemClient object,
							String value) {
						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										RewardsItemConstants.EDITOR_REWARDSITEM_View,
										"EDITOR_REWARDS_ITEM_VIEW"
												+ object.getId(), object);
					}
				});
		resultTable.addColumn("操作", new HyperLinkCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient arg0) {
						return "删除";
					}
				}, new FieldUpdater<RewardsItemClient, String>() {
					@Override
					public void update(int index, final RewardsItemClient object,	String value) {
						if (object.isEnabled() == true){
							win.alert(object.getName()+"奖项已激活，不能删除");
						}else{
							win.confirm("删除提示", "确定删除吗？", new ConfirmHandler() {
								
								@Override
								public void confirm() {
									dispatch.execute(new DeleteRewardsItemRequest(object.getId(),sessionManager.getSession().getToken()),
											new AsyncCallback<DeleteRewardsItemResponse>() {

												@Override
												public void onFailure(Throwable t) {
													win.alert(t.getMessage());
												}

												@Override
												public void onSuccess(DeleteRewardsItemResponse resp) {
													win.alert(resp.getName() + "已删除!");
													doSearch();
												}
											});
								}
							});
							
						}
					}
				});

		resultTable.addColumn("操作", new HyperLinkCell(),
				new GetValue<RewardsItemClient, String>() {
					@Override
					public String getValue(RewardsItemClient arg0) {

						if (arg0.isEnabled() == false)
							return "应用";
						else
							return "已激活";
					}
				}, new FieldUpdater<RewardsItemClient, String>() {
					@Override
					public void update(int index,
							final RewardsItemClient object, String value) {
						if (object.isEnabled() == false) {

							if(object.getStartTime()==null)
							{
								win.alert("失败，"+object.getName()+"资料不完整，影响其正常运作，请完善后再应用");
								return;
							}

							win.confirm("提示", "确定激活？", new ConfirmHandler() {
								@Override
								public void confirm() {
									activationRewardItem(object.getId());
								}
							});

						} else {
							win.alert("失败，" + object.getName() + "已经处于激活状态");
							/*
							 * if (Window.confirm("为了测试,重新激活,再运行一次batch?")) {
							 * activationRewardItem(object.getId()); }
							 */
						}
					}
				});

	}

	public void activationRewardItem(String rewardsItemId) {

		dispatch.execute(new ActivationRewardsItemRequest(rewardsItemId,
				sessionManager.getSession().getToken()),
				new AsyncCallback<ActivationRewardsItemResponse>() {

					@Override
					public void onFailure(Throwable t) {
						win.alert(t.getMessage());
					}

					@Override
					public void onSuccess(ActivationRewardsItemResponse resp) {
						win.alert(resp.getName() + "--已激活!");
						doSearch();
					}
				});
	}

	public void deleteRewardItem(String rewardsItemId) {

		dispatch.execute(new DeleteRewardsItemRequest(rewardsItemId,
				sessionManager.getSession().getToken()),
				new AsyncCallback<DeleteRewardsItemResponse>() {

					@Override
					public void onFailure(Throwable t) {
						win.alert(t.getMessage());
					}

					@Override
					public void onSuccess(DeleteRewardsItemResponse resp) {
						win.alert(resp.getName() + "已删除!");
						doSearch();
					}
				});
	}

	public void doSearch() {
		RewardsItemCriteria criteria = new RewardsItemCriteria();
		// criteria.setDepartmentId(display.getBuildDept());
		criteria.setSubDepartmentChoose(display.getChooseSubDepartment().getValue());
		criteria.setName(display.getSearchName().getValue());
		criteria.setCreateTime(display.getCreateTime().getValue());
		criteria.setCreateTimeEnd(display.getCreateTimeEnd().getValue());
		if(display.getStatus().equals("true"))
		   criteria.setEnabled(true);
		else
			criteria.setEnabled(false);
		listViewAdapter.setCriteria(criteria);
		listViewAdapter.reloadToFirstPage();
	}
}
