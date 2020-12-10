package com.neuedu.controller;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neuedu.Utils.MD5Utils;
import com.neuedu.Utils.PersistenceCookie;
import com.neuedu.entity.User;
import com.neuedu.entity.Usersession;
import com.neuedu.service.UserService;
import com.neuedu.service.UsersessionService;

@Controller
public class LoginController {

	@Autowired
	UserService userService;
	@Autowired
	UsersessionService usersessionService;
	
	/**
	 * 跳转到登录界面
	 * @param m
	 * @return
	 */
	@RequestMapping("/tologin")
	public String toLogin(Model m,HttpServletRequest request) {
		//登录成功
		if(PersistenceCookie.loginAutomatic(request)) {
			User user =(User) request.getSession().getAttribute("currentUser");
			Integer usertype = user.getUsertype();
			//学生
			if(usertype==0) {
				return "studentPaper/studentPaperIndex";
			}
			//老师
			else if(usertype==1) {
				return "redirect:/tobankTeacher";
			}
		}
		return "login";
	}
	
	@RequestMapping("/login")
	@ResponseBody
	public Map login(HttpServletRequest request,String number, String userpassword) {
		HashMap<String,Object> map = new HashMap<String,Object>();
		User u = new User();
		u.setUserpassword(userpassword);
		User selectUser = null;
		String sessionid = null;
		Integer usertype;//用户角色
		//电话登录
		if(number.length()==11) {
			u.setTel(number);
			selectUser = userService.selectUserByTel(u);		
		}
		//账号登录
		else if(number.length()==12) {
			u.setUseraccount(number);
			selectUser = userService.selectUserByAccount(u);	
		}	
		//无此用户
		if(selectUser==null) {
			map.put("code", "1000");
		}else if(MD5Utils.getSaltverifyMD5(userpassword, selectUser.getUserpassword())) {//登录成功
			map.put("code", "0000");
			//用户目前登录类型，把用户登录的沈飞显示的类型页面
			
			usertype = selectUser.getUsertype();
			//学生
			if(usertype==0) {
			map.put("statusPage", "toStudent");
			}
			//老师
			else if(usertype==1) {
			map.put("statusPage", "toTeacher");
			}
			//用户信息session存储
			request.getSession().setAttribute("currentUser", selectUser);
			
			//本地持久化！！！
			//用户sessionid存储实现多点登录
			sessionid = request.getSession().getId();
			//System.out.println(sessionid);
			
			//用户登录以后，根据判断去维护usersession表
			Usersession usession = new Usersession();
			usession.setUserid(selectUser.getUserid());
			Usersession selectsession =usersessionService.selectUsersessionByUserid(usession);
             if(selectsession==null){
            	 selectsession = new Usersession();
            	 selectsession.setUserid(selectUser.getUserid());
            	 selectsession.setSessionid(request.getSession().getId());
            	 int i =usersessionService.insertUsersession(selectsession);
            	
             }else{
            	 selectsession.setSessionid(request.getSession().getId());
//            	 System.out.println(selectsession.getSessionid());
            	 int i=usersessionService.updateUsersession(selectsession);
            	
             }
			
			
		}else {//密码错误
			map.put("code", "1001");
		}
		return map;
	}

}
