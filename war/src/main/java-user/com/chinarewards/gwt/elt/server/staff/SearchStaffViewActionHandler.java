package com.chinarewards.gwt.elt.server.staff;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.slf4j.Logger;

import com.chinarewards.elt.domain.org.Staff;
import com.chinarewards.elt.service.staff.IStaffService;
import com.chinarewards.gwt.elt.client.staffList.model.StaffListCriteria.StaffStatus;
import com.chinarewards.gwt.elt.client.staffView.request.StaffViewRequest;
import com.chinarewards.gwt.elt.client.staffView.request.StaffViewResponse;
import com.chinarewards.gwt.elt.server.BaseActionHandler;
import com.chinarewards.gwt.elt.server.logger.InjectLogger;
import com.google.inject.Inject;

/**
 * The handler to correspond the StaffViewRequest.
 * 
 * @author nicho
 * @since 2012年2月15日 10:04:40
 */
public class SearchStaffViewActionHandler extends
		BaseActionHandler<StaffViewRequest, StaffViewResponse> {

	@InjectLogger
	Logger logger;

	IStaffService staffService;


	@Inject
	public SearchStaffViewActionHandler(IStaffService staffService) {
		this.staffService = staffService;
	}

	@Override
	public StaffViewResponse execute(StaffViewRequest request,
			ExecutionContext response) throws DispatchException {
		StaffViewResponse staffResponse=new StaffViewResponse();
		Staff staff=staffService.findStaffById(request.getStaffId());
		staffResponse.setStaffId(staff.getId());
		staffResponse.setStaffNo(staff.getJobNo());
		staffResponse.setStaffName(staff.getName());
		if(staff.getDepartment()!=null)
		staffResponse.setDepartmentName(staff.getDepartment().getName());
		staffResponse.setPhoto(staff.getPhoto());
		staffResponse.setJobPosition(staff.getJobPosition());
		staffResponse.setLeadership(staff.getLeadership());
		staffResponse.setPhone(staff.getPhone());
		staffResponse.setEmail(staff.getEmail());
		staffResponse.setDob(staff.getDob());
		staffResponse.setStatus(StaffStatus.valueOf(staff.getStatus().toString()));
		return staffResponse;
	}

	@Override
	public Class<StaffViewRequest> getActionType() {
		return StaffViewRequest.class;
	}

	@Override
	public void rollback(StaffViewRequest request,
			StaffViewResponse response, ExecutionContext context)
			throws DispatchException {
	}

}
