package com.neuedu.service;

import java.util.List;

import com.neuedu.entity.Groupmember;
import com.neuedu.entity.Groups;
import com.neuedu.entity.User;

public interface GroupmemberService {

	int addMember(Groupmember gm);
	List<Groupmember> selectmemGroups(int userid);
	List<Groups> selectGroups(int userid);
	List<Integer> selectUser(int groupid);
	List<User> selectUserEntity(int groupid);
	int deleteMember(int userid);
	int ifContainInGroup(Groupmember gm);
	void updateMemberid(Groupmember gm,int changeid);
}
