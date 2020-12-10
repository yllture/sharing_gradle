package com.neuedu.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Paper;
import com.neuedu.entity.Paper_fill;
import com.neuedu.entity.Paper_fillchoice;
import com.neuedu.entity.Paper_judge;
import com.neuedu.entity.Paper_multiple;
import com.neuedu.entity.Paper_multiplechoice;
import com.neuedu.entity.Paper_shortanswer;
import com.neuedu.entity.Paper_single;
import com.neuedu.entity.Paper_singlechoice;
import com.neuedu.entity.Shop_paper;
import com.neuedu.entity.User;
import com.neuedu.service.BankService;
import com.neuedu.service.PaperService;
import com.neuedu.service.Paper_fillService;
import com.neuedu.service.Paper_fillchoiceService;
import com.neuedu.service.Paper_judgeService;
import com.neuedu.service.Paper_multipleService;
import com.neuedu.service.Paper_multiplechoiceService;
import com.neuedu.service.Paper_shortanswerService;
import com.neuedu.service.Paper_singleService;
import com.neuedu.service.Paper_singlechoiceService;
import com.neuedu.service.Shop_paperService;

@Controller
public class EditorPaperController {
	@Autowired
	PaperService paperService;
	@Autowired
	Paper_fillService paper_fillService;
	@Autowired
	Paper_judgeService paper_judge_Service;
	@Autowired
	Paper_multipleService paper_multipleService;
	@Autowired
	Paper_shortanswerService paper_shortanswer_Service;
	@Autowired
	Paper_singleService papersingle_Service;
	@Autowired
	Paper_singlechoiceService paper_singlechoiceService;
	@Autowired
	Paper_multiplechoiceService paper_multiplechoiceService;
	@Autowired
	Paper_fillchoiceService paper_fillchoiceService;
	@Autowired 
	Shop_paperService shop_paperService;
	/**
	 * 用户点击新建试卷生成试卷信息跳转编辑页
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/newPaper")
	public String newPaper(Model m,HttpServletRequest req, HttpServletResponse res,String papername) throws IOException {
		User user = (User) req.getSession().getAttribute("currentUser");
		Integer winder=user.getUserid();
		Paper newPaper = new Paper();
		newPaper.setWinder(winder);
		newPaper.setPapername(papername);
		paperService.addPaper(newPaper);
		m.addAttribute("paper", newPaper);
		return "teacherPaper/paperEditor";
	}
	//用户在编辑页面点击保存试卷
	@RequestMapping("/tosavepaper")
	public String tosavepaper(HttpServletRequest req, HttpServletResponse res,@RequestParam String papername,@RequestParam String paperintroduce,@RequestParam Integer order,@RequestParam Integer time,Model m) {
//		System.out.println("-----");
		Paper paper=(Paper)req.getSession().getAttribute("currentPaper1");
		System.out.println("id"+paper.getPaperid());
		Paper p=paperService.selectPaperbyId(paper.getPaperid());
		p.setOrderflag(order);
		p.setC1(paperintroduce);
		p.setPapername(papername);
		p.setDuration(time.toString());
		paperService.updateByPaperId(p);
		User currentUser = (User) req.getSession().getAttribute("currentUser");
		int userid= currentUser.getUserid();
		User u = new User();
		u.setUserid(userid);
		 
		List<Paper> list = paperService.selectPaperunPublic(u);
		List<Paper> uploadlist = new ArrayList<Paper>();
		List<Paper> unuploadlist = new ArrayList<Paper>();
		if(list!=null) {
			
			for(Paper pap :list) {
				Shop_paper sp = new Shop_paper();
				sp.setPaperid(pap.getPaperid());
				sp = shop_paperService.selectbypaperid(sp);
				if(sp==null) {
					unuploadlist.add(pap);
				}else {
					uploadlist.add(pap);
				}
			}	
		}
		m.addAttribute("uploadlist", uploadlist);
		m.addAttribute("unuploadlist", unuploadlist);
		req.getSession().removeAttribute("currentPaper1");
		
		return "redirect:/tounpublishteacherPaper";
	}
	
	//------------------------------------------------------编辑起始试卷内容时的页面------------------------------------------------------
	/**
	 * 用户点击编辑试卷跳转编辑页
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/editorPaper")
	public String editorPaper(Model m,HttpServletRequest req, HttpServletResponse res,@RequestParam Integer paperid) throws IOException {
		Paper paper = new Paper();
		paper.setPaperid(paperid);
		req.getSession().setAttribute("currentPaper1", paper);
//		测试数据
//		paper.setPaperid(1);
		Paper editorPaper = paperService.selectPaperbyId(paperid);
		//单选题
		List<Paper_single> singles= papersingle_Service.getAllbyPaperid(editorPaper);
		//多选题
		List<Paper_multiple> multiples= paper_multipleService.getAllbyPaperid(editorPaper);
		//判断题
		List<Paper_judge> judges= paper_judge_Service.getAllbyPaperid(editorPaper);
		//填空题
		List<Paper_fill> fills= paper_fillService.getAllbyPaperid(editorPaper);
		//简答题
		List<Paper_shortanswer> shortanswers= paper_shortanswer_Service.getAllbyPaperid(editorPaper);
		//知识点
		List<String> kownledgs = paperService.getKnowledge(paper);
		List<Map<String, Object>> kownledgsinlefor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinmufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinjufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinfifor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinshfor= new LinkedList<Map<String,Object>>();
		//--------------------------------------------------获取单选题题目信息部分信息-----------------------------------------------------
		List<Paper_single> paper_singles = papersingle_Service.getAllbyPaperid(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_singles.size();j++) {
				if(paper_singles.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_singles.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinlefor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		//--------------------------------------------------获取多选题题目信息部分信息-----------------------------------------------------
		List<Paper_multiple> paper_multiples = paper_multipleService.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_multiples.size();j++) {
				if(paper_multiples.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_multiples.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinmufor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		//--------------------------------------------------获取判断题题目信息部分信息-----------------------------------------------------
		List<Paper_judge> paper_judges = paper_judge_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_judges.size();j++) {
				if(paper_judges.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_judges.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinjufor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		//--------------------------------------------------获取填空题题目信息部分信息-----------------------------------------------------
		List<Paper_fill> paper_fills = paper_fillService.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_fills.size();j++) {
				if(paper_fills.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_fills.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfifor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		//--------------------------------------------------获取简答题题目信息部分信息-----------------------------------------------------
		List<Paper_shortanswer> paper_shortanswers = paper_shortanswer_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_shortanswers.size();j++) {
				if(paper_shortanswers.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_shortanswers.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinshfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		m.addAttribute("kownledgsinlefor",kownledgsinlefor);
		m.addAttribute("kownledgsinmufor",kownledgsinmufor);
		m.addAttribute("kownledgsinfifor",kownledgsinfifor);
		m.addAttribute("kownledgsinjufor",kownledgsinjufor);
		m.addAttribute("kownledgsinshfor",kownledgsinshfor);
		m.addAttribute("Paper", editorPaper);
		
//		System.out.println("duosho"+multiples.get(0).getMultiplelists().size());
		m.addAttribute("singles",singles);
		m.addAttribute("multiples",multiples);
		m.addAttribute("judges",judges);
		m.addAttribute("fills",fills);
		m.addAttribute("shortanswers",shortanswers);
		return "teacherPaper/editorPaper";
	}
	
		/**
	 * 用户点击编辑试卷跳转编辑页
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/editorPaper1")
	public String editorPaper1(Model m,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper paper=(Paper)req.getSession().getAttribute("currentPaper1");
		
//		测试数据
//		paper.setPaperid(1);
		Paper editorPaper = paperService.selectPaperbyId(paper.getPaperid());
		//单选题
		List<Paper_single> singles= papersingle_Service.getAllbyPaperid(editorPaper);
		//多选题
		List<Paper_multiple> multiples= paper_multipleService.getAllbyPaperid(editorPaper);
		//判断题
		List<Paper_judge> judges= paper_judge_Service.getAllbyPaperid(editorPaper);
		//填空题
		List<Paper_fill> fills= paper_fillService.getAllbyPaperid(editorPaper);
		//简答题
		List<Paper_shortanswer> shortanswers= paper_shortanswer_Service.getAllbyPaperid(editorPaper);
		//知识点
		List<String> kownledgs = paperService.getKnowledge(paper);
		List<Map<String, Object>> kownledgsinlefor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinmufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinjufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinfifor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinshfor= new LinkedList<Map<String,Object>>();
		//--------------------------------------------------获取单选题题目信息部分信息-----------------------------------------------------
		List<Paper_single> paper_singles = papersingle_Service.getAllbyPaperid(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_singles.size();j++) {
				if(paper_singles.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_singles.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinlefor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		//--------------------------------------------------获取多选题题目信息部分信息-----------------------------------------------------
		List<Paper_multiple> paper_multiples = paper_multipleService.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_multiples.size();j++) {
				if(paper_multiples.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_multiples.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinmufor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		//--------------------------------------------------获取判断题题目信息部分信息-----------------------------------------------------
		List<Paper_judge> paper_judges = paper_judge_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_judges.size();j++) {
				if(paper_judges.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_judges.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinjufor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		//--------------------------------------------------获取填空题题目信息部分信息-----------------------------------------------------
		List<Paper_fill> paper_fills = paper_fillService.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_fills.size();j++) {
				if(paper_fills.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_fills.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfifor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		//--------------------------------------------------获取简答题题目信息部分信息-----------------------------------------------------
		List<Paper_shortanswer> paper_shortanswers = paper_shortanswer_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_shortanswers.size();j++) {
				if(paper_shortanswers.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_shortanswers.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinshfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		m.addAttribute("kownledgsinlefor",kownledgsinlefor);
		m.addAttribute("kownledgsinmufor",kownledgsinmufor);
		m.addAttribute("kownledgsinfifor",kownledgsinfifor);
		m.addAttribute("kownledgsinjufor",kownledgsinjufor);
		m.addAttribute("kownledgsinshfor",kownledgsinshfor);
		m.addAttribute("Paper", editorPaper);
		
//		System.out.println("duosho"+multiples.get(0).getMultiplelists().size());
		m.addAttribute("singles",singles);
		m.addAttribute("multiples",multiples);
		m.addAttribute("judges",judges);
		m.addAttribute("fills",fills);
		m.addAttribute("shortanswers",shortanswers);
		return "teacherPaper/editorPaper";
	}
	//----------------------------------------------------用户点击保存设置----------------------------------------------------
	
	
	//----------------------------------------------------点击编辑页取出对应的知识点信息----------------------------------------------------
	@RequestMapping("/getkonwledges")
	@ResponseBody
	public Map getkonwledges(Integer paperid,HttpServletRequest req) {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> knowledges = paperService.getKnowledge(paper);
	
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("knowledges", knowledges);
		map.put("code", "0000");
		return map;
	}
	
	//------------------------------------------------------编辑进行时传递新增编辑数据------------------------------------------------------
	/**
	 * 传输试卷新增单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transingle")
	@ResponseBody
	public void transingle(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		Integer paperid = p.getPaperid();
		//取出单选题数据
//		System.out.print("obj"+JSON.toJSONString(obj));
//		Integer paperid = obj.getInteger("paperid");
		String singeltitle = obj.getString("single_title");
		Integer singelscore = obj.getInteger("singelscore");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项内容
		JSONArray singlechoicecontent =  obj.getJSONArray("singleChoicesContent");
		//得到选项对错
		JSONArray singlechoicejudge =  obj.getJSONArray("singlechoicejudge");
		
		
		//单选题存入数据库
		Paper_single paper_single = new Paper_single();
		paper_single.setqTitle(singeltitle);
		paper_single.setqScore(singelscore);
		paper_single.setqKnowledge(knowledge);
		paper_single.setqAnalysis(analysis);
		paper_single.setqDifficulty(difficulty);
		paper_single.setPaperid(paperid);
		paper_single.setqId(qid);
		List<Paper_singlechoice> paper_singlechoices = new ArrayList<Paper_singlechoice>();
		
		for(int i=0;i<singlechoicecontent.size();i++) {
			Paper_singlechoice paper_singlechoice = new Paper_singlechoice();
			paper_singlechoice.setContent((String)singlechoicecontent.get(i));
			paper_singlechoice.setType((Integer)singlechoicejudge.get(i));
			paper_singlechoice.setSequence(i+1);
			paper_singlechoices.add(paper_singlechoice);
		}
		
		//更新
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			Paper_single paper_single2 = new Paper_single();
			paper_single2.setqId(qid);
			List<Paper_singlechoice> paper_singlechoicesold = papersingle_Service.selectPaper_single(paper_single2).getSinglelist();
			for(int i=0;i<paper_singlechoicesold.size();i++) {
				paper_singlechoiceService.deletePaper_singlechoice(paper_singlechoicesold.get(i));
			}
			papersingle_Service.updatePaper_single(paper_single);
		}
		else {
			papersingle_Service.addPaper_single(paper_single);
		}
		
		Integer paper_single_id = paper_single.getqId();
		//选项存入数据库
		for(int i=0;i<paper_singlechoices.size();i++) {
			paper_singlechoices.get(i).setqId(paper_single_id);;
			paper_singlechoiceService.addPaper_singlechoice(paper_singlechoices.get(i));
		}
		//
		paper_single = papersingle_Service.selectPaper_single(paper_single);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("paper_single", paper_single);
		
		Object json = JSON.toJSONString(map);

		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}
	
	/**
	 * 传输试卷新增多选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmultiple")
	@ResponseBody
	public void tranmultiple(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
		Integer paperid  = p.getPaperid();
//		Integer paperid = obj.getInteger("paperid");
		String multipletitle = obj.getString("multiple_title");
		Integer multiplescore = obj.getInteger("multiplescore");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项内容
		JSONArray multiplechoicecontent =  obj.getJSONArray("multipleChoicesContent");
		//得到选项对错
		JSONArray multiplechoicejudge =  obj.getJSONArray("multiplechoicejudge");
		
		
		
		Paper_multiple paper_multiple = new Paper_multiple();
		paper_multiple.setqTitle(multipletitle);
		paper_multiple.setqScore(multiplescore);
		paper_multiple.setqKnowledge(knowledge);
		paper_multiple.setqAnalysis(analysis);
		paper_multiple.setqDifficulty(difficulty);
		paper_multiple.setPaperid(paperid);
		paper_multiple.setqId(qid);
		List<Paper_multiplechoice> paper_multiplechoices = new ArrayList<Paper_multiplechoice>();
		for(int i=0;i<multiplechoicecontent.size();i++) {
			Paper_multiplechoice paper_multiplechoice = new Paper_multiplechoice();
			paper_multiplechoice.setContent((String)multiplechoicecontent.get(i));
			paper_multiplechoice.setType((Integer)multiplechoicejudge.get(i));
			paper_multiplechoice.setSequence(i+1);
			paper_multiplechoices.add(paper_multiplechoice);
		}
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			Paper_multiple paper_multiple2 = new Paper_multiple();
			paper_multiple2.setqId(qid);
			List<Paper_multiplechoice> paper_multiplechoicesold = paper_multipleService.selectPaper_multiple(paper_multiple2).getMultiplelist();
			for(int i=0;i<paper_multiplechoicesold.size();i++) {
				paper_multiplechoiceService.deletePaper_multiplechoice(paper_multiplechoicesold.get(i));
			}
			//多选题存入数据库
			paper_multipleService.updatePaper_multiple(paper_multiple);
		}else {
			paper_multipleService.addPaper_multiple(paper_multiple);
		}
		
		
		Integer paper_multiple_id = paper_multiple.getqId();
		//选项存入数据库
		for(int i=0;i<paper_multiplechoices.size();i++) {
			paper_multiplechoices.get(i).setqId(paper_multiple_id);;
			paper_multiplechoiceService.addPaper_multiplechoice(paper_multiplechoices.get(i));
		}
		//
		paper_multiple=paper_multipleService.selectPaper_multiple(paper_multiple);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("paper_multiple", paper_multiple);
		
		Object json = JSON.toJSONString(map);

		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}
	

	/**
	 * 传输试卷新增判断题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranjudge")
	@ResponseBody
	public void tranjudge(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		Integer paperid = p.getPaperid();
//		Integer paperid = obj.getInteger("paperid");
		String judgetitle = obj.getString("judge_title");
		Integer judgescore = obj.getInteger("judgescore");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项对错
		Integer judge =  obj.getInteger("isture");
		
		
		
		Paper_judge paper_judge = new Paper_judge();
		paper_judge.setqTitle(judgetitle);
		paper_judge.setqScore(judgescore);
		paper_judge.setqKnowledge(knowledge);
		paper_judge.setqAnalysis(analysis);
		paper_judge.setqDifficulty(difficulty);
		paper_judge.setPaperid(paperid);
		paper_judge.setIsture(judge);
		paper_judge.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			paper_judge_Service.updatePaper_judge(paper_judge);
		}else {
			
			paper_judge_Service.addPaper_judge(paper_judge);
		}
		
		//
		paper_judge=paper_judge_Service.selectPaper_judge(paper_judge);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("paper_judge", paper_judge);
		
		Object json = JSON.toJSONString(map);

		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}
	
	
	/**
	 * 传输试卷新增填空题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranfill")
	@ResponseBody
	public void tranfill(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出填空题数据
		Integer paperid = p.getPaperid();
//		Integer paperid = obj.getInteger("paperid");
		String filltitle = obj.getString("fill_title");
		Integer fillscore = obj.getInteger("fillscore");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到填空题答案
		JSONArray fillcontent =  obj.getJSONArray("fillcontent");
		
		List<Paper_fillchoice> paper_fillchoices = new ArrayList<Paper_fillchoice>();
		for(int i=0;i<fillcontent.size();i++) {
			Paper_fillchoice paper_fillchoice = new Paper_fillchoice();
			paper_fillchoice.setContent((String)fillcontent.get(i));
			paper_fillchoice.setSequence(i+1);
			paper_fillchoices.add(paper_fillchoice);
		}
		
		
		Paper_fill paper_fill = new Paper_fill();
		paper_fill.setqTitle(filltitle);
		paper_fill.setqScore(fillscore);
		paper_fill.setqKnowledge(knowledge);
		paper_fill.setqAnalysis(analysis);
		paper_fill.setqDifficulty(difficulty);
		paper_fill.setPaperid(paperid);
		paper_fill.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			paper_fillService.deletePaper_fill(paper_fill);
			Paper_fill paper_fill2 = new Paper_fill();
			paper_fill2.setqId(qid);
			List<Paper_fillchoice> paper_fillchoicesold = paper_fillService.selectPaper_fill(paper_fill2).getFilllist();
			for(int i=0;i<paper_fillchoicesold.size();i++) {
				paper_fillchoiceService.deletePaper_fillchoice(paper_fillchoicesold.get(i));
			}
			
			//填空题存入数据库
			paper_fillService.updatePaper_fill(paper_fill);
		}else {
			paper_fillService.addPaper_fill(paper_fill);
		}
		for(int i=0;i<paper_fillchoices.size();i++) {
			paper_fillchoices.get(i).setqId(paper_fill.getqId());
			paper_fillchoiceService.addPaper_fillchoice(paper_fillchoices.get(i));
		}
		//
		paper_fill=paper_fillService.selectPaper_fill(paper_fill);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("paper_fill", paper_fill);
		
		Object json = JSON.toJSONString(map);

		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}

	
	/**
	 * 传输试卷新增简答题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transhortanswer")
	@ResponseBody
	public void transhortanswer(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据
		Integer paperid = p.getPaperid();
//		Integer paperid = obj.getInteger("paperid");
		String shortanswertitle = obj.getString("shortanswer_title");
		Integer shortanswerscore = obj.getInteger("shortanswerscore");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		
		Paper_shortanswer paper_shortanswer = new Paper_shortanswer();
		paper_shortanswer.setqTitle(shortanswertitle);
		paper_shortanswer.setqScore(shortanswerscore);
		paper_shortanswer.setqKnowledge(knowledge);
		paper_shortanswer.setqAnalysis(analysis);
		paper_shortanswer.setqFifficulty(difficulty);
		paper_shortanswer.setPaperid(paperid);
		paper_shortanswer.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			paper_shortanswer_Service.updatePaper_shortanswer(paper_shortanswer);
		}else {
			paper_shortanswer_Service.addPaper_shortanswer(paper_shortanswer);
		}
		paper_shortanswer=paper_shortanswer_Service.selectPaper_shortanswer(paper_shortanswer);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("paper_shortanswer", paper_shortanswer);
		
		Object json = JSON.toJSONString(map);

		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}

	//------------------------------------------------------编辑进行时传递修改编辑数据------------------------------------------------------

    /** 传输试卷修改单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/traneditsingle")
	@ResponseBody
	public Map traneditsingle(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出单选题数据
		
		//单选题存入数据库
		Paper_single paper_single = new Paper_single();
		paper_single.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		paper_single=papersingle_Service.selectPaper_single(paper_single);
		if(paper_single!=null) {
			Paper paper = new Paper();
			paper.setPaperid(paper_single.getPaperid());
			List<String> knowledges = paperService.getKnowledge(paper);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("paper_single", paper_single);
			map.put("sequence", sequence);
		}else {
			map.put("code", "6000");
		}
		
		
		return map;
	}
	
	/**
	 * 传输试卷修改多选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/traneditmultiple")
	@ResponseBody
	public Map traneditmultiple(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
	
		Paper_multiple paper_multiple = new Paper_multiple();
		paper_multiple.setqId(qid);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		paper_multiple=paper_multipleService.selectPaper_multiple(paper_multiple);
		if(paper_multiple!=null) {
			Paper paper = new Paper();
			paper.setPaperid(paper_multiple.getPaperid());
			List<String> knowledges = paperService.getKnowledge(paper);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("paper_multiple", paper_multiple);
			map.put("sequence", sequence);
		}else {
			map.put("code", "6000");
		}
		return map;
	}
	

	/**
	 * 传输试卷修改判断题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/traneditjudge")
	@ResponseBody
	public Map traneditjudge(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		Paper_judge paper_judge = new Paper_judge();
		
		paper_judge.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		paper_judge=paper_judge_Service.selectPaper_judge(paper_judge);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_judge!=null) {
			Paper paper = new Paper();
			paper.setPaperid(paper_judge.getPaperid());
			List<String> knowledges = paperService.getKnowledge(paper);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("paper_judge", paper_judge);
			map.put("sequence", sequence);
		}else {
			map.put("code", "6000");
		}
		return map;
	}
	
	
	/**
	 * 传输试卷修改填空题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/traneditfill")
	@ResponseBody
	public Map traneditfill(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");

		Paper_fill paper_fill = new Paper_fill();
		paper_fill.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		paper_fill=paper_fillService.selectPaper_fill(paper_fill);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_fill!=null) {
			Paper paper = new Paper();
			paper.setPaperid(paper_fill.getPaperid());
			List<String> knowledges = paperService.getKnowledge(paper);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("paper_fill", paper_fill);
			map.put("sequence", sequence);
		}else {
			map.put("code", "6000");
		}
		
		return map;
	}

	
	/**
	 * 传输试卷修改简答题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/traneditshortanswer")
	@ResponseBody
	public Map traneditshortanswer(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper_shortanswer paper_shortanswer = new Paper_shortanswer();	
		paper_shortanswer.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		paper_shortanswer=paper_shortanswer_Service.selectPaper_shortanswer(paper_shortanswer);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_shortanswer!=null) {
			Paper paper = new Paper();
			paper.setPaperid(paper_shortanswer.getPaperid());
			List<String> knowledges = paperService.getKnowledge(paper);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("paper_shortanswer", paper_shortanswer);
			map.put("sequence", sequence);
		}else {
			map.put("code", "6000");
		}
		return map;
	}
	
	//------------------------------------------------------编辑进行时传递删除数据------------------------------------------------------
	/**
	 * 传输试卷删除单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletesingle")
	@ResponseBody
	public Map trandeletesingle(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出单选题数据
		
		//单选题存入数据库
		Paper_single paper_single = new Paper_single();
		paper_single.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(papersingle_Service.deletePaper_single(paper_single)!=0) {
			map.put("code", "0000");
		}else {
			map.put("code", "6000");
		}
		
		
		return map;
	}
	
	/**
	 * 传输试卷删除多选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletemultiple")
	@ResponseBody
	public Map trandeletemultiple(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
	
		Paper_multiple paper_multiple = new Paper_multiple();
		paper_multiple.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_multipleService.deletePaper_multiple(paper_multiple)!=0) {
			map.put("code", "0000");
		}else {
			map.put("code", "6000");
		}
		return map;
	}
	

	/**
	 * 传输试卷删除判断题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletejudge")
	@ResponseBody
	public Map trandeletejudge(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		Paper_judge paper_judge = new Paper_judge();
		
		paper_judge.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_judge_Service.deletePaper_judge(paper_judge)!=0) {
			map.put("code", "0000");
		}else {
			map.put("code", "6000");
		}
		return map;
	}
	
	
	/**
	 * 传输试卷删除填空题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletefill")
	@ResponseBody
	public Map trandeletefill(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");

		Paper_fill paper_fill = new Paper_fill();
		paper_fill.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_fillService.deletePaper_fill(paper_fill)!=0) {
			map.put("code", "0000");
		}else {
			map.put("code", "6000");
		}
		
		return map;
	}

	
	/**
	 * 传输试卷删除简答题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeleteshortanswer")
	@ResponseBody
	public Map trandeleteshortanswer(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据		
		Paper_shortanswer paper_shortanswer = new Paper_shortanswer();	
		paper_shortanswer.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(paper_shortanswer_Service.deletePaper_shortanswer(paper_shortanswer)!=0) {
			map.put("code", "0000");
		}else {
			map.put("code", "6000");
		}
		return map;
	}

	//------------------------------------------------------批量删除数据------------------------------------------------------
	/**
	 * 批量传输试卷删除单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletesingles")
	@ResponseBody
	public Map trandeletesingles(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		
		//得到选项内容
		JSONArray singleids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<singleids.size();i++) {
			Paper_single paper_single = new Paper_single();
			paper_single.setqId((Integer)singleids.get(i));
			papersingle_Service.deletePaper_single(paper_single);
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		return map;
	}
	
	/**
	 * 批量传输试卷删除多选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletemultiples")
	@ResponseBody
	public Map trandeletemultiples(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
		JSONArray multipleids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<multipleids.size();i++) {
			Paper_multiple paper_multiple = new Paper_multiple();
			paper_multiple.setqId((Integer)multipleids.get(i));
			paper_multipleService.deletePaper_multiple(paper_multiple);
		}
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		return map;
	}
	

	/**
	 * 批量传输试卷删除判断题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletejudges")
	@ResponseBody
	public Map trandeletejudges(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		JSONArray judgeids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<judgeids.size();i++) {
			Paper_judge paper_judge = new Paper_judge();
			paper_judge.setqId((Integer)judgeids.get(i));
			paper_judge_Service.deletePaper_judge(paper_judge);
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		return map;
	}
	
	
	/**
	 * 批量传输试卷删除填空题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeletefills")
	@ResponseBody
	public Map trandeletefills(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		JSONArray fillids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<fillids.size();i++) {
			Paper_fill paper_fill = new Paper_fill();
			paper_fill.setqId((Integer)fillids.get(i));
			paper_fillService.deletePaper_fill(paper_fill);
			
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		
		return map;
	}

	
	/**
	 * 批量传输试卷删除简答题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/trandeleteshortanswers")
	@ResponseBody
	public Map trandeleteshortanswers(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据		
		JSONArray shortanswerids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<shortanswerids.size();i++) {
			Paper_shortanswer paper_shortanswer = new Paper_shortanswer();	
			paper_shortanswer.setqId((Integer)shortanswerids.get(i));
			paper_shortanswer_Service.deletePaper_shortanswer(paper_shortanswer);
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");	
		return map;
	}
	
	//------------------------------------------------------批量设置题目分值------------------------------------------------------
	
	/** 传输设置分值单选题数组
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranpaperscoresingle")
	@ResponseBody
	public void tranpaperscoresingle(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		//得到题目id和分数
		JSONArray checkchoices =  obj.getJSONArray("checkchoices");
		Integer score =  obj.getInteger("score");

		for(int i=0;i<checkchoices.size();i++) {
			Paper_single paper_single = new Paper_single();
			paper_single.setqId((Integer)checkchoices.get(i));
			paper_single.setqScore(score);
			papersingle_Service.updatePaper_single(paper_single);
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		Object json = JSON.toJSONString(map);
		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}
	
	/**
	 * 传输设置分值多选题数组
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranpaperscoremultiple")
	@ResponseBody
	public void tranpaperscoremultiple(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		//得到题目id和分数
				JSONArray checkchoices =  obj.getJSONArray("checkchoices");
				Integer score =  obj.getInteger("score");

				for(int i=0;i<checkchoices.size();i++) {
					Paper_multiple paper_multiple = new Paper_multiple();
					paper_multiple.setqId((Integer)checkchoices.get(i));
					paper_multiple.setqScore(score);
					paper_multipleService.updatePaper_multiple(paper_multiple);
				}
				
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("code", "0000");
				Object json = JSON.toJSONString(map);
				// 将map传过去
				res.setContentType("text/html;charset=utf-8");
				res.getWriter().print(json);
				return;
	}
	

	/**
	 * 传输设置分值判断题数组
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranpaperscorejudge")
	@ResponseBody
	public void tranpaperscorejudge(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		//得到题目id和分数
		JSONArray checkchoices =  obj.getJSONArray("checkchoices");
		Integer score =  obj.getInteger("score");

		for(int i=0;i<checkchoices.size();i++) {
			Paper_judge paper_judge = new Paper_judge();
			paper_judge.setqId((Integer)checkchoices.get(i));
			paper_judge.setqScore(score);
			paper_judge_Service.updatePaper_judge(paper_judge);
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		Object json = JSON.toJSONString(map);
		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}
	
	
	/**
	 * 传输设置分值填空题数组
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranpaperscorefill")
	@ResponseBody
	public void tranpaperscorefill(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		//得到题目id和分数
				JSONArray checkchoices =  obj.getJSONArray("checkchoices");
				Integer score =  obj.getInteger("score");

				for(int i=0;i<checkchoices.size();i++) {
					Paper_fill paper_fill = new Paper_fill();
					paper_fill.setqId((Integer)checkchoices.get(i));
					paper_fill.setqScore(score);
					paper_fillService.updatePaper_fill(paper_fill);
				}
				
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				map.put("code", "0000");
				Object json = JSON.toJSONString(map);
				// 将map传过去
				res.setContentType("text/html;charset=utf-8");
				res.getWriter().print(json);
				return;
	}

	
	/**
	 * 传输设置分值简答题数组
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranpaperscoreshortanswer")
	@ResponseBody
	public void tranpaperscoreshortanswer(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		JSONArray checkchoices =  obj.getJSONArray("checkchoices");
		Integer score =  obj.getInteger("score");

		for(int i=0;i<checkchoices.size();i++) {
			Paper_shortanswer paper_shortanswer = new Paper_shortanswer();
			paper_shortanswer.setqId((Integer)checkchoices.get(i));
			paper_shortanswer.setqScore(score);
			paper_shortanswer_Service.updatePaper_shortanswer(paper_shortanswer);
		}
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");
		Object json = JSON.toJSONString(map);
		// 将map传过去
		res.setContentType("text/html;charset=utf-8");
		res.getWriter().print(json);
		return;
	}

	
	//------------------------------------------------------上移下移试卷数据------------------------------------------------------

	/**
	 * 传输试卷移动单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovesingle")
	@ResponseBody
	public Map tranmovesingle(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出单选题数据
		Paper_single paper_single1 = new Paper_single();
		Paper_single paper_single2 = new Paper_single();
		Paper_single paper_temp = new Paper_single();
		paper_single1.setqId(q1id);
		paper_single2.setqId(q2id);
		//交换信息
		System.out.println("q1id"+q1id+"q2id"+q2id);
		paper_temp = papersingle_Service.selectPaper_single(paper_single1);
		paper_single2 = papersingle_Service.selectPaper_single(paper_single2);
		paper_single1 = paper_single2;
		paper_single1.setqId(q1id);
		paper_single2 = paper_temp;
		paper_single2.setqId(q2id);
		papersingle_Service.updatePaper_single(paper_single1);
		papersingle_Service.updatePaper_single(paper_single2);
		
		for(int i=0;i<paper_single1.getSinglelist().size();i++) {
			paper_single1.getSinglelist().get(i).setqId(q1id);
			paper_singlechoiceService.updatePaper_singlechoice(paper_single1.getSinglelist().get(i));
		}
		
		for(int i=0;i<paper_single2.getSinglelist().size();i++) {
			paper_single2.getSinglelist().get(i).setqId(q2id);
			paper_singlechoiceService.updatePaper_singlechoice(paper_single2.getSinglelist().get(i));
		}
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code", "0000");
		
		return map;
	}
	
	/**
	 * 传输试卷移动多选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovemultiple")
	@ResponseBody
	public Map tranmovemultiple(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
		System.out.println(q1id+','+q2id);
		Paper_multiple paper_multiple1 = new Paper_multiple();
		Paper_multiple paper_multiple2 = new Paper_multiple();
		Paper_multiple paper_temp = new Paper_multiple();
		paper_multiple1.setqId(q1id);
		paper_multiple2.setqId(q2id);
		//交换信息
		paper_temp = paper_multipleService.selectPaper_multiple(paper_multiple1);
		paper_multiple2 = paper_multipleService.selectPaper_multiple(paper_multiple2);
		paper_multiple1 = paper_multiple2;
		paper_multiple1.setqId(q1id);
		paper_multiple2 = paper_temp;
		paper_multiple2.setqId(q2id);
		paper_multipleService.updatePaper_multiple(paper_multiple1);
		paper_multipleService.updatePaper_multiple(paper_multiple2);
		
		for(int i=0;i<paper_multiple1.getMultiplelist().size();i++) {
			paper_multiple1.getMultiplelist().get(i).setqId(q1id);
			paper_multiplechoiceService.updatePaper_multiplechoice(paper_multiple1.getMultiplelist().get(i));
		}
		
		for(int i=0;i<paper_multiple2.getMultiplelist().size();i++) {
			paper_multiple2.getMultiplelist().get(i).setqId(q2id);
			paper_multiplechoiceService.updatePaper_multiplechoice(paper_multiple2.getMultiplelist().get(i));
		}
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code", "0000");
		
		return map;
	}
	

	/**
	 * 传输试卷移动判断题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovejudge")
	@ResponseBody
	public Map tranmovejudge(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据

		Paper_judge paper_judge1 = new Paper_judge();
		Paper_judge paper_judge2 = new Paper_judge();
		Paper_judge paper_temp = new Paper_judge();
		paper_judge1.setqId(q1id);
		paper_judge2.setqId(q2id);
		//交换信息
		paper_temp = paper_judge_Service.selectPaper_judge(paper_judge1);
		paper_judge2 = paper_judge_Service.selectPaper_judge(paper_judge2);
		paper_judge1 = paper_judge2;
		paper_judge1.setqId(q1id);
		paper_judge2 = paper_temp;
		paper_judge2.setqId(q2id);
		paper_judge_Service.updatePaper_judge(paper_judge1);
		paper_judge_Service.updatePaper_judge(paper_judge2);
		
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code", "0000");
		
		return map;
	}
	
	
	/**
	 * 传输试卷移动填空题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovefill")
	@ResponseBody
	public Map tranmovefill(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");

		Paper_fill paper_fill1 = new Paper_fill();
		Paper_fill paper_fill2 = new Paper_fill();
		Paper_fill paper_temp = new Paper_fill();
		paper_fill1.setqId(q1id);
		paper_fill2.setqId(q2id);
		//交换信息
		paper_temp = paper_fillService.selectPaper_fill(paper_fill1);
		paper_fill2 = paper_fillService.selectPaper_fill(paper_fill2);
		paper_fill1 = paper_fill2;
		paper_fill1.setqId(q1id);
		paper_fill2 = paper_temp;
		paper_fill2.setqId(q2id);
		paper_fillService.updatePaper_fill(paper_fill1);
		paper_fillService.updatePaper_fill(paper_fill2);
		
		for(int i=0;i<paper_fill1.getFilllist().size();i++) {
			paper_fill1.getFilllist().get(i).setqId(q1id);
			paper_fillchoiceService.updatePaper_fillchoice(paper_fill1.getFilllist().get(i));
		}
		
		for(int i=0;i<paper_fill2.getFilllist().size();i++) {
			paper_fill2.getFilllist().get(i).setqId(q2id);
			paper_fillchoiceService.updatePaper_fillchoice(paper_fill2.getFilllist().get(i));
		}
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code", "0000");
		
		return map;
	}

	
	/**
	 * 传输试卷移动简答题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmoveshortanswer")
	@ResponseBody
	public Map tranmoveshortanswer(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据		
		Paper_shortanswer paper_shortanswer1 = new Paper_shortanswer();
		Paper_shortanswer paper_shortanswer2 = new Paper_shortanswer();
		Paper_shortanswer paper_temp = new Paper_shortanswer();
		paper_shortanswer1.setqId(q1id);
		paper_shortanswer2.setqId(q2id);
		//交换信息
		paper_temp = paper_shortanswer_Service.selectPaper_shortanswer(paper_shortanswer1);
		paper_shortanswer2 = paper_shortanswer_Service.selectPaper_shortanswer(paper_shortanswer2);
		paper_shortanswer1 = paper_shortanswer2;
		paper_shortanswer1.setqId(q1id);
		paper_shortanswer2 = paper_temp;
		paper_shortanswer2.setqId(q2id);
		paper_shortanswer_Service.updatePaper_shortanswer(paper_shortanswer1);
		paper_shortanswer_Service.updatePaper_shortanswer(paper_shortanswer2);
		
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code", "0000");
		
		return map;
	}
	//------------------------------------------------------获得每部分知识点题目、分数信息------------------------------------------------------
	
	/**
	 * 传输试卷单选题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transinglekown")
	@ResponseBody
	public Map transinglekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出单选题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_single> paper_singles = papersingle_Service.getAllbyPaperid(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_singles.size();j++) {
				if(paper_singles.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_singles.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		

		map.put("kownledgs",kownledgsinfor);
		return map;
	}
	
	/**
	 * 传输试卷多选题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmultiplekown")
	@ResponseBody
	public Map tranmultiplekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_multiple> paper_multiples = paper_multipleService.getAllbyPaperid(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_multiples.size();j++) {
				if(paper_multiples.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_multiples.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}
		map.put("kownledgs",kownledgsinfor);
		return map;
	}
	

	/**
	 * 传输试卷判断题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovejudgekown")
	@ResponseBody
	public Map tranjudgekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		List<Paper_judge> paper_judges = paper_judge_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_judges.size();j++) {
				if(paper_judges.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_judges.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		map.put("kownledgs",kownledgsinfor);
		return map;
	}
	
	
	/**
	 * 传输试卷填空题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranfillkown")
	@ResponseBody
	public Map tranfillkown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		List<Paper_fill> paper_fills = paper_fillService.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_fills.size();j++) {
				if(paper_fills.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_fills.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		map.put("kownledgs",kownledgsinfor);
		return map;
	}

	
	/**
	 * 传输试卷简答题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transhortanswerkown")
	@ResponseBody
	public Map transhortanswerkown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		Integer kownlegsnum = 0; 
		Integer score = 0; 
		List<Paper_shortanswer> paper_shortanswers = paper_shortanswer_Service.getAllbyPaperid(paper);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<paper_shortanswers.size();j++) {
				if(paper_shortanswers.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
					score+=paper_shortanswers.get(j).getqScore();
				}
			}
			maplittle.put("num",kownlegsnum);
			maplittle.put("score",score);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
			score = 0;
		}		
		map.put("kownledgs",kownledgsinfor);
		return map;
	}
	
	//------------------------------------------------------根据知识点删题------------------------------------------------------
	

	/**
	 * 传输试卷单选题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transinglekowndelete")
	@ResponseBody
	public Map transinglekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出单选题数据

		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_single> paper_singles = papersingle_Service.getAllbyPaperid(paper);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
			for(int i=0;i<paper_singles.size();i++) {
				if(paper_singles.get(i).getqKnowledge().equals(knowname)) {
					papersingle_Service.deletePaper_single(paper_singles.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}
	
	/**
	 * 传输试卷多选题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmultiplekowndelete")
	@ResponseBody
	public Map tranmultiplekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出多选题数据
		Paper paper = new Paper();
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_multiple> paper_multiples = paper_multipleService.getAllbyPaperid(paper);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<paper_multiples.size();i++) {
				if(paper_multiples.get(i).getqKnowledge().equals(knowname)) {
					paper_multipleService.deletePaper_multiple(paper_multiples.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}
	

	/**
	 * 传输试卷判断题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranmovejudgekowndelete")
	@ResponseBody
	public Map tranjudgekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出判断题数据
		Paper paper = new Paper();
//		Integer paperid = paper.getPaperid();
		
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_judge> paper_judges = paper_judge_Service.getAllbyPaperid(paper);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<paper_judges.size();i++) {
				if(paper_judges.get(i).getqKnowledge().equals(knowname)) {
					paper_judge_Service.deletePaper_judge(paper_judges.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}
	
	
	/**
	 * 传输试卷填空题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranfillkowndelete")
	@ResponseBody
	public Map tranfillkowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		Paper paper = new Paper();
//		Integer paperid = paper.getPaperid();
		
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_fill> paper_fills = paper_fillService.getAllbyPaperid(paper);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<paper_fills.size();i++) {
				if(paper_fills.get(i).getqKnowledge().equals(knowname)) {
					paper_fillService.deletePaper_fill(paper_fills.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}

	
	/**
	 * 传输试卷简答题知识点
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/transhortanswerkowndelete")
	@ResponseBody
	public Map transhortanswerkowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Paper p = (Paper) req.getSession().getAttribute("currentPaper1");
		//取出简答题数据		
		Paper paper =new Paper();
//		Integer paperid = paper.getPaperid();
		
		paper.setPaperid(p.getPaperid());
		List<String> kownledgs=paperService.getKnowledge(paper);
		List<Paper_shortanswer> paper_shortanswers = paper_shortanswer_Service.getAllbyPaperid(paper);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<paper_shortanswers.size();i++) {
				if(paper_shortanswers.get(i).getqKnowledge().equals(knowname)) {
					paper_shortanswer_Service.deletePaper_shortanswer(paper_shortanswers.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}
	
}
