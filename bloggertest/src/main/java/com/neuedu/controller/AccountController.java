package com.neuedu.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.neuedu.entity.Adimin;
import com.neuedu.entity.Bank;
import com.neuedu.entity.Payrecord;
import com.neuedu.entity.Shop_bank;
import com.neuedu.entity.Shop_paper;
import com.neuedu.entity.User;
import com.neuedu.service.PayrecordService;
import com.neuedu.service.Shop_bankService;
import com.neuedu.service.Shop_paperService;
import com.neuedu.service.UserService;

@Controller
public class AccountController {
	@Autowired
	Shop_bankService shop_bankService; 
	@Autowired
	Shop_paperService shop_paperService;
	@Autowired
	PayrecordService payrecordService;
	@Autowired
	UserService userservice;
	// -----------------------------------------------------用户题库发布记录页--------------------------------------------------
	/**
	 * 跳转到用户账户页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/tomymoneyaccount")
	public String tomymoneyaccount(HttpServletRequest request,Model m) {
		User currentUser = (User) request.getSession().getAttribute("currentUser");
//        User user=new User();
//        user.setUserid(1);
//        User currentUser= userservice.selectUserByPrimaryKey(user);
////		req.setAttribute("tipInfo","用户名或者密码错误");
		m.addAttribute("currentuser", currentUser); 
		return "shopmarket/mymoneyaccount";

	}

	// -----------------------------------------------------用户题库发布记录页--------------------------------------------------

	/**
	 * 跳转到题库发布记录页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/topublishbankrecord")
	public String topublishbankrecord(Model m, HttpServletRequest request) {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		User user = new User();
		user.setUserid(currentUser.getUserid());

		List<Shop_bank> bankreslist = shop_bankService.selectByUser(user);

		m.addAttribute("publishbankList", bankreslist);
		return "shopmarket/publishbankrecord";

	}

	/**
	 * 题库下架
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/bankdownpublish")
	public @ResponseBody Map bankdownpublish(Model m,
			@RequestParam(value = "downpublishbankid", required = false, defaultValue = "1") Integer bankid) {

		HashMap<String, Object> map = new HashMap<String, Object>();
//		User user = (User)request.getSession().getAttribute("currentUser");
		Shop_bank shop_bank = new Shop_bank();

		shop_bank.setBankid(bankid);
		shop_bank.setCheckflag(3);// 设置为下架
		// 从商城中下架
		shop_bankService.updateCheckFlag(shop_bank);

		map.put("code", "0000");
		return map;

	}

	/**
	 * 题库查看页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/lookpublishbank")
	public Map lookpublishbank(Model m,
			@RequestParam(value = "downpublishbankid", required = false, defaultValue = "1") Integer bankid) {

		HashMap<String, Object> map = new HashMap<String, Object>();


		map.put("code", "0000");
		return map;

	}

	// -----------------------------------------------------用户试卷发布记录页--------------------------------------------------
	/**
	 * 跳转到试卷发布记录页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/topublishpaperrecord")
	public String topublishpaperrecord(Model m) {
//		User user = (User)request.getSession().getAttribute("currentUser");
		User user = new User();
		user.setUserid(1);

		List<Shop_paper> paperreslist = shop_paperService.selectByUser(user);

		m.addAttribute("publishpaperList", paperreslist);
		return "shopmarket/publishpaperrecord"; 

	}

	/**
	 * 试卷下架
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/paperdownpublish")
	public @ResponseBody Map paperdownpublish(Model m,
			@RequestParam(value = "downpublishpaperid", required = false, defaultValue = "1") Integer paperid) {

		HashMap<String, Object> map = new HashMap<String, Object>();
//		User user = (User)request.getSession().getAttribute("currentUser");
		Shop_paper shop_paper = new Shop_paper();

		shop_paper.setPaperid(paperid);
		shop_paper.setCheckflag(3);// 设置为下架
		// 从商城中下架
		shop_paperService.updateCheckFlag(shop_paper);

		map.put("code", "0000");
		return map;

	}
	
	/**
	 * 试卷查看页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/lookpublishpaper")
	public Map lookpublishpaper(Model m,
			@RequestParam(value = "downpublishbankid", required = false, defaultValue = "1") Integer bankid) {

		HashMap<String, Object> map = new HashMap<String, Object>();


		map.put("code", "0000");
		return map;

	}

	// -----------------------------------------------------购买记录页--------------------------------------------------
	/**
	 * 跳转到购买记录页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/toshopinrecord")
	public String toshopinrecord(HttpServletRequest request,Model m) {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		User user = new User();
		user.setUserid(currentUser.getUserid());

		List<Payrecord> precordreslist = payrecordService.getallBuyRecord(user);

		m.addAttribute("precordreslist", precordreslist);
		return "shopmarket/shopinrecord";

	}

	/**
	 * 对购买商品进行评论
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/paycomment")
	public  void paycomment(Model m,
			HttpServletResponse response,
     	   @RequestParam(value = "payrecordid", required = false, defaultValue = "1") Integer payrecordid,
     	   @RequestParam(value = "commentscore", required = false, defaultValue = "1") Integer commentscore,
     	   @RequestParam(value = "commentstr", required = false) String commentstr) throws IOException {
//		User user = (User)request.getSession().getAttribute("currentUser");
		
		Date commenttime = new Date();
//System.out.print(commentstr);
		Payrecord payrecord = new Payrecord();
		payrecord.setRecordid(payrecordid);
		payrecord.setCommentscore(commentscore);
		payrecord.setC3(commentstr);
		payrecord.setCommenttime(commenttime);
		
		payrecordService.updateScore(payrecord);
        response.setCharacterEncoding("UTF-8");
		response.getWriter().print(0000);
	}
	
	/**
	 * 购买查看页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/payinlook")
	public Map payinlook(Model m,
			@RequestParam(value = "downpublishbankid", required = false, defaultValue = "1") Integer bankid) {

		HashMap<String, Object> map = new HashMap<String, Object>();


		map.put("code", "0000");
		return map;

	}


	// -----------------------------------------------------出售记录页--------------------------------------------------
	/**
	 * 跳转到出售记录页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/toshopoutrecord")
	public String toshopoutrecord(HttpServletRequest request,Model m) {
		User currentUser = (User)request.getSession().getAttribute("currentUser");
		User user = new User();
		user.setUserid(currentUser.getUserid());

		List<Payrecord> sellreslist = payrecordService.getallSolderRecord(user);
		m.addAttribute("sellreslist", sellreslist);
		return "shopmarket/shopoutrecord";

	}
	
	/**
	 * 出售查看页
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/payoutlook")
	public Map payoutlook(Model m,
			@RequestParam(value = "downpublishbankid", required = false, defaultValue = "1") Integer bankid) {

		HashMap<String, Object> map = new HashMap<String, Object>();


		map.put("code", "0000");
		return map;

	}
	
	@RequestMapping("/findcomment")
	public void findcomment(Model m,HttpServletResponse response,
			@RequestParam(value = "payrecordid", required = false) Integer recordid) throws IOException {
//		HashMap<String, Object> map = new HashMap<String, Object>();
		response.setCharacterEncoding("UTF-8");
        Payrecord payrecord=new Payrecord();
        payrecord.setRecordid(recordid);
        Payrecord record=payrecordService.selectByPrimarykey(payrecord);
        String jsonrecord=JSON.toJSONString(record);
        response.getWriter().print(jsonrecord);
//		map.put("record", record);
		
	}
	
	
	
	
}
