package com.chinarewards.gwt.elt.client.rewardItem.presenter;



import java.util.Date;

import net.customware.gwt.dispatch.client.DispatchAsync;

import com.chinarewards.elt.model.reward.base.RequireAutoGenerate;
import com.chinarewards.gwt.elt.client.core.Platform;
import com.chinarewards.gwt.elt.client.core.ui.DialogCloseListener;
import com.chinarewards.gwt.elt.client.frequency.CalculatorSelectFactory;
import com.chinarewards.gwt.elt.client.frequency.NextRewardsDateCalculator;
import com.chinarewards.gwt.elt.client.frequency.RewardStartDateCalculator;
import com.chinarewards.gwt.elt.client.frequency.RewardStartDateCalculator.RewardDateInfo;
import com.chinarewards.gwt.elt.client.frequency.SimpleRewardDateInfo;
import com.chinarewards.gwt.elt.client.mvp.BasePresenter;
import com.chinarewards.gwt.elt.client.mvp.ErrorHandler;
import com.chinarewards.gwt.elt.client.mvp.EventBus;
import com.chinarewards.gwt.elt.client.rewardItem.dialog.FrequencySettingDialog;
import com.chinarewards.gwt.elt.client.rewardItem.event.FrequencySettingEvent;
import com.chinarewards.gwt.elt.client.rewardItem.handler.FrequencySettingHandler;
import com.chinarewards.gwt.elt.client.rewardItem.request.CreateRewardsItemRequest;
import com.chinarewards.gwt.elt.client.rewardItem.request.CreateRewardsItemResponse;
import com.chinarewards.gwt.elt.client.rewards.model.DayFrequencyClient;
import com.chinarewards.gwt.elt.client.rewards.model.DepartmentClient;
import com.chinarewards.gwt.elt.client.rewards.model.FrequencyClient;
import com.chinarewards.gwt.elt.client.rewards.model.MonthFrequencyClient;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsClient;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsItemClient;
import com.chinarewards.gwt.elt.client.rewards.model.RewardsTypeClient;
import com.chinarewards.gwt.elt.client.rewards.model.SpecialCondition;
import com.chinarewards.gwt.elt.client.rewards.model.WeekFrequencyClient;
import com.chinarewards.gwt.elt.client.rewards.model.YearFrequencyClient;
import com.chinarewards.gwt.elt.client.support.SessionManager;
import com.chinarewards.gwt.elt.client.view.constant.ViewConstants;
import com.chinarewards.gwt.elt.util.DateTool;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class RewardsItemCreatePresenterImpl extends
		BasePresenter<RewardsItemCreatePresenter.RewardsItemDisplay> implements
		RewardsItemCreatePresenter {
   
	/**
	 * 是否为修改页，默认为false
	 */
	private boolean isEditPage = false;
	private String rewardsItemId;
	/** 上一次颁奖时间 **/
	private Date lastRewardsDate;
	/**
	 * 选择员工模块
	 */
	private final ChooseStaffBlockPresenter staffBlock;
	private final DispatchAsync dispatcher;
	private final ErrorHandler errorHandler;
	private final Provider<FrequencySettingDialog> freProvider;
	//private final Win win;
	private final SessionManager sessionManager;
	private final RewardStartDateCalculator startDateCalculator;
	private final CalculatorSelectFactory factory;
	
	// 保存上一次修改正确的结束时间
		private Date endDateCopy;
		// 保存下次公布时间副本
		private Date nextPublishCopy;
		// 保存下次公布时间和下次颁奖相隔多少天,默认间隔一天
		private int day = 1;
		// 保存最初的开始时间
		private Date startDateCopy;

		// 不正规修改颁奖时间记录是否有过提示,默认是没有
		private boolean okAndNo = false;
		// 保存最近一次颁奖时间
		private Date rewardsDateCopuy;
		// 保存修改前一次的下次颁奖日期
		private Date nextRewardsDateCopy;
		
		private  Date tmdays;//提前提名的日期

		// 用来判断修改前是否有平率
		private boolean isFrequency = false;
	@Inject
	public RewardsItemCreatePresenterImpl(EventBus eventBus,ChooseStaffBlockPresenter staffBlock,
			RewardsItemDisplay display,DispatchAsync dispatcher,ErrorHandler errorHandler,Provider<FrequencySettingDialog> freProvider
			,RewardStartDateCalculator startDateCalculator,CalculatorSelectFactory factory,SessionManager sessionManager) {
		super(eventBus, display);
		this.dispatcher=dispatcher;
		this.staffBlock = staffBlock;
		this.errorHandler = errorHandler;
		this.freProvider = freProvider;
		this.startDateCalculator = startDateCalculator;
		this.factory = factory;
		this.sessionManager = sessionManager;
	}

	@Override
	public void bind() {
		//绑定事件
		 init();
		//候选人面板显示
		staffBlock.bind();
		display.initStaffBlock(staffBlock.getDisplay().asWidget());
	}
	private void init(){
		//频率设置
		registerHandler(display.getFrequencySettingClick().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						FrequencySettingDialog dialog = freProvider.get();
						// load default values
						dialog.initNewFrequency();

						if (display.getFrequencyObj() != null) {
							dialog.initFrequency(display.getFrequencyObj(),
									true);
						}
						final HandlerRegistration registration = eventBus
								.addHandler(FrequencySettingEvent.getType(),
										new FrequencySettingHandler() {
											@Override
											public void setting(FrequencyClient frequency) {
												doSettingFrequency(frequency);
											}
										});
						Platform.getInstance().getSiteManager()
								.openDialog(dialog, new DialogCloseListener() {
									public void onClose(String dialogId,
											String instanceId) {
										registration.removeHandler();
									}
								});
					}
				}));
		
		// handles the event when the start time is clicked.
					registerHandler(display.getStartTimeChangeHandler()
							.addValueChangeHandler(new ValueChangeHandler<Date>() {
								@Override
								public void onValueChange(ValueChangeEvent<Date> e) {

									Date startDate = display.getStartTime().getValue();
									FrequencyClient frequency = display.getFrequencyObj();
									// 没设定频率的时候，终止！
									if (frequency == null) {
										return;
									}

									Date currentDate = new Date();
									RewardDateInfo info = new SimpleRewardDateInfo(startDate, currentDate,lastRewardsDate);
									// The begin time which to cal
									// the next rewarded time.
									Date startTime = startDateCalculator.calculateStartDate(info);
									NextRewardsDateCalculator cal = factory.getCalculator();
									Date nextRewardsDate = cal.calNextRewardsDate(startTime, frequency);
									display.getStartTime().setValue(nextRewardsDate);
									display.getNextRewardsTime().setValue(nextRewardsDate);

									
									// 修改下次公布时间
									modifyNextPublishTime();
								}
							}));

					

					// 下次公布时间值的改变事件
					registerHandler(display.getNextPublishTime().addValueChangeHandler(
							new ValueChangeHandler<Date>() {
								@Override
								public void onValueChange(ValueChangeEvent<Date> e) {
									Date nextPublishDate = e.getValue();
									Date nextRewardsDate = display.getNextRewardsTime()
											.getValue();
									if (nextRewardsDate.getTime() < nextPublishDate
											.getTime()) {
										display.getNextPublishTime().setValue(
												nextPublishCopy);
									} else {
										nextPublishCopy = nextPublishDate;
										day = DateTool.getIntervalDays(nextPublishDate,
												nextRewardsDate);
									}
								}
							}));

					registerHandler(display.getNextRewardsTime().addValueChangeHandler(
							new ValueChangeHandler<Date>() {
								@Override
								public void onValueChange(ValueChangeEvent<Date> e) {
									Date rewardsDate = display.getNextRewardsTime().getValue();

									modifyNextRewardsSetDate(rewardsDate);
									if (rewardsDateCopuy != null) {
										modifyNextPublishTimeClew();
									}

								}
							}));
					
//					// 添加人数限制输入框即时改动输入值
//					registerHandler(display.getPeopleSizeLimit().addValueChangeHandler(
//							new ValueChangeHandler<String>() {
//
//								@Override
//								public void onValueChange(ValueChangeEvent<String> event) {
//									try {
//										int peopleSizeLimitParse = Integer.parseInt(display
//												.getPeopleSizeLimit().getValue());
//										display.getPeopleSizeLimit().setValue(
//												peopleSizeLimitParse + "");
//									} catch (Exception e) {
//										e.getStackTrace();
//									}
//
//								}
//
//							}));
		
		//保存事件
		registerHandler(display.getSaveClick().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						// validate first!
						if (!validateFirst()) {
							return;
						}
						
						RewardsItemClient rewardsItem = new RewardsItemClient();
						// 基本信息
						rewardsItem.setName(display.getRewardsName().getValue().trim());
						rewardsItem.setType(new RewardsTypeClient(display.getRewardsType(), ""));//
						rewardsItem.setDefinition(display.getDefinition().getValue().trim());
						rewardsItem.setStandard(display.getStandard().getValue().trim());
						// 候选限额
						rewardsItem.setSizeLimit(Integer.parseInt(display.getPeopleSizeLimit().getValue()));
						
						//定义设置奖项和入帐的部门，现为同一部门，以后从session中得到，现为固定部门
						rewardsItem.setBuilderDept("8a83835134598a790134598a7c920008");
						rewardsItem.setAccountDept("8a83835134598a790134598a7c920008");

						// 候选人信息
						rewardsItem.setParticipateInfo(staffBlock.getparticipateInfo());
						rewardsItem.setStartTime(display.getStartTime().getValue());
						
						
						rewardsItem.setPeriodEnable(display.getEnableCbx().getValue());
						//总积分
						rewardsItem.setTotalJF(Integer.parseInt(display.getTotalJF().toString()));
						//每人积分
						rewardsItem.setRewardsFrom(Integer.parseInt(display.getRewardsFrom().toString()));
						
						// 周期性频率信息启用
						if (display.getEnableCbx().getValue() || isEditPage) {
							if(display.getTmday()!=null&&!display.getTmday().toString().equals("")){//把周期性的提名提前的天数放在一个rewardsItem
							 rewardsItem.setTmdays(display.getTmday().intValue());
							}else{
								 rewardsItem.setTmdays(0);
							}
							rewardsItem.setNextTime(display.getNextRewardsTime().getValue());//下次颁奖的时间
							rewardsItem.setFrequency(display.getFrequencyObj());
							rewardsItem.setNextPublishTime(display.getNextPublishTime().getValue());
							rewardsItem	.setAuto(display.getAutoCbx().getValue());
							rewardsItem.setRewardsUnit(display.getRewardsUnit());
							rewardsItem.setHasSpecialCondition(display.getSpecialCbx().getValue());
							rewardsItem.setGeneratedRewards(false);
							if (display.getSpecialCbx().getValue()&& display.getBirthRadio().getValue()) {
								rewardsItem.setCondition(SpecialCondition.birth);
							}
						}else{// 一次性质
							rewardsItem.setNextTime(display.getExpectTime().getValue());//期望的时间
							if(display.getTmdays()!=null&&!display.getTmdays().toString().equals(""))//把一次性的提名提前的天数放在一个rewardsItem
								 rewardsItem.setTmdays(display.getTmdays().intValue());
							else
								 rewardsItem.setTmdays(0);
							}

					
						if (!isEditPage) {
							rewardsItem.setId("");
							doSave(rewardsItem);
						} else {
							rewardsItem.setId(rewardsItemId);
						//	doEdit(rewardsItem);//修改功能
						}
					}
				}));
	}
	
	private void doSave(RewardsItemClient rewardsItem) {

		dispatcher.execute(new CreateRewardsItemRequest(rewardsItem),
				new AsyncCallback<CreateRewardsItemResponse>() {
					@Override
					public void onFailure(Throwable t) {
						errorHandler.alert(t.toString());
					}

					@Override
					public void onSuccess(CreateRewardsItemResponse response) {
						Window.alert("添加成功");
//						Platform.getInstance()
//								.getEditorRegistry()
//								.closeEditor(
//										RewardsConstants.EDITOR_REWARDS_ITEM_CREATE,
//										instanceId);

					}
				});
	}
	
	// setting a new frequency.
		private void doSettingFrequency(FrequencyClient frequency) {
			display.showFrequencyInfo(frequency);
			Date startDate = display.getStartTime().getValue();
			
			Date currentDate = new Date();
			// 开始时间设为依据时间
			RewardDateInfo info = new SimpleRewardDateInfo(startDate, currentDate, lastRewardsDate);
			Date startTime = startDateCalculator.calculateStartDate(info);
			// 初始化时默认开始时间是当日时间
			if (startTime == null) {
				startTime = new Date();
			}
			NextRewardsDateCalculator cal = factory.getCalculator();
			Date nextRewardsDate = cal.calNextRewardsDate(startTime, frequency);
			startDate = startDate == null ? new Date() : startDate;
			if (!isEditPage || (startDate.getTime() > nextRewardsDate.getTime())) {
				// 开始时间和下次颁奖时间一样
				display.getStartTime().setValue(nextRewardsDate);
			} else if (!isEditPage|| (nextRewardsDate.getTime() >= startDateCopy.getTime())) {
				if (isFrequency) {
					display.getStartTime().setValue(startDateCopy);
				} else {
					display.getStartTime().setValue(nextRewardsDate);
				}
			} else if (isEditPage&& nextRewardsDate.getTime() <= startDateCopy.getTime()) {
				display.getStartTime().setValue(nextRewardsDate);
			}

			display.getNextRewardsTime().setValue(nextRewardsDate);
			
			

		}
		private void modifyNextRewardsSetDate(Date rewardsDate) {
			FrequencyClient frequency = display.getFrequencyObj();
			boolean toD = false;
			// 没设定频率的时候，终止！
			if (frequency == null) {
				return;
			}
			
			Date currentDate = new Date();
			RewardDateInfo info = new SimpleRewardDateInfo(rewardsDate, currentDate, lastRewardsDate);
			// The begin time which to cal
			// the next rewarded time.
			Date startTime = startDateCalculator.calculateStartDate(info);
			NextRewardsDateCalculator cal = factory.getCalculator();
			Date nextRewardsDate = cal.calNextRewardsDate(startTime, frequency);
			display.getNextRewardsTime().setValue(nextRewardsDate);

			rewardsDate = display.getNextRewardsTime().getValue();
			if (rewardsDate.getTime() < startTime.getTime()) {
					display.getStartTime().setValue(rewardsDate);
				} else {
					display.getStartTime().setValue(startTime);
				}
			
			if (toD) {
				
				endDateCopy = nextRewardsDate;
			}

			// 修改下次公布时间
			modifyNextPublishTime();
		}
		// 修改下次颁奖时间
		private void modifyNextPublishTime() {
			Date startDate = display.getNextRewardsTime().getValue();
			Date date = DateTool.addSomeDay(startDate, 0 - day);
			display.getNextPublishTime().setValue(date);
			nextPublishCopy = date;
		
		}
		
		private void modifyNextPublishTimeClew() {
			DateTimeFormat format = DateTimeFormat.getFormat(ViewConstants.date_format);
			Date NextRewardsTimeDate = display.getNextRewardsTime().getValue();
			if ((NextRewardsTimeDate.getTime() <= rewardsDateCopuy.getTime())
					&& !okAndNo) {
				okAndNo = true;
				Window.confirm("建议下次颁奖时间应该在上次颁奖时间(" + format.format(rewardsDateCopuy)
								+ ")之后！<br/>按“确定”回复到以前的设置！<br/>按“取消”使用新的时间！");

			} else if (NextRewardsTimeDate.getTime() > rewardsDateCopuy.getTime()) {
				okAndNo = false;
				nextRewardsDateCopy = NextRewardsTimeDate;

			}
		}
		
		//奖项验证方法
				private boolean validateFirst() {
					boolean flag = true;
					StringBuilder errorMsg = new StringBuilder();
					if (display.getRewardsName().getValue() == null
							|| "".equals(display.getRewardsName().getValue().trim())) {
						errorMsg.append("请填写奖项名称!<br>");

						flag = false;
					}
					
				

					
					// 员工选择
					if (staffBlock.getDisplay().isSomeone().getValue() == true) {
						if (staffBlock.getDisplay().getRealOrginzationIds() == null) {
							errorMsg.append("请选择候奖员工!<br>");
							flag = false;

						}
						if (staffBlock.getDisplay().getRealOrginzationIds() != null
								&& staffBlock.getDisplay().getRealOrginzationIds().size() == 0) {
							errorMsg.append("请选择候奖员工!<br>");
							flag = false;

						}
					}

					if (display.getRewardsType() == null|| "".equals(display.getRewardsType().trim())) {
						errorMsg.append("请选择奖项类型!<br>");
						flag = false;
					}
		            
					if (display.getTotalJF() == null|| display.getTotalJF().intValue() < 0) {
						errorMsg.append("总积分额度出错，总积分要是整数!<br>");
						flag = false;
					}else	if (display.getRewardsFrom() == null
								|| display.getRewardsFrom().intValue() < 0) {
							errorMsg.append("每人积分额度出错，积分要是整数!<br>");
							flag = false;
					} else	if (display.getTotalJF().intValue()/Integer.parseInt(display.getPeopleSizeLimit().getValue())!=display.getRewardsFrom().intValue() ) {
						errorMsg.append("每人积分额度出错，总积分除以人数不等于每人得的积分数!<br>");
						flag = false;
					}
					if (display.getRewardsFrom() != null) {
						if (display.getRewardsFrom().intValue() == 0) {
							errorMsg.append("个人得分不能为“0”!<br>");
							flag = false;
						}
						if (display.getRewardsFrom().intValue() > display.getTotalJF().intValue()) {
							errorMsg.append("总积分不能小于个人得分!<br>");
							flag = false;
						}
					}
					
					if (display.getStartTime().getValue() == null|| "".equals(display.getStartTime().getValue())) {
						errorMsg.append("开始时间不能为空<br>");
						flag = false;
					}
					
							

					try {

							int limitPeople = Integer.parseInt(display.getPeopleSizeLimit()
									.getValue());
							if (display.getPeopleSizeLimit().getValue() != null) {
								if (limitPeople == 0 || limitPeople < 0) {
									errorMsg.append("请正确填写获奖名额(正整数)!<br>");
									flag = false;
								}
							}
						} catch (Exception e) {
							errorMsg.append("请正确填写获奖名额!<br>");
							flag = false;
						}
					
                   //周期性
					if (display.getEnableCbx().getValue()) {
						
						if (display.getNextRewardsTime().getValue() == null|| "".equals(display.getNextRewardsTime().getValue())) {
							errorMsg.append("下次颁奖时间不能为空!<br>");
							flag = false;
						}else if(display.getStartTime().getValue().getTime()>display.getNextRewardsTime().getValue().getTime()){
							errorMsg.append("开始时间要小于或等于下次颁奖时间<br>");
						}
						if (display.getTmday() != null&& display.getTmday().intValue() < 0) {
							errorMsg.append(" 要提前的天数是整数!<br>");
							flag = false;
					     }
						// 生日奖校验
						if (display.getSpecialCbx().getValue()
								&& display.getBirthRadio().getValue()) {
							if (display.getFrequencyObj() == null) {
								errorMsg.append("请选择频率!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof WeekFrequencyClient) {
								errorMsg.append("生日奖不能为按周的频率!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof YearFrequencyClient) {
								errorMsg.append("生日奖不能为按年的频率!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof DayFrequencyClient
									&& display.getFrequencyObj().getInterval() > 200) {
								errorMsg.append("生日奖按日的频率，最大只能设到每200日<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof MonthFrequencyClient
									&& display.getFrequencyObj().getInterval() > 12) {
								errorMsg.append("生日奖按月的频率，最大只能设到每12月<br>");
								flag = false;
							}
						} else {
							// 普通有频率奖校验
							if (display.getFrequencyObj() == null) {
								errorMsg.append("请选择频率!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof WeekFrequencyClient
									&& display.getFrequencyObj().getInterval() > 20) {
								errorMsg.append("按周的频率，最大只能设到每20周!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof YearFrequencyClient
									&& display.getFrequencyObj().getInterval() > 5) {
								errorMsg.append("按年的频率，最大只能设到每5年!<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof DayFrequencyClient
									&& display.getFrequencyObj().getInterval() > 200) {
								errorMsg.append("按日的频率，最大只能设到每200日<br>");
								flag = false;
							} else if (display.getFrequencyObj() instanceof MonthFrequencyClient
									&& display.getFrequencyObj().getInterval() > 36) {
								errorMsg.append("按月的频率，最大只能设到每36月<br>");
								flag = false;
							}
						}

						
						if (!display.getAutoCbx().getValue()) {
							if (display.getNextPublishTime().getValue() == null) {
								errorMsg.append("请填写下一次公布颁奖时间!<br>");
								flag = false;
							} else if (display.getNextPublishTime().getValue() != null
									&& display.getNextRewardsTime().getValue() != null
									&& display.getNextPublishTime().getValue()
											.after(display.getNextRewardsTime().getValue())) {
								errorMsg.append("下一次公布奖励时间必须在下一次颁奖时间之前!<br>");
								flag = false;
							}
						}
					}else{//一次性
						if (display.getExpectTime().getValue() == null|| "".equals(display.getExpectTime().getValue())) {
							errorMsg.append("预计颁奖时间不能为空!<br>");
							flag = false;
						}else if(display.getStartTime().getValue().getTime()>=display.getExpectTime().getValue().getTime()){
							errorMsg.append("开始时间要小于预计颁奖时间<br>");
						}
						if (display.getTmdays() != null&& display.getTmdays().intValue() < 0) {
							errorMsg.append(" 要提前的天数是整数!<br>");
							flag = false;
					     }
						
					}

					

					if (!flag) {

						errorHandler.alert(errorMsg.toString());
						// win.alert(errorMsg.toString(), true); // true = HTML escape
					}

					return flag;
				}

}
