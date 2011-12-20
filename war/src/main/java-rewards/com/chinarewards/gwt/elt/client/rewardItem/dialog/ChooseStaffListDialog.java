package com.chinarewards.gwt.elt.client.rewardItem.dialog;

import java.util.List;

import com.chinarewards.gwt.elt.client.chooseStaff.presenter.ChooseStaffListPresenter;
import com.chinarewards.gwt.elt.client.core.ui.impl.AbstractDialog;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ChooseStaffListDialog extends AbstractDialog {

	final Provider<ChooseStaffListPresenter> presenterProvider;

	ChooseStaffListPresenter presenter;
String rewardId;
	@Inject
	public ChooseStaffListDialog(
			Provider<ChooseStaffListPresenter> presenterProvider) {
		super("staff.choosee", "staff.choosee");
		this.presenterProvider = presenterProvider;
		presenter = presenterProvider.get();
		presenter.setDialog(this);

		init();
	}

	public void init() {
		setTitle("选择员工");
		presenter.setRewardId(rewardId);
		presenter.bind();
	}

	@Override
	public Widget asWidget() {
		return presenter.getDisplay().asWidget();
	}

	@Override
	public boolean beforeClose() {
		presenter.unbind();
		return true;
	}

	public void setNominee(boolean isLimitByNominee, boolean isChooseAll,
			List<String> orgIds) {
		presenter.setNominee(isLimitByNominee, isChooseAll, orgIds);
	}
	public void setRewardId(String rewardId) {
		this.rewardId=rewardId;
		presenter.setRewardId(rewardId);
	}
	public void setStaffOnly(boolean staffOnly) {
		presenter.setStaffOnly(staffOnly);
	}
}
