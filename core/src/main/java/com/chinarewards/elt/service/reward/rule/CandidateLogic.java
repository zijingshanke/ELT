package com.chinarewards.elt.service.reward.rule;

import java.util.List;

import com.chinarewards.elt.domain.reward.person.Candidate;
import com.chinarewards.elt.domain.reward.rule.CandidateRule;
import com.chinarewards.elt.domain.user.SysUser;

/**
 * Provides some methods to deal with {@link Candidate}
 * 
 * @author yanxin
 * @since 1.0
 */
public interface CandidateLogic {

	/**
	 * Add candidate to reward by the specified CandidateRule.
	 * 
	 * @param caller
	 * @param rewardId
	 * @param candidateRule
	 */
	public void AddCandidateToReward(SysUser caller, String rewardId,
			CandidateRule candidateRule);

	/**
	 * Get list of candidate from the specified Reward.
	 * 
	 * @param rewardId
	 * @return
	 */
	public List<Candidate> getCandidatesFromReward(String rewardId);

	/**
	 * 被提名人次数+1
	 */
	public void updateCandidatesCount(List<String> CandidateIds);
}
