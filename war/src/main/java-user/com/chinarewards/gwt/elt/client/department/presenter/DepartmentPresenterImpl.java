package com.chinarewards.gwt.elt.client.department.presenter;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.chinarewards.gwt.elt.client.breadCrumbs.presenter.BreadCrumbsPresenter;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.department.model.DepartmentVo;
import com.chinarewards.gwt.elt.client.department.plugin.DepartmentConstants;
import com.chinarewards.gwt.elt.client.department.request.EditDepartmentRequest;
import com.chinarewards.gwt.elt.client.department.request.EditDepartmentResponse;
import com.chinarewards.gwt.elt.client.department.request.SearchDepartmentByIdRequest;
import com.chinarewards.gwt.elt.client.department.request.SearchDepartmentByIdResponse;
import com.chinarewards.gwt.elt.client.department.util.DepartmentAdapterClient;
import com.chinarewards.gwt.elt.client.mvp.BasePresenter;
import com.chinarewards.gwt.elt.client.mvp.ErrorHandler;
import com.chinarewards.gwt.elt.client.mvp.EventBus;
import com.chinarewards.gwt.elt.client.support.SessionManager;
import com.chinarewards.gwt.elt.client.win.Win;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DepartmentPresenterImpl extends
		BasePresenter<DepartmentPresenter.DepartmentDisplay> implements
		DepartmentPresenter {
	String instanceId;// 修改时传过来的ID

	private String thisAction;
	private String departmentId;
	//
	private final DispatchAsync dispatcher;
	private final ErrorHandler errorHandler;
	private final SessionManager sessionManager;

	private final Win win;

	private final BreadCrumbsPresenter breadCrumbs;

	@Inject
	public DepartmentPresenterImpl(EventBus eventBus,
			DepartmentDisplay display, DispatchAsync dispatcher,
			ErrorHandler errorHandler, SessionManager sessionManager, Win win,
			BreadCrumbsPresenter breadCrumbs) {
		super(eventBus, display);
		this.dispatcher = dispatcher;
		this.errorHandler = errorHandler;
		this.sessionManager = sessionManager;
		this.win = win;
		this.breadCrumbs = breadCrumbs;
	}

	@Override
	public void bind() {
		registerEvent();

		if (DepartmentConstants.ACTION_DEPARTMENT_ADD.equals(thisAction)) {
			breadCrumbs.loadChildPage("新建部门");
			initSave();
		} else if (DepartmentConstants.ACTION_DEPARTMENT_EDIT
				.equals(thisAction)) {
			initEdit();
			breadCrumbs.loadChildPage("编辑部门");
		} else {
			win.alert("未定义的方法");
		}

		display.setBreadCrumbs(breadCrumbs.getDisplay().asWidget());
	}

	// 绑定事件
	private void registerEvent() {
		// 保存事件
		registerHandler(display.getSaveClick().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						if (!validateSubmit()) {
							return;
						}

						DepartmentVo departmentVo = DepartmentAdapterClient
								.adapterDisplay(display);

						if (DepartmentConstants.ACTION_DEPARTMENT_ADD
								.equals(thisAction)) {
							departmentVo.setId(null);
							doSave(departmentVo);
						} else if (DepartmentConstants.ACTION_DEPARTMENT_EDIT
								.equals(thisAction)) {
							departmentVo.setId(departmentId);
							doEdit(departmentVo);
						} else {
							win.alert("未定义的方法");
						}
					}

					private void doSave(DepartmentVo department) {
						dispatcher.execute(new EditDepartmentRequest(
								department, sessionManager.getSession()),
								new AsyncCallback<EditDepartmentResponse>() {
									@Override
									public void onFailure(Throwable t) {
										errorHandler.alert(t.toString());
									}

									@Override
									public void onSuccess(
											EditDepartmentResponse response) {
										Window.alert("添加成功");
										// if(instanceId!=null||!instanceId.equals(""))
										Platform.getInstance()
												.getEditorRegistry()
												.openEditor(
														DepartmentConstants.EDITOR_DEPARTMENTLIST_SEARCH,
														DepartmentConstants.ACTION_DEPARTMENT_LIST,
														instanceId);
									}
								});
					}

					private void doEdit(DepartmentVo department) {
						if (Window.confirm("确定修改?")) {
							dispatcher.execute(
									new EditDepartmentRequest(department,
											sessionManager.getSession()),
									new AsyncCallback<EditDepartmentResponse>() {
										@Override
										public void onFailure(Throwable t) {
											Window.alert("修改失败");
											Platform.getInstance()
													.getEditorRegistry()
													.closeEditor(
															DepartmentConstants.EDITOR_DEPARTMENT_EDIT,
															instanceId);
										}

										@Override
										public void onSuccess(
												EditDepartmentResponse arg0) {
											Window.alert("修改成功");
											Platform.getInstance()
													.getEditorRegistry()
													.openEditor(
															DepartmentConstants.EDITOR_DEPARTMENTLIST_SEARCH,
															DepartmentConstants.ACTION_DEPARTMENT_LIST,
															instanceId);
										}
									});
						}
					}

				}));

		registerHandler(display.getBackClick().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						Platform.getInstance()
								.getEditorRegistry()
								.openEditor(
										DepartmentConstants.EDITOR_DEPARTMENTLIST_SEARCH,
										DepartmentConstants.ACTION_DEPARTMENT_LIST,
										instanceId);
					}
				}));

	}

	// 验证方法
	private boolean validateSubmit() {
		boolean flag = true;
		StringBuilder errorMsg = new StringBuilder();
		// if (display.getName().getValue() == null
		// || "".equals(display.getName().getValue().trim())) {
		// errorMsg.append("请填写部门名称!<br>");
		// flag = false;
		// }

		if (!flag) {
			win.alert(errorMsg.toString());
		}

		return flag;
	}

	private void initEdit() {
		if (departmentId != null) {
			dispatcher.execute(new SearchDepartmentByIdRequest(departmentId),
					new AsyncCallback<SearchDepartmentByIdResponse>() {
						@Override
						public void onFailure(Throwable arg0) {
							errorHandler.alert("查询出错!");
							Platform.getInstance()
									.getEditorRegistry()
									.closeEditor(
											DepartmentConstants.EDITOR_DEPARTMENT_EDIT,
											instanceId);
						}

						@Override
						public void onSuccess(
								SearchDepartmentByIdResponse response) {
							DepartmentVo departmentVo = response
									.getDepartment();
							clear();
							display.initEditDepartment(departmentVo);
						}
					});
		} else {
			System.err.println("------------缺少departmentId----------");
		}

	}

	private void initSave() {
		display.initAddDepartment(new DepartmentVo());
	}

	private void clear() {
		display.clear();
	}

	public void setId(String id) {
		this.departmentId = id;
	}

	@Override
	public void initEditor(String departmentId, String thisAction) {
		this.departmentId = departmentId;
		this.thisAction = thisAction;
	}

}
