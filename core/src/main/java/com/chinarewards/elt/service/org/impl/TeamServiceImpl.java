package com.chinarewards.elt.service.org.impl;

import com.chinarewards.elt.domain.org.Members;
import com.chinarewards.elt.domain.org.Team;
import com.chinarewards.elt.domain.user.SysUser;
import com.chinarewards.elt.model.common.PageStore;
import com.chinarewards.elt.model.user.UserContext;
import com.chinarewards.elt.model.vo.TeamListVo;
import com.chinarewards.elt.service.org.TeamLogic;
import com.chinarewards.elt.service.org.TeamService;
import com.chinarewards.elt.service.user.UserLogic;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
@Transactional
public class TeamServiceImpl implements TeamService {
	private final TeamLogic teamLogic;
	private final UserLogic userLogic;

	@Inject
	public TeamServiceImpl(TeamLogic teamLogic,UserLogic userLogic) {
		this.userLogic = userLogic;
		this.teamLogic = teamLogic;
		
	}
	@Override
	public Team save(UserContext context, Team team) {
		SysUser caller = userLogic.findUserById(context.getUserId());
		Team teams = teamLogic.save(caller, team);
		return teams;
	}
	
	@Override
	public Members saveMembers(UserContext context, Members members) {
		SysUser caller = userLogic.findUserById(context.getUserId());
		Members member = teamLogic.saveMembers(caller, members);
		return member;
	}
	@Override
	public Team findTeamById(String id) {		
		return teamLogic.findTeamById(id);
	}

	@Override
	public String deleteTeam(String id) throws Exception {		
		return teamLogic.deleteTeam(id);
	}

	@Override
	public PageStore<TeamListVo> teamList(UserContext context, TeamListVo teamVo) {
		SysUser caller = userLogic.findUserById(context.getUserId());
		return teamLogic.teamList(caller, teamVo);
	}

	

}
