package com.neuedu.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neuedu.dao.HomeworkMapper;
import com.neuedu.dao.PapermappingMapper;
import com.neuedu.entity.Homework;
import com.neuedu.entity.HomeworkExample;
import com.neuedu.entity.HomeworkExample.Criteria;
import com.neuedu.entity.Paper;
import com.neuedu.entity.Papermapping;
import com.neuedu.entity.PapermappingExample;
import com.neuedu.entity.User;
import com.neuedu.service.HomeworkService;
import com.neuedu.service.PaperService;
@Service
public class HomeworkServiceImpl implements HomeworkService {

	@Autowired
	HomeworkMapper homeworkMapper;
	
	@Autowired
	PaperService paperService;

	@Autowired
	PapermappingMapper papermappingMapper;
	@Override
	public int addHomework(Homework homework) {
		// TODO Auto-generated method stub
		int i=homeworkMapper.insert(homework);
		return 0;
	}

	@Override
	public List<Homework> selectHomeworkByGroupid(int groupid) {
		// TODO Auto-generated method stub
		HomeworkExample homeworkExample=new HomeworkExample();
		Criteria c1=homeworkExample.createCriteria();
		c1.andGroupidEqualTo(groupid);
		List<Homework> homeworklist=homeworkMapper.selectByExample(homeworkExample);
		return homeworklist;
	}

	@Override
	public List<Paper> selectPaperFromHomework(int groupid) {
		// TODO Auto-generated method stub
		List<Homework> hl=selectHomeworkByGroupid(groupid);
		//System.out.println(groupid+"=======");
		List<Paper> pa=new ArrayList<Paper>();
		for(Homework h:hl) {
			Paper p=paperService.selectPaperbyId(h.getPaperid());
			//System.out.println(p.getPaperid()+"-------");
			//Paper p=papermappingMapper.s
			pa.add(p);
		}
		return pa;
	}

	@Override
	public List<Integer> selectIfDo(int groupid,int userid) {
		// TODO Auto-generated method stub
		List<Paper> palist=selectPaperFromHomework(groupid);
		List<Integer> ifdolist=new ArrayList<Integer>();
		for(Paper p:palist) {
//			System.out.println(p.getPaperid());
//			System.out.println(p.getDoflag());
			PapermappingExample papermappingExample =new PapermappingExample();
			com.neuedu.entity.PapermappingExample.Criteria c1=papermappingExample.createCriteria();
			c1.andPaperidEqualTo(p.getPaperid());
			System.out.println(p.getPaperid());
			c1.andExamineeEqualTo(userid);
			List<Papermapping> pmlist=papermappingMapper.selectByExample(papermappingExample);
			
			Paper pc=paperService.selectPaperbyId(pmlist.get(0).getUserpaperid());
			ifdolist.add(pc.getDoflag());
			System.out.println(pc.getDoflag());
		}
		return ifdolist;
	}

	/*
	 * @Override public User seleceUserFromHomework(int groupid) { // TODO
	 * Auto-generated method stub List<Homework>
	 * homeworklist=selectHomeworkByGroupid(groupid); User u; return null; }
	 */

}
