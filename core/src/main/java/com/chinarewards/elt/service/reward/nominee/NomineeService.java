package com.chinarewards.elt.service.reward.nominee;

import java.util.List;

import com.chinarewards.elt.domain.reward.person.NomineeLot;
import com.chinarewards.elt.model.reward.exception.JudgeException;

public interface NomineeService {
	public NomineeLot addNomineeLotToReward(String rewardId,List<String> staffIds) throws JudgeException;
}