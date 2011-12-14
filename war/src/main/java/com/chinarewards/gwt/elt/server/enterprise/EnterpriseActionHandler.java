package com.chinarewards.gwt.elt.server.enterprise;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.DispatchException;
//import com.chinarewards.gwt.elt.model.enterprise.EnterprisePersistence;
import com.chinarewards.elt.domain.enterprise.Enterprise;
import com.chinarewards.elt.service.enterprise.IEnterpriseService;
import com.chinarewards.gwt.elt.client.enterprise.EnterpriseRequest;
import com.chinarewards.gwt.elt.client.enterprise.EnterpriseResponse;
import com.chinarewards.gwt.elt.client.enterprise.model.EnterpriseVo;
import com.chinarewards.gwt.elt.model.ClientException;
import com.chinarewards.gwt.elt.server.BaseActionHandler;
import com.google.inject.Inject;

public class EnterpriseActionHandler extends
		BaseActionHandler<EnterpriseRequest, EnterpriseResponse> {

	IEnterpriseService enterpriseService;

	@Inject
	public EnterpriseActionHandler(IEnterpriseService enterpriseService) {
		this.enterpriseService = enterpriseService;
	}

	@Override
	public Class<EnterpriseRequest> getActionType() {
		return EnterpriseRequest.class;
	}

	@Override
	public EnterpriseResponse execute(EnterpriseRequest request, ExecutionContext context)
			throws DispatchException {
		    EnterpriseVo vo = request.getEnterprise();
		    Enterprise enterprise = new Enterprise();
		    enterprise.setAddress(vo.getAddress());
		    enterprise.setCellphone(vo.getCellphone());
		    enterprise.setCorporation(vo.getCorporation());
		    enterprise.setEmail(vo.getEmail());
		    enterprise.setEnterpriseName(vo.getEnterpriseName());
		    enterprise.setFax(vo.getFax());
		    enterprise.setLinkman(vo.getLinkman());
		    enterprise.setRemark(vo.getRemark());
		    enterprise.setTell(vo.getTell());
		    enterprise.setWeb(vo.getWeb());
		    enterprise.setId(vo.getId());
		   
	        String s = enterpriseService.addEnterpriseInfo(enterprise);
			if (s != null) {
			EnterpriseResponse resp = new EnterpriseResponse();
			resp.setToken("注册成功");
			return resp;
		} else {
			throw new ClientException("注册失败!");
		}
	}
	
	
	@Override
	public void rollback(EnterpriseRequest action, EnterpriseResponse result,
			ExecutionContext context) throws DispatchException {

	}

}
