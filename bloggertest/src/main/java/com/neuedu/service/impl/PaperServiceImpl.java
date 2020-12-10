package com.neuedu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.neuedu.dao.BankMapper;
import com.neuedu.dao.Bank_fillMapper;
import com.neuedu.dao.Bank_judgeMapper;
import com.neuedu.dao.Bank_multipleMapper;
import com.neuedu.dao.Bank_shortanswerMapper;
import com.neuedu.dao.Bank_singleMapper;
import com.neuedu.dao.PaperMapper;
import com.neuedu.dao.Paper_fillMapper;
import com.neuedu.dao.Paper_judgeMapper;
import com.neuedu.dao.Paper_multipleMapper;
import com.neuedu.dao.Paper_shortanswerMapper;
import com.neuedu.dao.Paper_singleMapper;
import com.neuedu.dao.PapermappingMapper;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Bank_fill;
import com.neuedu.entity.Bank_judge;
import com.neuedu.entity.Bank_multiple;
import com.neuedu.entity.Bank_shortanswer;
import com.neuedu.entity.Bank_single;
import com.neuedu.entity.Groups;
import com.neuedu.entity.Paper;
import com.neuedu.entity.PaperExample;
import com.neuedu.entity.User;
import com.neuedu.entity.PaperExample.Criteria;
import com.neuedu.entity.Paper_fill;
import com.neuedu.entity.Paper_fillchoice;
import com.neuedu.entity.Paper_judge;
import com.neuedu.entity.Paper_multiple;
import com.neuedu.entity.Paper_multiplechoice;
import com.neuedu.entity.Paper_shortanswer;
import com.neuedu.entity.Paper_shortanswer_mov;
import com.neuedu.entity.Paper_shortanswer_pic;
import com.neuedu.entity.Paper_single;
import com.neuedu.entity.Paper_singlechoice;
import com.neuedu.entity.Papermapping;
import com.neuedu.entity.PapermappingExample;
import com.neuedu.service.BankService;
import com.neuedu.service.ExamService;
import com.neuedu.service.GroupmemberService;
import com.neuedu.service.PaperService;
import com.neuedu.service.Paper_fillService;
import com.neuedu.service.Paper_fillchoiceService;
import com.neuedu.service.Paper_judgeService;
import com.neuedu.service.Paper_multipleService;
import com.neuedu.service.Paper_multiplechoiceService;
import com.neuedu.service.Paper_shortanswerService;
import com.neuedu.service.Paper_shortanswer_movService;
import com.neuedu.service.Paper_shortanswer_picService;
import com.neuedu.service.Paper_singleService;
import com.neuedu.service.Paper_singlechoiceService;
import com.neuedu.service.UserService;


@Service
public class PaperServiceImpl implements PaperService {
	
	@Autowired
	PaperMapper paperMapper;
	@Autowired
	GroupmemberService groupmemberService;
	@Autowired
	PapermappingMapper papermappingMapper;
	@Autowired
	BankService bankService;
	@Autowired
	Bank_singleMapper bank_singleMapper;
	@Autowired
	BankMapper bankMapper;
	@Autowired
	Bank_multipleMapper bank_multipleMapper;
	@Autowired
	Bank_shortanswerMapper bank_shortanswerMapper;
	@Autowired
	Bank_judgeMapper bank_judgeMapper;
	@Autowired
	Bank_fillMapper bank_fillMapper;
	@Autowired
	Paper_fillMapper paper_fillMapper;
	@Autowired
	Paper_fillService paper_fillService;
	@Autowired
	Paper_singleService paper_singleService;
	@Autowired
	Paper_multipleService paper_multipleService;
	@Autowired
	Paper_shortanswerService paper_shortanswerService;
	@Autowired
	Paper_judgeService paper_judgeService;
	@Autowired
	Paper_singleMapper paper_singleMapper;
	@Autowired
	Paper_multipleMapper paper_multipleMapper;
	@Autowired
	Paper_shortanswerMapper paper_shortanswerMapper;
	@Autowired
	Paper_judgeMapper paper_judgeMapper;
	@Autowired
	UserService userService;
	
	@Autowired
	Paper_fillchoiceService paper_fillchoiceService;
	
	@Autowired
	Paper_multiplechoiceService paper_multiplechoiceService;
	
	@Autowired
	Paper_singlechoiceService paper_singlechoiceService;
	
	@Autowired
	Paper_shortanswer_movService paper_shortanswer_movService;
	@Autowired
	Paper_shortanswer_picService paper_shortanswer_picService;
	@Autowired
	ExamService examService;
	
	PaperExample paperExample;
	PapermappingExample papermappingExample;
	
	@Override
	public Paper selectPaperbyId(Integer i) {
		Paper paper=paperMapper.selectByPrimaryKey(i);
		return paper;
	}
	@Override
	public List<Paper> selectAllPaper(User u) {
		// TODO Auto-generated method stub
		List<Paper> papers=paperMapper.selectAllPaper(u.getUserid());
		return papers;
	}
	@Override
	public List<Paper> selectAllPaperByTeacher(User u) {
		List<Paper> papers=paperMapper.selectAllPaperByTeacher(u.getUserid());
		List<Paper> result=new ArrayList<>();
		for(Paper paper:papers) {
			if(paper.getExaminee()==null) {
				result.add(paper);
			}
		}
		return result;
	}
	@Override
	public int addPaper(Paper p) {
		// TODO Auto-generated method stub
		int i=paperMapper.insert(p);
		return i;
	}
	@Override
	public List<Paper> selectAllPaperWaitingToFinish(User user) {
		// TODO Auto-generated method stub
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperWaitingToFinish=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
			
			Date beginTime =paperItem.getBegintime();
			Date endTime=paperItem.getEndtime();
			Date now=new Date();
			if(paperItem!=null) {
			if(beginTime!=null&&endTime!=null&&now.compareTo(beginTime)>0&&endTime.compareTo(now)>0&&paperItem.getDoflag()==0) {
				paperWaitingToFinish.add(selectAllPaper.get(index));
			}
			}
		   index++;
		}
		return paperWaitingToFinish;
		}
		return null;
	  }
	@Override
	public List<Paper> selectAllPaperFinished(User user) {
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperFinished=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
		System.out.println("已完成");
		if(paperItem!=null) {
			if(paperItem.getDoflag()!=null&&2==paperItem.getDoflag()) {
				paperFinished.add(selectAllPaper.get(index));
			}
		}
		   index++;
		}
		return  paperFinished;
		}
		return null;
		
	}
	@Override
	public List<Paper> selectAllPaperWaitingToStart(User user) {
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperWaitingToStart=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
			Date beginTime =paperItem.getBegintime();
			System.out.println("待开始:"+paperItem);
			Date now=new Date();
			if(paperItem!=null) {
			if(beginTime!=null&&now.compareTo(beginTime)<0) {
				paperWaitingToStart.add(selectAllPaper.get(index));
			}
			}
		   index++;
		}
		return  paperWaitingToStart;
		}
		return null;
		
	}
	@Override
	public List<Paper> selectAllPaperOutOfDate(User user) {
		// TODO Auto-generated method stub
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperOutOfDate=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
			System.out.println("过期:"+paperItem);
			Date endTime=paperItem.getEndtime();
			System.out.println("过期时间:"+endTime);
			Date now= new Date();
			System.out.println("currentTime："+now);
			if(paperItem!=null) {
			if(endTime!=null&&now.compareTo(endTime)>0&&paperItem.getDoflag()!=null&&2!=paperItem.getDoflag()) {
				System.out.print("进来了");
				paperOutOfDate.add(selectAllPaper.get(index));
			
			}
			}
		   index++;
		}
		return  paperOutOfDate;
		}
			
		return null;
	}
	@Override
	public List<Paper> selectAllPaperInitial(User user) {
		// TODO Auto-generated method stub
		PaperExample paperExample=new PaperExample();
		Criteria c1=paperExample.createCriteria();
		c1.andExamineeIsNull();
		c1.andWinderEqualTo(user.getUserid());
//		Criteria c2=paperExample.createCriteria();
//		c2.andWinderEqualTo(user.getUserid());
//		paperExample.or(c2);
		List<Paper> paperlist=paperMapper.selectByExample(paperExample);
		return paperlist;
	}
	@Override
	public int addPaperExamniee(Paper p) {
		// TODO Auto-generated method stub
		
		return 0;
	}
	@Override
	public List<Integer> addPaper(Paper p, Groups g) {
		// TODO Auto-generated method stub
		//System.out.println("to");
		List<Integer> useridlist=groupmemberService.selectUser(g.getGroupid());
		Paper paper=paperMapper.selectByPrimaryKey(p.getPaperid());
		Papermapping pm=new Papermapping();
		paper.setType1(p.getType1());
		paper.setBegintime(p.getBegintime());
		paper.setEndtime(p.getEndtime());
		paper.setDuration(p.getDuration());
		//paper.setPaperid(null);
		//paper.setUndonenum(0);
		//paper.setType1(p.getType1());
		paper.setDoflag(0);
		List<Integer> paperidList=new ArrayList<Integer>();
		int paperid;
		for(int i=0;i<useridlist.size();i++) {
			//paper.setPaperid(null);
			//System.out.println("to1");
//			paper.setExaminee(useridlist.get(i));
//			int rows=paperMapper.insert(paper);
//			paperid=paper.getPaperid();
			User user=new User();
			user.setUserid(useridlist.get(i));
			//User user=userService.selectUserByPrimaryKey(u);
			if(!user.getUserid().equals(g.getOwner())) {
//			User user=new User();
//			user.setUserid(useridlist.get(i));
				examService.creatExampaper(p, user);
			}
			//System.out.println(paperid+"pid");
//			paperidList.add(paperid);
//			pm.setPaperid(p.getPaperid());
//			pm.setExaminee(useridlist.get(i));
//			pm.setUserpaperid(paperid);
//			pm.setType(0);
			
			//papermappingMapper.insert(pm);
		}
		return paperidList;
	}

	@Override
	public HashMap<String, Integer> getSingleKnowledgeAndNumber(List<Bank> banks) {
		HashMap<String,Integer> hashmap=new HashMap<>();
		List<Bank_single> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_single> sin=bank_singleMapper.getAllbyBankid(bank.getBankid());
			//System.out.println("papersize"+sin.size());
			if(!(sin==null)) {
				for(Bank_single single:sin) {
					singles.add(single);
				}
			}
			
		}
		//System.out.println("singlesize"+singles.size());
		for(Bank_single single:singles) {
			String knowledge=single.getqKnowledge();
			if(hashmap.containsKey(knowledge)) {
				int n=hashmap.get(knowledge);
				hashmap.put(knowledge, ++n);
			}else {
				hashmap.put(knowledge, 1);
			}
		}
//		for(String key:hashmap.keySet()) {
//			System.out.println("key= "+key+" and value= "+hashmap.get(key));
//		}
		return hashmap;
	}
	@Override
	public HashMap<String, Integer> getMutipleKnowledgeAndNumber(List<Bank> banks) {
		HashMap<String,Integer> hashmap=new HashMap<>();
		List<Bank_multiple> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_multiple> sin=bank_multipleMapper.getAllbyBankid(bank.getBankid());
			//System.out.println("papersize"+sin.size());
			if(!(sin==null)) {
				for(Bank_multiple single:sin) {
					singles.add(single);
				}
			}
			
		}
		//System.out.println("singlesize"+singles.size());
		for(Bank_multiple single:singles) {
			String knowledge=single.getqKnowledge();
			if(hashmap.containsKey(knowledge)) {
				int n=hashmap.get(knowledge);
				hashmap.put(knowledge, ++n);
			}else {
				hashmap.put(knowledge, 1);
			}
		}
//		for(String key:hashmap.keySet()) {
//			System.out.println("key= "+key+" and value= "+hashmap.get(key));
//		}
		return hashmap;
	}
	@Override
	public HashMap<String, Integer> getJudgeKnowledgeAndNumber(List<Bank> banks) {
		HashMap<String,Integer> hashmap=new HashMap<>();
		List<Bank_judge> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_judge> sin=bank_judgeMapper.getAllbyBankid(bank.getBankid());
			//System.out.println("papersize"+sin.size());
			if(!(sin==null)) {
				for(Bank_judge single:sin) {
					singles.add(single);
				}
			}
			
		}
		//System.out.println("singlesize"+singles.size());
		for(Bank_judge single:singles) {
			String knowledge=single.getqKnowledge();
			if(hashmap.containsKey(knowledge)) {
				int n=hashmap.get(knowledge);
				hashmap.put(knowledge, ++n);
			}else {
				hashmap.put(knowledge, 1);
			}
		}
//		for(String key:hashmap.keySet()) {
//			System.out.println("key= "+key+" and value= "+hashmap.get(key));
//		}
		return hashmap;
	}
	@Override
	public HashMap<String, Integer> getFillKnowledgeAndNumber(List<Bank> banks) {
		HashMap<String,Integer> hashmap=new HashMap<>();
		List<Bank_fill> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_fill> sin=bank_fillMapper.getAllbyBankid(bank.getBankid());
			//System.out.println("papersize"+sin.size());
			if(!(sin==null)) {
				for(Bank_fill single:sin) {
					singles.add(single);
				}
			}
			
		}
		//System.out.println("singlesize"+singles.size());
		for(Bank_fill single:singles) {
			String knowledge=single.getqKnowledge();
			if(hashmap.containsKey(knowledge)) {
				int n=hashmap.get(knowledge);
				hashmap.put(knowledge, ++n);
			}else {
				hashmap.put(knowledge, 1);
			}
		}
//		for(String key:hashmap.keySet()) {
//			System.out.println("key= "+key+" and value= "+hashmap.get(key));
//		}
		return hashmap;
	}
	@Override
	public HashMap<String, Integer> getShortanswerKnowledgeAndNumber(List<Bank> banks) {
		HashMap<String,Integer> hashmap=new HashMap<>();
		List<Bank_shortanswer> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_shortanswer> sin=bank_shortanswerMapper.getAllbyBankid(bank.getBankid());
			//System.out.println("papersize"+sin.size());
			if(!(sin==null)) {
				for(Bank_shortanswer single:sin) {
					singles.add(single);
				}
			}
			
		}
		//System.out.println("singlesize"+singles.size());
		for(Bank_shortanswer single:singles) {
			String knowledge=single.getqKnowledge();
			if(hashmap.containsKey(knowledge)) {
				int n=hashmap.get(knowledge);
				hashmap.put(knowledge, ++n);
			}else {
				hashmap.put(knowledge, 1);
			}
		}
		for(String key:hashmap.keySet()) {
			System.out.println("key= "+key+" and value= "+hashmap.get(key));
		}
		return hashmap;
	}
	@Override
	public Paper intelligentFillPaper(HashMap<String, Integer> hashmap1, int score, List<Bank> banks,Paper paper) {
		//遍历得到题库下各个知识点及对应的bankfill
		HashMap<String,List<Bank_fill>> hashmap=new HashMap<>();
		List<Bank_fill> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_fill> sin=bank_fillMapper.getAllbyBankid(bank.getBankid());
			if(!(sin==null)) {
				for(Bank_fill single:sin) {
					singles.add(single);
				}
			}
		}
		for(String key:hashmap1.keySet()) {
			List<Bank_fill> fills=new ArrayList<>();
			for(Bank_fill fill:singles) {
				if(fill.getqKnowledge().equals(key)) {
					fills.add(fill);
				}
			}
			hashmap.put(key, fills);
		}
		for(String key:hashmap.keySet()) {
			//System.out.println("key= "+key+" and value= "+hashmap.get(key));
			if(hashmap1.get(key)!=0) {
				List<Bank_fill> bankfill=hashmap.get(key);
				if(hashmap1.get(key)==bankfill.size()) {
					for(Bank_fill f:bankfill) {
						paper_fillService.addPaper_fill2(f, score, paper);
						
					}
				}else {
					Random random =new Random();
					//List<Integer> nums=new ArrayList<>();
					int removenum=bankfill.size()-hashmap1.get(key);
					//随机删掉removenum个幸运试题
					for(int i=0;i<removenum;i++) {
						int n = random.nextInt(removenum);
						bankfill.remove(n);
					}
					for(Bank_fill f:bankfill) {
						paper_fillService.addPaper_fill2(f, score, paper);
						
					}
					//nums.add(n);
				}
			
			}
		}

		return null;
	}
	@Override
	public Paper intelligentSinglePaper(HashMap<String, Integer> hashmap1, int score, List<Bank> banks,Paper paper) {
		HashMap<String,List<Bank_single>> hashmap=new HashMap<>();
		List<Bank_single> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_single> sin=bank_singleMapper.getAllbyBankid(bank.getBankid());
			if(!(sin==null)) {
				for(Bank_single single:sin) {
					singles.add(single);
				}
			}
		}
				for(String key:hashmap1.keySet()) {
					List<Bank_single> fills=new ArrayList<>();
					for(Bank_single fill:singles) {
						if(fill.getqKnowledge().equals(key)) {
							fills.add(fill);
						}
					}
					hashmap.put(key, fills);
				}

				for(String key:hashmap.keySet()) {
					//System.out.println("key= "+key+" and value= "+hashmap.get(key));
					if(hashmap1.get(key)!=0) {
						List<Bank_single> bankfill=hashmap.get(key);
						if(hashmap1.get(key)==bankfill.size()) {
							for(Bank_single f:bankfill) {
								paper_singleService.addPaper_single(f, score, paper);
								
							}
						}else {
							Random random =new Random();
							//List<Integer> nums=new ArrayList<>();
							int removenum=bankfill.size()-hashmap1.get(key);
							//随机删掉removenum个幸运试题
							for(int i=0;i<removenum;i++) {
								int n = random.nextInt (removenum);
								bankfill.remove(n);
							}
							for(Bank_single f:bankfill) {
								paper_singleService.addPaper_single(f, score, paper);
								
							}
							//nums.add(n);
						}
					
					}
				}

				return null;
	}
	@Override
	public Paper intelligentMultiplePaper(HashMap<String, Integer> hashmap1, int score, List<Bank> banks,Paper paper) {
		HashMap<String,List<Bank_multiple>> hashmap=new HashMap<>();
		List<Bank_multiple> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_multiple> sin=bank_multipleMapper.getAllbyBankid(bank.getBankid());
			if(!(sin==null)) {
				for(Bank_multiple single:sin) {
					singles.add(single);
				}
			}
		}
		for(String key:hashmap1.keySet()) {
			List<Bank_multiple> fills=new ArrayList<>();
			for(Bank_multiple fill:singles) {
				if(fill.getqKnowledge().equals(key)) {
					fills.add(fill);
				}
			}
			hashmap.put(key, fills);
		}
		for(String key:hashmap.keySet()) {
			//System.out.println("key= "+key+" and value= "+hashmap.get(key));
			if(hashmap1.get(key)!=0) {
				List<Bank_multiple> bankfill=hashmap.get(key);
				if(hashmap1.get(key)==bankfill.size()) {
					for(Bank_multiple f:bankfill) {
						paper_multipleService.addPaper_multiple2(f, score, paper);
						
					}
				}else {
					Random random =new Random();
					//List<Integer> nums=new ArrayList<>();
					int removenum=bankfill.size()-hashmap1.get(key);
					//随机删掉removenum个幸运试题
					for(int i=0;i<removenum;i++) {
						int n = random.nextInt (removenum);
						bankfill.remove(n);
					}
					for(Bank_multiple f:bankfill) {
						paper_multipleService.addPaper_multiple2(f, score, paper);
						
					}
					//nums.add(n);
				}
			
			}
		}

		return null;
	}
	@Override
	public Paper intelligentJudgePaper(HashMap<String, Integer> hashmap1, int score, List<Bank> banks,Paper paper) {
		HashMap<String,List<Bank_judge>> hashmap=new HashMap<>();
		List<Bank_judge> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_judge> sin=bank_judgeMapper.getAllbyBankid(bank.getBankid());
			if(!(sin==null)) {
				for(Bank_judge single:sin) {
					singles.add(single);
				}
			}
		}
		for(String key:hashmap1.keySet()) {
			List<Bank_judge> fills=new ArrayList<>();
			for(Bank_judge fill:singles) {
				if(fill.getqKnowledge().equals(key)) {
					fills.add(fill);
				}
			}
			hashmap.put(key, fills);
		}
		for(String key:hashmap.keySet()) {
			//System.out.println("key= "+key+" and value= "+hashmap.get(key));
			if(hashmap1.get(key)!=0) {
				List<Bank_judge> bankfill=hashmap.get(key);
				if(hashmap1.get(key)==bankfill.size()) {
					for(Bank_judge f:bankfill) {
						paper_judgeService.addPaper_judge2(f, score, paper);
						
					}
				}else {
					Random random =new Random();
					//List<Integer> nums=new ArrayList<>();
					int removenum=bankfill.size()-hashmap1.get(key);
					//随机删掉removenum个幸运试题
					for(int i=0;i<removenum;i++) {
						int n = random.nextInt (removenum);
						bankfill.remove(n);
					}
					for(Bank_judge f:bankfill) {
						paper_judgeService.addPaper_judge2(f, score, paper);
						
					}
					//nums.add(n);
				}
			
			}
		}
		return null;
	}
	@Override
	public Paper intelligentShortanswerPaper(HashMap<String, Integer> hashmap1, int score, List<Bank> banks,Paper paper) {
		HashMap<String,List<Bank_shortanswer>> hashmap=new HashMap<>();
		List<Bank_shortanswer> singles=new ArrayList<>();
		for(Bank bank:banks) {
			List<Bank_shortanswer> sin=bank_shortanswerMapper.getAllbyBankid(bank.getBankid());
			if(!(sin==null)) {
				for(Bank_shortanswer single:sin) {
					singles.add(single);
				}
			}
		}
		for(String key:hashmap1.keySet()) {
			List<Bank_shortanswer> fills=new ArrayList<>();
			for(Bank_shortanswer fill:singles) {
				if(fill.getqKnowledge().equals(key)) {
					fills.add(fill);
				}
			}
			hashmap.put(key, fills);
		}
		for(String key:hashmap.keySet()) {
			//System.out.println("key= "+key+" and value= "+hashmap.get(key));
			if(hashmap1.get(key)!=0) {
				List<Bank_shortanswer> bankfill=hashmap.get(key);
				if(hashmap1.get(key)==bankfill.size()) {
					for(Bank_shortanswer f:bankfill) {
						paper_shortanswerService.addPaper_shortanswer2(f, score, paper);
						
					}
				}else {
					Random random =new Random();
					//List<Integer> nums=new ArrayList<>();
					int removenum=bankfill.size()-hashmap1.get(key);
					//随机删掉removenum个幸运试题
					for(int i=0;i<removenum;i++) {
						int n = random.nextInt(removenum);
						bankfill.remove(n);
					}
					for(Bank_shortanswer f:bankfill) {
						paper_shortanswerService.addPaper_shortanswer2(f, score, paper);
						
					}
					//nums.add(n);
				}
			
			}
		}

		return null;
	}
	@Override
	public List<Paper> selectAllPaperDone(int paperid) {
		// TODO Auto-generated method stub
		PapermappingExample papermappingExample =new PapermappingExample();
		com.neuedu.entity.PapermappingExample.Criteria c1=papermappingExample.createCriteria();
		c1.andPaperidEqualTo(paperid);
		List<Papermapping> pmlist=papermappingMapper.selectByExample(papermappingExample);
		List<Paper> plist=new ArrayList<Paper>();
		for(Papermapping pm:pmlist) {
			Paper p=paperMapper.selectByPrimaryKey(pm.getPaperid());
			if(p.getDoflag()==1) {
				plist.add(p);
			}
		}
		return plist;
	}
	@Override
	public List<Paper> selectAllPaperUndo(int paperid) {
		// TODO Auto-generated method stub
		System.out.println(paperid);
		PapermappingExample papermappingExample =new PapermappingExample();
		com.neuedu.entity.PapermappingExample.Criteria c1=papermappingExample.createCriteria();
		c1.andPaperidEqualTo(paperid);
		List<Papermapping> pmlist=papermappingMapper.selectByExample(papermappingExample);
		List<Paper> plist=new ArrayList<Paper>();
		for(Papermapping pm:pmlist) {
			System.out.println(pmlist.size());
			Paper p=paperMapper.selectByPrimaryKey(pm.getUserpaperid());
			System.out.println(p.getPaperid()+"  "+p.getDoflag());
			if(p.getDoflag()==0) {
				plist.add(p);
			}
		}
		return plist;
	}
	@Override
	public List<String> getKnowledge(Paper paper) {
		List<String> knowledge=new ArrayList<>();
		HashMap<String, Integer> hashmap=new HashMap<>();
		List<Paper_fill> fills=paper_fillMapper.getAllbyPaperid(paper.getPaperid());
		for(Paper_fill fill:fills) {
			hashmap.put(fill.getqKnowledge(), 1);
		}
		List<Paper_single> singles=paper_singleMapper.getAllbyPaperid(paper.getPaperid());
		for(Paper_single single:singles) {
			hashmap.put(single.getqKnowledge(), 1);
		}
		List<Paper_multiple> multiples=paper_multipleMapper.getAllbyPaperid(paper.getPaperid());
		for(Paper_multiple multiple:multiples) {
			hashmap.put(multiple.getqKnowledge(), 1);
		}
		List<Paper_shortanswer> shortanswers=paper_shortanswerMapper.getAllbyid(paper.getPaperid());
		for(Paper_shortanswer shortanswer:shortanswers) {
			hashmap.put(shortanswer.getqKnowledge(), 1);
		}
		List<Paper_judge> judges=paper_judgeMapper.getAllbyPaperid(paper.getPaperid());
		for(Paper_judge judge:judges) {
			hashmap.put(judge.getqKnowledge(), 1);
		}
		for(String key:hashmap.keySet()) {
			knowledge.add(key);
		}
		return knowledge;
	}
	@Override
	public List<User> selectAllUserDone(int paperid) {
		// TODO Auto-generated method stub
		List<Paper> paperlist=selectAllPaperDone(paperid);
		List<User> userlist=new ArrayList<User>();
		for(Paper p:paperlist) {
			User u=new User();
			u.setUserid(p.getExaminee());
			userlist.add(userService.selectUserByPrimaryKey(u));
		}
		return userlist;
	}
	@Override
	public List<Paper> selectAllPaperWaitingToFinishTeacher(User user) {
		// TODO Auto-generated method stub
		List<Paper> selectAllPaper = selectAllPaperByTeacher(user);
		List<Paper> paperWaitingToFinish=new ArrayList<Paper>();
		int index=0;
		for(Paper paperItem:selectAllPaper) {
			Date beginTime =paperItem.getBegintime();
			Date endTime=paperItem.getEndtime();
			Date now=new Date();
			if(paperItem.getWinder()!=null&&paperItem.getDoflag()!=null&&paperItem.getDoflag()==0&&now.compareTo(beginTime)>0&&endTime.compareTo(now)>0) {
				paperWaitingToFinish.add(selectAllPaper.get(index));
			}
		   index++;
		}
		return paperWaitingToFinish;
	}
	@Override
	public List<Paper> selectAllPaperFinishedTeacher(User user) {
		List<Paper> selectAllPaper = selectAllPaperByTeacher(user);
		List<Paper> paperFinished=new ArrayList<Paper>();
		int index=0;
		for(Paper paperItem:selectAllPaper) {
			
			if(paperItem.getWinder()!=null&&paperItem.getDoflag()==2) {
				paperFinished.add(selectAllPaper.get(index));
			}
		   index++;
		}
		return  paperFinished;
	}
	@Override
	public List<Paper> selectAllPaperWaitingToStartTeacher(User user) {
		List<Paper> selectAllPaper = selectAllPaperByTeacher(user);
		List<Paper> paperWaitingToStart=new ArrayList<Paper>();
		int index=0;
		for(Paper paperItem:selectAllPaper) {
			Date beginTime =paperItem.getBegintime();
			Date now=new Date();
			if(paperItem.getWinder()!=null&&(now.compareTo(beginTime)<0)) {
				paperWaitingToStart.add(selectAllPaper.get(index));
			}
		   index++;
		}
		return  paperWaitingToStart;
	}
	@Override
	public List<Paper> selectAllPaperOutOfDateTeacher(User user) {
		List<Paper> selectAllPaper = selectAllPaperByTeacher(user);
		List<Paper> paperOutOfDate=new ArrayList<Paper>();
		int index=0;
		for(Paper paperItem:selectAllPaper) {
			
			Date endTime=paperItem.getEndtime();
			Date now=new Date();
			if(paperItem.getWinder()!=null&&now.compareTo(endTime)>0&&paperItem.getDoflag()!=2) {
				paperOutOfDate.add(selectAllPaper.get(index));
			}
		   index++;
		}
		return  paperOutOfDate;
	}
	@Override
	public List<Paper> selectAllPaperInitialTeacher(User user) {
		PaperExample paperExample=new PaperExample();
		Criteria c1=paperExample.createCriteria();
		c1.andExamineeIsNull();
		c1.andWinderEqualTo(user.getUserid());
//		Criteria c2=paperExample.createCriteria();
//		c2.andWinderEqualTo(user.getUserid());
//		paperExample.or(c2);
		List<Paper> paperlist=paperMapper.selectByExample(paperExample);
		return paperlist;
	}
	@Override
	public int Buypaper(Paper paper,int userid) {
		// TODO Auto-generated method stub
		Paper newpaper=new Paper();
		newpaper.setPapername(paper.getPapername());
		newpaper.setPaperlabel(paper.getPaperlabel());
		newpaper.setWinder(paper.getWinder());
		newpaper.setExaminee(userid);
		newpaper.setDuration(paper.getDuration());
		newpaper.setQuestionnum(0);
		newpaper.setSinglenum(0);
		newpaper.setMultiplenum(0);
		newpaper.setFillnum(0);
		newpaper.setJudgenum(0);
		newpaper.setShortanswernum(0);
		newpaper.setDoflag(0);
		newpaper.setOutnum(0);
		addPaper(newpaper);
		if(paper.getQuestionnum()!=0)
		{
			if(paper.getSinglenum()!=0)
			{
				List<Paper_single> singles = paper_singleService.getAllbyPaperid(paper);
				for(int i=0;i<singles.size();i++) {
					Paper_single single=singles.get(i);
					single.setPaperid(newpaper.getPaperid());
					single.setqId(null);
					paper_singleService.addPaper_single(single);
					for(int j=0;j<single.getSinglelist().size();j++) 
					{
						Paper_singlechoice choice=single.getSinglelist().get(j);
						choice.setqId(single.getqId());
						choice.setChoiceid(null);
						paper_singlechoiceService.addPaper_singlechoice(choice);
					}				
				}
			}
			
			
			if(paper.getJudgenum()!=0)
			{
				List<Paper_judge> judges = paper_judgeService.getAllbyPaperid(paper);
				for(int i=0;i<judges.size();i++) {
					Paper_judge judge=judges.get(i);
					judge.setPaperid(newpaper.getPaperid());
					judge.setqId(null);
					paper_judgeService.addPaper_judge(judge);			
				}
			}
			
			
			if(paper.getFillnum()!=0)
			{
				List<Paper_fill> fills = paper_fillService.getAllbyPaperid(paper);
				for(int i=0;i<fills.size();i++) {
					Paper_fill fill=fills.get(i);
					fill.setPaperid(newpaper.getPaperid());
					fill.setqId(null);
					paper_fillService.addPaper_fill(fill);
					for(int j=0;j<fill.getFilllist().size();j++) 
					{
						Paper_fillchoice choice=fill.getFilllist().get(j);
						choice.setqId(fill.getqId());
						choice.setChoiceid(null);
						paper_fillchoiceService.addPaper_fillchoice(choice);
					}				
				}
			}
			
			
			if(paper.getMultiplenum()!=0)
			{
				List<Paper_multiple> multiples = paper_multipleService.getAllbyPaperid(paper);
				for(int i=0;i<multiples.size();i++) {
					Paper_multiple multiple=multiples.get(i);
					multiple.setPaperid(newpaper.getPaperid());
					multiple.setqId(null);
					paper_multipleService.addPaper_multiple(multiple);
					for(int j=0;j<multiple.getMultiplelist().size();j++) 
					{
						Paper_multiplechoice choice=multiple.getMultiplelist().get(j);
						choice.setqId(multiple.getqId());
						choice.setChoiceid(null);
						paper_multiplechoiceService.addPaper_multiplechoice(choice);
					}				
				}
			}
			

			if(paper.getShortanswernum()!=0)
			{
				List<Paper_shortanswer> shortanswers = paper_shortanswerService.getAllbyPaperid(paper);
				for(int i=0;i<shortanswers.size();i++) {
					Paper_shortanswer shortanswer=shortanswers.get(i);
					shortanswer.setPaperid(newpaper.getPaperid());
					shortanswer.setqId(null);
					paper_shortanswerService.addPaper_shortanswer(shortanswer);
					for(int j=0;j<shortanswer.getShortmovlist().size();j++) 
					{
						Paper_shortanswer_mov mov = shortanswer.getShortmovlist().get(j);
						mov.setqId(shortanswer.getqId());
						paper_shortanswer_movService.addPaper_shortanswer_mov(mov);
					}		
					for(int j=0;j<shortanswer.getShortpiclist().size();j++) 
					{
						Paper_shortanswer_pic pic=shortanswer.getShortpiclist().get(j);
						pic.setqId(shortanswer.getqId());
						paper_shortanswer_picService.addPaper_shortanswer_pic(pic);
					}		
				}
			}
			
		}
		
		
		return newpaper.getPaperid();
	}

	@Override
	public List<User> selectAllUserUndo(int paperid) {
		// TODO Auto-generated method stub
		List<Paper> paperlist=selectAllPaperUndo(paperid);
		List<User> userlist=new ArrayList<User>();
		for(Paper p:paperlist) {
			User u=new User();
			u.setUserid(p.getExaminee());
			userlist.add(userService.selectUserByPrimaryKey(u));
		}
		return userlist;
	}
	@Override
	public int updateByPaperId(Paper paper) {
		// TODO Auto-generated method stub
		int updateByPrimaryKeySelective = paperMapper.updateByPrimaryKeySelective(paper);
		
		return updateByPrimaryKeySelective;
	}
	@Override
	public List<Paper> selectPaperByInvitationcode(String s,Integer userid) {
		// TODO Auto-generated method stub
		paperExample=new PaperExample();
		Criteria c=paperExample.createCriteria();
		c.andInvitationcodeEqualTo(s).andExamineeEqualTo(userid);
		
		List<Paper> selectByExample = paperMapper.selectByExample(paperExample);
		
		return selectByExample;
	}
	@Override
	public List<Integer> AllStudentScore(int paperid) {
		// TODO Auto-generated method stub
		//通过老师试卷id找到所有学生试卷再获得学生的分数
		List<Integer> scores=new ArrayList<Integer>();
		papermappingExample=new PapermappingExample();
		com.neuedu.entity.PapermappingExample.Criteria c=papermappingExample.createCriteria();
		c.andPaperidEqualTo(paperid);
		List<Papermapping> selectByExample = papermappingMapper.selectByExample(papermappingExample);
		for(Papermapping item:selectByExample) {
			Integer userpaperid = item.getUserpaperid();
			Paper selectByPrimaryKey = paperMapper.selectByPrimaryKey(userpaperid);
			if(selectByPrimaryKey.getFinscore()!=null) {
				scores.add(selectByPrimaryKey.getFinscore());
			}
		}
		return scores;
	}
	@Override
	public List<Paper> selectPaperPublic(User user) {
		List<Paper> papers=selectAllPaperByTeacher(user);
		List<Paper> result=new ArrayList<>();
		for(Paper paper :papers) {
			if(paper.getInvitationcode()!=null) {
				result.add(paper);
			}
		}
		return result;
	}
	@Override
	public List<Paper> selectPaperunPublic(User user) {
		List<Paper> papers=selectAllPaperByTeacher(user);
		List<Paper> result=new ArrayList<>();
		for(Paper paper :papers) {
			if(paper.getInvitationcode()==null&&paper.getExaminee()==null) {
				result.add(paper);
			}
		}
		return result;
	}
	@Override
	public int deletePaper(Paper p) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteByPrimaryKey(p.getPaperid());
		
		return i;
	}
	@Override
	public int getFillnumByknowledge(Paper_fill pf) {
		// TODO Auto-generated method stub
		Paper p = new Paper();
		p.setPaperid(pf.getPaperid());
		List<Paper_fill> allbyPaperid = paper_fillService.getAllbyPaperid(p);
		if(allbyPaperid==null) {
			return 0;
		}
		for(int i=0;i<allbyPaperid.size();i++) {
			if(!allbyPaperid.get(i).getqKnowledge().equals(pf.getqKnowledge())) {
				allbyPaperid.remove(i);
			}	
		}
		if(allbyPaperid==null) {
			return 0;
		}
		int i = allbyPaperid.size();
		
		return i;
	}
	@Override
	public int getSinglenumByknowledge(Paper_single ps) {
		// TODO Auto-generated method stub
		Paper p = new Paper();
		p.setPaperid(ps.getPaperid());
		List<Paper_single> allbyPaperid = paper_singleService.getAllbyPaperid(p);
		if(allbyPaperid==null) {
			return 0;
		}
		for(int i=0;i<allbyPaperid.size();i++) {
			if(!allbyPaperid.get(i).getqKnowledge().equals(ps.getqKnowledge())) {
				allbyPaperid.remove(i);
			}	
		}
		if(allbyPaperid==null) {
			return 0;
		}
		int i = allbyPaperid.size();
		
		return i;
	}
	@Override
	public int getMultiplenumByknowledge(Paper_multiple pm) {
		// TODO Auto-generated method stub
		Paper p = new Paper();
		p.setPaperid(pm.getPaperid());
		List<Paper_multiple> allbyPaperid = paper_multipleService.getAllbyPaperid(p);
		if(allbyPaperid==null) {
			return 0;
		}
		for(int i=0;i<allbyPaperid.size();i++) {
			if(!allbyPaperid.get(i).getqKnowledge().equals(pm.getqKnowledge())) {
				allbyPaperid.remove(i);
			}	
		}
		if(allbyPaperid==null) {
			return 0;
		}
		int i = allbyPaperid.size();
		
		return i;
	}
	@Override
	public int getJudgenumByknowledge(Paper_judge pj) {
		// TODO Auto-generated method stub
		Paper p = new Paper();
		p.setPaperid(pj.getPaperid());
		List<Paper_judge> allbyPaperid = paper_judgeService.getAllbyPaperid(p);
		if(allbyPaperid==null) {
			return 0;
		}
		for(int i=0;i<allbyPaperid.size();i++) {
			if(!allbyPaperid.get(i).getqKnowledge().equals(pj.getqKnowledge())) {
				allbyPaperid.remove(i);
			}	
		}
		if(allbyPaperid==null) {
			return 0;
		}
		int i = allbyPaperid.size();
		
		return i;
	}
	@Override
	public int getShortanswernumByknowledge(Paper_shortanswer ps) {
		// TODO Auto-generated method stub
		Paper p = new Paper();
		p.setPaperid(ps.getPaperid());
		List<Paper_shortanswer> allbyPaperid = paper_shortanswerService.getAllbyPaperid(p);
		if(allbyPaperid==null) {
			return 0;
		}
		for(int i=0;i<allbyPaperid.size();i++) {
			if(!allbyPaperid.get(i).getqKnowledge().equals(ps.getqKnowledge())) {
				allbyPaperid.remove(i);
			}	
		}
		if(allbyPaperid==null) {
			return 0;
		}
		int i = allbyPaperid.size();
		
		return i;
	}
	
	
	@Override
	public int publishPaper(Paper p) {
		// TODO Auto-generated method stub
		int oldpaperid = p.getPaperid();
		Paper old = new Paper();
		old.setPaperid(oldpaperid);
		Paper newp = selectPaperbyId(oldpaperid);
		
		int examinee = p.getExaminee();
		newp.setExaminee(examinee);
		newp.setPaperid(0);
		addPaper(newp);
		int newpaperid = newp.getPaperid();
		
//		Papermapping pm = new Papermapping();
//		pm.setUserpaperid(newpaperid);
//		pm.setPaperid(oldpaperid);
//		pm.setExaminee(examinee);
//		pm.setType(0);
//		papermappingMapper.insert(pm);
		List<Paper_fill> filllist = paper_fillService.getAllbyPaperid(old);
		for(Paper_fill pf :filllist) {
			pf.setqId(0);
			pf.setExaminee(examinee);
			pf.setPaperid(newpaperid);
			paper_fillService.addPaper_fill(pf);
			int qid = pf.getqId();
			for(Paper_fillchoice pfc:pf.getFilllist()) {
				pfc.setChoiceid(0);
				pfc.setqId(qid);
				paper_fillchoiceService.addPaper_fillchoice(pfc);
				
			}
		}
		
		List<Paper_single> singlelist = paper_singleService.getAllbyPaperid(old);
		for(Paper_single ps :singlelist) {
			ps.setqId(0);
			ps.setExaminee(examinee);
			ps.setPaperid(newpaperid);
			paper_singleService.addPaper_single(ps);
			int qid = ps.getqId();
			for(Paper_singlechoice psc:ps.getSinglelist()) {
				psc.setChoiceid(0);
				psc.setqId(qid);
				paper_singlechoiceService.addPaper_singlechoice(psc);
				
			}
		}
		
		List<Paper_multiple> multiplelist = paper_multipleService.getAllbyPaperid(old);
		
		for(Paper_multiple ps :multiplelist) {
			ps.setqId(0);
			ps.setExaminee(examinee);
			ps.setPaperid(newpaperid);
			paper_multipleService.addPaper_multiple(ps);
			int qid = ps.getqId();
			for(Paper_multiplechoice psc:ps.getMultiplelist()) {
				psc.setChoiceid(0);
				psc.setqId(qid);
				paper_multiplechoiceService.addPaper_multiplechoice(psc);
				
			}
		}
		
		
		List<Paper_judge> judgelist = paper_judgeService.getAllbyPaperid(old);

		for(Paper_judge ps :judgelist) {
			ps.setqId(0);
			ps.setExaminee(examinee);
			ps.setPaperid(newpaperid);
			paper_judgeService.addPaper_judge(ps);
			
		}
		
		List<Paper_shortanswer> shortanswerlist = paper_shortanswerService.getAllbyPaperid(old);
		for(Paper_shortanswer ps :shortanswerlist) {
			ps.setqId(0);
			ps.setExaminee(examinee);
			ps.setPaperid(newpaperid);
			paper_shortanswerService.addPaper_shortanswer(ps);
			int qid = ps.getqId();
			for(Paper_shortanswer_pic psp:ps.getShortpiclist()) {
				
				psp.setqId(qid);
				paper_shortanswer_picService.addPaper_shortanswer_pic(psp);
				
			}
			for(Paper_shortanswer_mov psv:ps.getShortmovlist()) {
				
				psv.setqId(qid);
				paper_shortanswer_movService.addPaper_shortanswer_mov(psv);
				
			}
		}
		
		
		return newpaperid;
	}
	@Override
	public int deleteFillByknowledge(Paper_fill pf) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteFillByknowledge(pf);
		
		return i;
	}
	@Override
	public int deleteSingleByknowledge(Paper_single ps) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteSingleByknowledge(ps);
		return i;
	}
	@Override
	public int deleteMultipleByknowledge(Paper_multiple pm) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteMultipleByknowledge(pm);
		return i;
	}
	@Override
	public int deleteJudgeByknowledge(Paper_judge pj) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteJudgeByknowledge(pj);
		return i;
	}
	@Override
	public int deleteShortanswerByknowledge(Paper_shortanswer ps) {
		// TODO Auto-generated method stub
		int i = paperMapper.deleteShortanswerByknowledge(ps);
		return i;
	}
	@Override
	public List<Paper> selectAllPaperWaitingToTest(User user) {
		// TODO Auto-generated method stub
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperWaitingToTest=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
			
			if(paperItem!=null) {
			if(paperItem.getWinder()==null&&paperItem.getDoflag()!=null&&paperItem.getDoflag()==0) {
				 paperWaitingToTest.add(selectAllPaper.get(index));
			}
			}
		   index++;
		}
		return paperWaitingToTest;
		}
		return null;
	}
	@Override
	public List<Paper> selectAllPaperTestFinished(User user) {
		// TODO Auto-generated method stub
		List<Paper> selectAllPaper = selectAllPaper(user);
		List<Paper> paperTestFinished=new ArrayList<Paper>();
		int index=0;
		if(selectAllPaper!=null) {
		for(Paper paperItem:selectAllPaper) {
			if(paperItem!=null) {
			if(paperItem.getWinder()==null&&paperItem.getDoflag()!=null&&paperItem.getDoflag()==2) {
				 paperTestFinished.add(selectAllPaper.get(index));
			}
			}
		   index++;
		}
		return paperTestFinished;
		}
		return null;

	}
	@Override
	public List<Paper> selectByExample(PaperExample paperexample) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Paper selectByUser(int paperid, int userid) {
		// TODO Auto-generated method stub
		PapermappingExample papermappingExample =new PapermappingExample();
		com.neuedu.entity.PapermappingExample.Criteria c1=papermappingExample.createCriteria();
		c1.andPaperidEqualTo(paperid);
		c1.andExamineeEqualTo(userid);
		List<Papermapping> pmlist=papermappingMapper.selectByExample(papermappingExample);
		Paper paper=paperMapper.selectByPrimaryKey(pmlist.get(0).getUserpaperid());
		return paper;
	}


	
	

}
