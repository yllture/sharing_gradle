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
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Bank_fill;
import com.neuedu.entity.Bank_fillchoice;
import com.neuedu.entity.Bank_judge;
import com.neuedu.entity.Bank_multiple;
import com.neuedu.entity.Bank_multiplechoice;
import com.neuedu.entity.Bank_shortanswer;
import com.neuedu.entity.Bank_single;
import com.neuedu.entity.Bank_singlechoice;
import com.neuedu.entity.Bank_fill;
import com.neuedu.entity.Bank_judge;
import com.neuedu.entity.Bank_multiple;
import com.neuedu.entity.Bank_shortanswer;
import com.neuedu.entity.Bank_single;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Bank_fill;
import com.neuedu.entity.Bank_judge;
import com.neuedu.entity.Bank_multiple;
import com.neuedu.entity.Bank_shortanswer;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Bank_single;
import com.neuedu.entity.User;
import com.neuedu.service.BankService;
import com.neuedu.service.BankService;
import com.neuedu.service.Bank_fillService;
import com.neuedu.service.Bank_fillchoiceService;
import com.neuedu.service.Bank_judgeService;
import com.neuedu.service.Bank_multipleService;
import com.neuedu.service.Bank_multiplechoiceService;
import com.neuedu.service.Bank_shortanswerService;
import com.neuedu.service.Bank_singleService;
import com.neuedu.service.Bank_singlechoiceService;

@Controller
public class EditorBankController {
	@Autowired
	BankService bankService;
	@Autowired
	Bank_fillService bank_fillService;
	@Autowired
	Bank_judgeService bank_judge_Service;
	@Autowired
	Bank_multipleService bank_multipleService;
	@Autowired
	Bank_shortanswerService bank_shortanswer_Service;
	@Autowired
	Bank_singleService banksingle_Service;
	@Autowired
	Bank_singlechoiceService bank_singlechoiceService;
	@Autowired
	Bank_multiplechoiceService bank_multiplechoiceService;
	@Autowired
	Bank_fillchoiceService bank_fillchoiceService;

	/**
	 * 用户点击新建试卷生成试卷信息跳转编辑页
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/newBank")
	public String newBank(Model m,HttpServletRequest req, HttpServletResponse res,String bankname) throws IOException {
		User user = (User) req.getSession().getAttribute("currentUser");
		Integer winder=user.getUserid();
		Bank newBank = new Bank();
		newBank.setBankname(bankname);
		bankService.addBank(newBank);
		m.addAttribute("bank", newBank);
		return "teacherBank/bankEditor";
	}
	
	
	//------------------------------------------------------编辑起始试卷内容时的页面------------------------------------------------------
	/**
	 * 用户点击编辑试卷跳转编辑页
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/editorBank")
	public String editorBank(Model m,HttpServletRequest req, HttpServletResponse res,Integer bankid) throws IOException {
		Bank bank = new Bank();
		bank.setBankid(bankid);
		req.getSession().setAttribute("currentBank", bank);
		Bank editorBank = bankService.selectBankByID(bank);
		//单选题
		List<Bank_single> singles= banksingle_Service.getAllbyBankid(editorBank);
		//多选题
		List<Bank_multiple> multiples= bank_multipleService.getAllbyBankid(editorBank);
		//判断题
		List<Bank_judge> judges= bank_judge_Service.getAllbyBankid(editorBank);
		//填空题
		List<Bank_fill> fills= bank_fillService.getAllbyBankid(editorBank);
		//简答题
		List<Bank_shortanswer> shortanswers= bank_shortanswer_Service.getAllbyBankid(editorBank);
		//知识点
		List<String> kownledgs = bankService.getKnowledge(bank);
		List<Map<String, Object>> kownledgsinlefor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinmufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinjufor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinfifor= new LinkedList<Map<String,Object>>();
		List<Map<String, Object>> kownledgsinshfor= new LinkedList<Map<String,Object>>();
		//--------------------------------------------------获取单选题题目信息部分信息-----------------------------------------------------
		List<Bank_single> bank_singles = banksingle_Service.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_singles.size();j++) {
				if(bank_singles.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinlefor.add(maplittle);
			}
			kownlegsnum=0;
		}
		//--------------------------------------------------获取多选题题目信息部分信息-----------------------------------------------------
		List<Bank_multiple> bank_multiples = bank_multipleService.getAllbyBankid(bank);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_multiples.size();j++) {
				if(bank_multiples.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinmufor.add(maplittle);
			}
			kownlegsnum=0;
		}
		//--------------------------------------------------获取判断题题目信息部分信息-----------------------------------------------------
		List<Bank_judge> bank_judges = bank_judge_Service.getAllbyBankid(bank);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_judges.size();j++) {
				if(bank_judges.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinjufor.add(maplittle);
			}
			kownlegsnum=0;
		}		
		//--------------------------------------------------获取填空题题目信息部分信息-----------------------------------------------------
		List<Bank_fill> bank_fills = bank_fillService.getAllbyBankid(bank);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_fills.size();j++) {
				if(bank_fills.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfifor.add(maplittle);
			}
			kownlegsnum=0;
		}		
		//--------------------------------------------------获取简答题题目信息部分信息-----------------------------------------------------
		List<Bank_shortanswer> bank_shortanswers = bank_shortanswer_Service.getAllbyBankid(bank);
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_shortanswers.size();j++) {
				if(bank_shortanswers.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinshfor.add(maplittle);
			}
			kownlegsnum=0;
		}		
		m.addAttribute("kownledgsinlefor",kownledgsinlefor);
		m.addAttribute("kownledgsinmufor",kownledgsinmufor);
		m.addAttribute("kownledgsinfifor",kownledgsinfifor);
		m.addAttribute("kownledgsinjufor",kownledgsinjufor);
		m.addAttribute("kownledgsinshfor",kownledgsinshfor);
		m.addAttribute("Bank", editorBank);
		
//		System.out.println("duosho"+multiples.get(0).getMultiplelists().size());
		m.addAttribute("singles",singles);
		m.addAttribute("multiples",multiples);
		m.addAttribute("judges",judges);
		m.addAttribute("fills",fills);
		m.addAttribute("shortanswers",shortanswers);
		return "teacherBank/editorBank";
	}

	
	//----------------------------------------------------点击编辑页取出对应的知识点信息----------------------------------------------------
	@RequestMapping("/getbankkonwledges")
	@ResponseBody
	public Map getbankkonwledges(Integer bankid,HttpServletRequest req) {
		Bank bank = new Bank();
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		bank.setBankid(b.getBankid());
		List<String> knowledges = bankService.getKnowledge(bank);
	
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
	@RequestMapping("/tranbanksingle")
	@ResponseBody
	public void tranbanksingle(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		Integer bankid = b.getBankid();
//		System.out.print("obj"+JSON.toJSONString(obj));
//		Integer bankid = obj.getInteger("bankid");
		String singeltitle = obj.getString("single_title");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项内容
		JSONArray singlechoicecontent =  obj.getJSONArray("singleChoicesContent");
		//得到选项对错
		JSONArray singlechoicejudge =  obj.getJSONArray("singlechoicejudge");
		
		
		//单选题存入数据库
		Bank_single bank_single = new Bank_single();
		bank_single.setqTitle(singeltitle);
		bank_single.setqKnowledge(knowledge);
		bank_single.setqAnalysis(analysis);
		bank_single.setqDifficulty(difficulty);
		bank_single.setBankid(bankid);
		bank_single.setqId(qid);
		List<Bank_singlechoice> bank_singlechoices = new ArrayList<Bank_singlechoice>();
		
		for(int i=0;i<singlechoicecontent.size();i++) {
			Bank_singlechoice bank_singlechoice = new Bank_singlechoice();
			bank_singlechoice.setContent((String)singlechoicecontent.get(i));
			bank_singlechoice.setType((Integer)singlechoicejudge.get(i));
			bank_singlechoice.setSequence(i+1);
			bank_singlechoices.add(bank_singlechoice);
		}
		
		//更新
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			Bank_single bank_single2 = new Bank_single();
			bank_single2.setqId(qid);
			List<Bank_singlechoice> bank_singlechoicesold = banksingle_Service.selectBank_single(bank_single2).getSinglelist();
			for(int i=0;i<bank_singlechoicesold.size();i++) {
				bank_singlechoiceService.deleteBank_singlechoice(bank_singlechoicesold.get(i));
			}
			banksingle_Service.updateBank_single(bank_single);
		}
		else {
			banksingle_Service.addBank_single(bank_single);
		}
		
		Integer bank_single_id = bank_single.getqId();
		//选项存入数据库
		for(int i=0;i<bank_singlechoices.size();i++) {
			bank_singlechoices.get(i).setqId(bank_single_id);;
			bank_singlechoiceService.addBank_singlechoice(bank_singlechoices.get(i));
		}
		//
		bank_single = banksingle_Service.selectBank_single(bank_single);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("bank_single", bank_single);
		
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
	@RequestMapping("/tranbankmultiple")
	@ResponseBody
	public void tranbankmultiple(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
		Integer bankid = b.getBankid();
//		Integer bankid = obj.getInteger("bankid");
		String multipletitle = obj.getString("multiple_title");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项内容
		JSONArray multiplechoicecontent =  obj.getJSONArray("multipleChoicesContent");
		//得到选项对错
		JSONArray multiplechoicejudge =  obj.getJSONArray("multiplechoicejudge");
		
		
		
		Bank_multiple bank_multiple = new Bank_multiple();
		bank_multiple.setqTitle(multipletitle);
		bank_multiple.setqKnowledge(knowledge);
		bank_multiple.setqAnalysis(analysis);
		bank_multiple.setqDifficulty(difficulty);
		bank_multiple.setBankid(bankid);
		bank_multiple.setqId(qid);
		List<Bank_multiplechoice> bank_multiplechoices = new ArrayList<Bank_multiplechoice>();
		for(int i=0;i<multiplechoicecontent.size();i++) {
			Bank_multiplechoice bank_multiplechoice = new Bank_multiplechoice();
			bank_multiplechoice.setContent((String)multiplechoicecontent.get(i));
			bank_multiplechoice.setType((Integer)multiplechoicejudge.get(i));
			bank_multiplechoice.setSequence(i+1);
			bank_multiplechoices.add(bank_multiplechoice);
		}
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			Bank_multiple bank_multiple2 = new Bank_multiple();
			bank_multiple2.setqId(qid);
			List<Bank_multiplechoice> bank_multiplechoicesold = bank_multipleService.selectBank_multiple(bank_multiple2).getMultiplelist();
			for(int i=0;i<bank_multiplechoicesold.size();i++) {
				bank_multiplechoiceService.deleteBank_multiplechoice(bank_multiplechoicesold.get(i));
			}
			//多选题存入数据库
			bank_multipleService.updateBank_multiple(bank_multiple);
		}else {
			bank_multipleService.addBank_multiple(bank_multiple);
		}
		
		
		Integer bank_multiple_id = bank_multiple.getqId();
		//选项存入数据库
		for(int i=0;i<bank_multiplechoices.size();i++) {
			bank_multiplechoices.get(i).setqId(bank_multiple_id);;
			bank_multiplechoiceService.addBank_multiplechoice(bank_multiplechoices.get(i));
		}
		//
		bank_multiple=bank_multipleService.selectBank_multiple(bank_multiple);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("bank_multiple", bank_multiple);
		
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
	@RequestMapping("/tranbankjudge")
	@ResponseBody
	public void tranbankjudge(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		Integer bankid = b.getBankid();
//		Integer bankid = obj.getInteger("bankid");
		String judgetitle = obj.getString("judge_title");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到选项对错
		Integer judge =  obj.getInteger("isture");
		
		
		
		Bank_judge bank_judge = new Bank_judge();
		bank_judge.setqTitle(judgetitle);
		bank_judge.setqKnowledge(knowledge);
		bank_judge.setqAnalysis(analysis);
		bank_judge.setqDifficulty(difficulty);
		bank_judge.setBankid(bankid);
		bank_judge.setIsture(judge);
		bank_judge.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			bank_judge_Service.updateBank_judge(bank_judge);
		}else {
			
			bank_judge_Service.addBank_judge(bank_judge);
		}
		
		System.out.println("22"+bank_judge.getqId());
		bank_judge=bank_judge_Service.selectBank_judge(bank_judge);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("bank_judge", bank_judge);
		
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
	@RequestMapping("/tranbankfill")
	@ResponseBody
	public void tranbankfill(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出填空题数据
		Integer bankid = b.getBankid();
//		Integer bankid = obj.getInteger("bankid");
		String filltitle = obj.getString("fill_title");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		//得到填空题答案
		JSONArray fillcontent =  obj.getJSONArray("fillcontent");
		
		List<Bank_fillchoice> bank_fillchoices = new ArrayList<Bank_fillchoice>();
		for(int i=0;i<fillcontent.size();i++) {
			Bank_fillchoice bank_fillchoice = new Bank_fillchoice();
			bank_fillchoice.setContent((String)fillcontent.get(i));
			bank_fillchoice.setSequence(i+1);
			bank_fillchoices.add(bank_fillchoice);
		}
		
		
		Bank_fill bank_fill = new Bank_fill();
		bank_fill.setqTitle(filltitle);
		bank_fill.setqKnowledge(knowledge);
		bank_fill.setqAnalysis(analysis);
		bank_fill.setqDifficulty(difficulty);
		bank_fill.setBankid(bankid);
		bank_fill.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			bank_fillService.deleteBank_fill(bank_fill);
			Bank_fill bank_fill2 = new Bank_fill();
			bank_fill2.setqId(qid);
			List<Bank_fillchoice> bank_fillchoicesold = bank_fillService.selectBank_fill(bank_fill2).getFilllsit();
			for(int i=0;i<bank_fillchoicesold.size();i++) {
				bank_fillchoiceService.deleteBankfillchoice(bank_fillchoicesold.get(i));
			}
			
			//填空题存入数据库
			bank_fillService.updateBank_fill(bank_fill);
		}else {
			bank_fillService.addBank_fill(bank_fill);
		}
		for(int i=0;i<bank_fillchoices.size();i++) {
			bank_fillchoices.get(i).setqId(bank_fill.getqId());
			bank_fillchoiceService.addBank_fillchoice(bank_fillchoices.get(i));
		}
		//
		bank_fill=bank_fillService.selectBank_fill(bank_fill);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("bank_fill", bank_fill);
		
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
	@RequestMapping("/tranbankshortanswer")
	@ResponseBody
	public void tranbankshortanswer(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据
		Integer bankid = b.getBankid();
//		Integer bankid = obj.getInteger("bankid");
		String shortanswertitle = obj.getString("shortanswer_title");
		String knowledge =obj.getString("knowledge");
		String analysis = obj.getString("analysis");
		Integer difficulty = obj.getInteger("difficulty");
		Integer qid = obj.getInteger("qid");
		
		Bank_shortanswer bank_shortanswer = new Bank_shortanswer();
		bank_shortanswer.setqTitle(shortanswertitle);
		bank_shortanswer.setqKnowledge(knowledge);
		bank_shortanswer.setqAnalysis(analysis);
		bank_shortanswer.setqFifficulty(difficulty);
		bank_shortanswer.setBankid(bankid);
		bank_shortanswer.setqId(qid);
		if(qid!=null) {
			//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
			bank_shortanswer_Service.updateBank_shortanswer(bank_shortanswer);
		}else {
			bank_shortanswer_Service.addBank_shortanswer(bank_shortanswer);
		}
		bank_shortanswer=bank_shortanswer_Service.selectBank_shortanswer(bank_shortanswer);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("bank_shortanswer", bank_shortanswer);
		
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
	@RequestMapping("/tranbankeditsingle")
	@ResponseBody
	public Map tranbankeditsingle(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		
		//单选题存入数据库
		Bank_single bank_single = new Bank_single();
		bank_single.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		bank_single=banksingle_Service.selectBank_single(bank_single);
		if(bank_single!=null) {
			Bank bank = new Bank();
			bank.setBankid(bank_single.getBankid());
			List<String> knowledges = bankService.getKnowledge(bank);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("bank_single", bank_single);
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
	@RequestMapping("/tranbankeditmultiple")
	@ResponseBody
	public Map tranbankeditmultiple(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
	
		Bank_multiple bank_multiple = new Bank_multiple();
		bank_multiple.setqId(qid);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		bank_multiple=bank_multipleService.selectBank_multiple(bank_multiple);
		if(bank_multiple!=null) {
			Bank bank = new Bank();
			bank.setBankid(bank_multiple.getBankid());
			List<String> knowledges = bankService.getKnowledge(bank);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("bank_multiple", bank_multiple);
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
	@RequestMapping("/tranbankeditjudge")
	@ResponseBody
	public Map tranbankeditjudge(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		Bank_judge bank_judge = new Bank_judge();
		
		bank_judge.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		bank_judge=bank_judge_Service.selectBank_judge(bank_judge);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_judge!=null) {
			Bank bank = new Bank();
			bank.setBankid(bank_judge.getBankid());
			List<String> knowledges = bankService.getKnowledge(bank);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("bank_judge", bank_judge);
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
	@RequestMapping("/tranbankeditfill")
	@ResponseBody
	public Map tranbankeditfill(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");

		Bank_fill bank_fill = new Bank_fill();
		bank_fill.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		bank_fill=bank_fillService.selectBank_fill(bank_fill);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_fill!=null) {
			Bank bank = new Bank();
			bank.setBankid(bank_fill.getBankid());
			List<String> knowledges = bankService.getKnowledge(bank);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("bank_fill", bank_fill);
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
	@RequestMapping("/tranbankeditshortanswer")
	@ResponseBody
	public Map tranbankeditshortanswer(Integer qid,Integer sequence,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank_shortanswer bank_shortanswer = new Bank_shortanswer();	
		bank_shortanswer.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		bank_shortanswer=bank_shortanswer_Service.selectBank_shortanswer(bank_shortanswer);
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_shortanswer!=null) {
			Bank bank = new Bank();
			bank.setBankid(bank_shortanswer.getBankid());
			List<String> knowledges = bankService.getKnowledge(bank);
			map.put("knowledges", knowledges);
			map.put("code", "0000");
			map.put("bank_shortanswer", bank_shortanswer);
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
	@RequestMapping("/tranbankdeletesingle")
	@ResponseBody
	public Map tranbankdeletesingle(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		
		//单选题存入数据库
		Bank_single bank_single = new Bank_single();
		bank_single.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(banksingle_Service.deleteBank_single(bank_single)!=0) {
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
	@RequestMapping("/tranbankdeletemultiple")
	@ResponseBody
	public Map tranbankdeletemultiple(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
	
		Bank_multiple bank_multiple = new Bank_multiple();
		bank_multiple.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_multipleService.deleteBank_multiple(bank_multiple)!=0) {
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
	@RequestMapping("/tranbankdeletejudge")
	@ResponseBody
	public Map tranbankdeletejudge(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		Bank_judge bank_judge = new Bank_judge();
		
		bank_judge.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_judge_Service.deleteBank_judge(bank_judge)!=0) {
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
	@RequestMapping("/tranbankdeletefill")
	@ResponseBody
	public Map tranbankdeletefill(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");

		Bank_fill bank_fill = new Bank_fill();
		bank_fill.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_fillService.deleteBank_fill(bank_fill)!=0) {
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
	@RequestMapping("/tranbankdeleteshortanswer")
	@ResponseBody
	public Map tranbankdeleteshortanswer(Integer qid,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据		
		Bank_shortanswer bank_shortanswer = new Bank_shortanswer();	
		bank_shortanswer.setqId(qid);
		//删除原有的加入全新的选项因为设计选项被删数据库数据残留不做更新
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if(bank_shortanswer_Service.deleteBank_shortanswer(bank_shortanswer)!=0) {
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
	@RequestMapping("/tranbankdeletesingles")
	@ResponseBody
	public Map tranbankdeletesingles(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		
		//得到选项内容
		JSONArray singleids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<singleids.size();i++) {
			Bank_single bank_single = new Bank_single();
			bank_single.setqId((Integer)singleids.get(i));
			banksingle_Service.deleteBank_single(bank_single);
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
	@RequestMapping("/tranbankdeletemultiples")
	@ResponseBody
	public Map tranbankdeletemultiples(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
		JSONArray multipleids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<multipleids.size();i++) {
			Bank_multiple bank_multiple = new Bank_multiple();
			bank_multiple.setqId((Integer)multipleids.get(i));
			bank_multipleService.deleteBank_multiple(bank_multiple);
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
	@RequestMapping("/tranbankdeletejudges")
	@ResponseBody
	public Map tranbankdeletejudges(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		JSONArray judgeids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<judgeids.size();i++) {
			Bank_judge bank_judge = new Bank_judge();
			bank_judge.setqId((Integer)judgeids.get(i));
			bank_judge_Service.deleteBank_judge(bank_judge);
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
	@RequestMapping("/tranbankdeletefills")
	@ResponseBody
	public Map tranbankdeletefills(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		JSONArray fillids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<fillids.size();i++) {
			Bank_fill bank_fill = new Bank_fill();
			bank_fill.setqId((Integer)fillids.get(i));
			bank_fillService.deleteBank_fill(bank_fill);
			
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
	@RequestMapping("/tranbankdeleteshortanswers")
	@ResponseBody
	public Map tranbankdeleteshortanswers(@RequestBody JSONObject obj,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据		
		JSONArray shortanswerids =  obj.getJSONArray("checkchoices");	
		for(int i=0;i<shortanswerids.size();i++) {
			Bank_shortanswer bank_shortanswer = new Bank_shortanswer();	
			bank_shortanswer.setqId((Integer)shortanswerids.get(i));
			bank_shortanswer_Service.deleteBank_shortanswer(bank_shortanswer);
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("code", "0000");	
		return map;
	}
	
	
	//------------------------------------------------------上移下移试卷数据------------------------------------------------------

	/**
	 * 传输试卷移动单选题
	 * 
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping("/tranbankmovesingle")
	@ResponseBody
	public Map tranbankmovesingle(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		Bank_single bank_single1 = new Bank_single();
		Bank_single bank_single2 = new Bank_single();
		Bank_single bank_temp = new Bank_single();
		bank_single1.setqId(q1id);
		bank_single2.setqId(q2id);
		//交换信息
		System.out.println("q1id"+q1id+"q2id"+q2id);
		bank_temp = banksingle_Service.selectBank_single(bank_single1);
		bank_single2 = banksingle_Service.selectBank_single(bank_single2);
		bank_single1 = bank_single2;
		bank_single1.setqId(q1id);
		bank_single2 = bank_temp;
		bank_single2.setqId(q2id);
		banksingle_Service.updateBank_single(bank_single1);
		banksingle_Service.updateBank_single(bank_single2);
		
		for(int i=0;i<bank_single1.getSinglelist().size();i++) {
			bank_single1.getSinglelist().get(i).setqId(q1id);
			bank_singlechoiceService.updateBank_singlechoice(bank_single1.getSinglelist().get(i));
		}
		
		for(int i=0;i<bank_single2.getSinglelist().size();i++) {
			bank_single2.getSinglelist().get(i).setqId(q2id);
			bank_singlechoiceService.updateBank_singlechoice(bank_single2.getSinglelist().get(i));
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
	@RequestMapping("/tranbankmovemultiple")
	@ResponseBody
	public Map tranbankmovemultiple(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
	
		Bank_multiple bank_multiple1 = new Bank_multiple();
		Bank_multiple bank_multiple2 = new Bank_multiple();
		Bank_multiple bank_temp = new Bank_multiple();
		bank_multiple1.setqId(q1id);
		bank_multiple2.setqId(q2id);
		//交换信息
		bank_temp = bank_multipleService.selectBank_multiple(bank_multiple1);
		bank_multiple2 = bank_multipleService.selectBank_multiple(bank_multiple2);
		bank_multiple1 = bank_multiple2;
		bank_multiple1.setqId(q1id);
		bank_multiple2 = bank_temp;
		bank_multiple2.setqId(q2id);
		bank_multipleService.updateBank_multiple(bank_multiple1);
		bank_multipleService.updateBank_multiple(bank_multiple2);
		
		for(int i=0;i<bank_multiple1.getMultiplelist().size();i++) {
			bank_multiple1.getMultiplelist().get(i).setqId(q1id);
			bank_multiplechoiceService.updateBank_multiplechoice(bank_multiple1.getMultiplelist().get(i));
		}
		
		for(int i=0;i<bank_multiple2.getMultiplelist().size();i++) {
			bank_multiple2.getMultiplelist().get(i).setqId(q2id);
			bank_multiplechoiceService.updateBank_multiplechoice(bank_multiple2.getMultiplelist().get(i));
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
	@RequestMapping("/tranbankmovejudge")
	@ResponseBody
	public Map tranbankmovejudge(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据

		Bank_judge bank_judge1 = new Bank_judge();
		Bank_judge bank_judge2 = new Bank_judge();
		Bank_judge bank_temp = new Bank_judge();
		bank_judge1.setqId(q1id);
		bank_judge2.setqId(q2id);
		//交换信息
		bank_temp = bank_judge_Service.selectBank_judge(bank_judge1);
		bank_judge2 = bank_judge_Service.selectBank_judge(bank_judge2);
		bank_judge1 = bank_judge2;
		bank_judge1.setqId(q1id);
		bank_judge2 = bank_temp;
		bank_judge2.setqId(q2id);
		bank_judge_Service.updateBank_judge(bank_judge1);
		bank_judge_Service.updateBank_judge(bank_judge2);
		
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
	@RequestMapping("/tranbankmovefill")
	@ResponseBody
	public Map tranbankmovefill(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");

		Bank_fill bank_fill1 = new Bank_fill();
		Bank_fill bank_fill2 = new Bank_fill();
		Bank_fill bank_temp = new Bank_fill();
		bank_fill1.setqId(q1id);
		bank_fill2.setqId(q2id);
		//交换信息
		bank_temp = bank_fillService.selectBank_fill(bank_fill1);
		bank_fill2 = bank_fillService.selectBank_fill(bank_fill2);
		bank_fill1 = bank_fill2;
		bank_fill1.setqId(q1id);
		bank_fill2 = bank_temp;
		bank_fill2.setqId(q2id);
		bank_fillService.updateBank_fill(bank_fill1);
		bank_fillService.updateBank_fill(bank_fill2);
		
		for(int i=0;i<bank_fill1.getFilllsit().size();i++) {
			bank_fill1.getFilllsit().get(i).setqId(q1id);
			bank_fillchoiceService.updateBankfillchoice(bank_fill1.getFilllsit().get(i));
		}
		
		for(int i=0;i<bank_fill2.getFilllsit().size();i++) {
			bank_fill2.getFilllsit().get(i).setqId(q2id);
			bank_fillchoiceService.updateBankfillchoice(bank_fill2.getFilllsit().get(i));
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
	@RequestMapping("/tranbankmoveshortanswer")
	@ResponseBody
	public Map tranbankmoveshortanswer(Integer q1id,Integer q2id,HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank p = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据		
		Bank_shortanswer bank_shortanswer1 = new Bank_shortanswer();
		Bank_shortanswer bank_shortanswer2 = new Bank_shortanswer();
		Bank_shortanswer bank_temp = new Bank_shortanswer();
		bank_shortanswer1.setqId(q1id);
		bank_shortanswer2.setqId(q2id);
		//交换信息
		bank_temp = bank_shortanswer_Service.selectBank_shortanswer(bank_shortanswer1);
		bank_shortanswer2 = bank_shortanswer_Service.selectBank_shortanswer(bank_shortanswer2);
		bank_shortanswer1 = bank_shortanswer2;
		bank_shortanswer1.setqId(q1id);
		bank_shortanswer2 = bank_temp;
		bank_shortanswer2.setqId(q2id);
		bank_shortanswer_Service.updateBank_shortanswer(bank_shortanswer1);
		bank_shortanswer_Service.updateBank_shortanswer(bank_shortanswer2);
		
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
	@RequestMapping("/tranbanksinglekown")
	@ResponseBody
	public Map tranbanksinglekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Bank bank = new Bank();
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_single> bank_singles = banksingle_Service.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_singles.size();j++) {
				if(bank_singles.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
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
	@RequestMapping("/tranbankmultiplekown")
	@ResponseBody
	public Map tranbankmultiplekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Bank bank = new Bank();
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_multiple> bank_multiples = bank_multipleService.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_multiples.size();j++) {
				if(bank_multiples.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
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
	@RequestMapping("/tranbankmovejudgekown")
	@ResponseBody
	public Map tranbankjudgekown(HttpServletRequest req, HttpServletResponse res) throws IOException {
//		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Bank bank = new Bank();
		bank.setBankid(1);
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_judge> bank_judges = bank_judge_Service.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_judges.size();j++) {
				if(bank_judges.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
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
	@RequestMapping("/tranbankfillkown")
	@ResponseBody
	public Map tranbankfillkown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Bank bank = new Bank();
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_fill> bank_fills = bank_fillService.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_fills.size();j++) {
				if(bank_fills.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
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
	@RequestMapping("/tranbankshortanswerkown")
	@ResponseBody
	public Map tranbankshortanswerkown(HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据		
		Map<String,Object> map = new LinkedHashMap<String,Object>();
		List<Map<String, Object>> kownledgsinfor= new LinkedList<Map<String,Object>>();
		Bank bank = new Bank();
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_shortanswer> bank_shortanswers = bank_shortanswer_Service.getAllbyBankid(bank);
		Integer kownlegsnum = 0; 
		for(int i=0;i<kownledgs.size();i++) {
			Map<String, Object> maplittle = new LinkedHashMap<String, Object>();
			maplittle.put("name",kownledgs.get(i));
			for(int j=0;j<bank_shortanswers.size();j++) {
				if(bank_shortanswers.get(j).getqKnowledge().equals(kownledgs.get(i))) {
					kownlegsnum++;
				}
			}
			maplittle.put("num",kownlegsnum);
			if(kownlegsnum!=0) {
				kownledgsinfor.add(maplittle);
			}
			kownlegsnum=0;
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
	@RequestMapping("/tranbanksinglekowndelete")
	@ResponseBody
	public Map tranbanksinglekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出单选题数据
		Bank bank = new Bank();
		
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_single> bank_singles = banksingle_Service.getAllbyBankid(bank);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<bank_singles.size();i++) {
				if(bank_singles.get(i).getqKnowledge().equals(knowname)) {
					banksingle_Service.deleteBank_single(bank_singles.get(i));
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
	@RequestMapping("/tranbankmultiplekowndelete")
	@ResponseBody
	public Map tranbankmultiplekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出多选题数据
		Bank bank = new Bank();
		
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_multiple> bank_multiples = bank_multipleService.getAllbyBankid(bank);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<bank_multiples.size();i++) {
				if(bank_multiples.get(i).getqKnowledge().equals(knowname)) {
					bank_multipleService.deleteBank_multiple(bank_multiples.get(i));
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
	@RequestMapping("/tranbankmovejudgekowndelete")
	@ResponseBody
	public Map tranbankjudgekowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出判断题数据
		Bank bank = new Bank();
		
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_judge> bank_judges = bank_judge_Service.getAllbyBankid(bank);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<bank_judges.size();i++) {
				if(bank_judges.get(i).getqKnowledge().equals(knowname)) {
					bank_judge_Service.deleteBank_judge(bank_judges.get(i));
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
	@RequestMapping("/tranbankfillkowndelete")
	@ResponseBody
	public Map tranbankfillkowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		Bank bank = new Bank();
		
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_fill> bank_fills = bank_fillService.getAllbyBankid(bank);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<bank_fills.size();i++) {
				if(bank_fills.get(i).getqKnowledge().equals(knowname)) {
					bank_fillService.deleteBank_fill(bank_fills.get(i));
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
	@RequestMapping("/tranbankshortanswerkowndelete")
	@ResponseBody
	public Map tranbankshortanswerkowndelete(String knowname,HttpServletRequest req, HttpServletResponse res) throws IOException {
		Bank b = (Bank) req.getSession().getAttribute("currentBank");
		//取出简答题数据		
		Bank bank = new Bank();
		bank.setBankid(b.getBankid());
		List<String> kownledgs=bankService.getKnowledge(bank);
		List<Bank_shortanswer> bank_shortanswers = bank_shortanswer_Service.getAllbyBankid(bank);
		Integer []kownlegsnum = new Integer[kownledgs.size()]; 
	
			for(int i=0;i<bank_shortanswers.size();i++) {
				if(bank_shortanswers.get(i).getqKnowledge().equals(knowname)) {
					bank_shortanswer_Service.deleteBank_shortanswer(bank_shortanswers.get(i));
				}
			}
		Map<String, Object> map = new LinkedHashMap<String, Object>();

		map.put("code","0000");
		return map;
	}
}
