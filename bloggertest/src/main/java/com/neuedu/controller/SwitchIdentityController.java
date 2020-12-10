package com.neuedu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.neuedu.entity.User;
import com.neuedu.service.UserService;


@Controller
public class SwitchIdentityController {
	
	@Autowired
	UserService userService ;

	@RequestMapping("/switchIdentity")
	public String switchIdentity(HttpServletRequest req) {
		//获取当前的用户身份
		 User u=(User)(req.getSession().getAttribute("currentUser"));
//		 System.out.println("currentUser:"+u);
		 Integer usertype=null;
		 if(u!=null) {
			 usertype= u.getUsertype();
		 }
//		 System.out.println("jiaose:"+usertype);
		
		 
		 //0表示学生 1表示老师
		 if(usertype!=null) {
		 if(usertype==0) {
			 u.setUsertype(1);
			 req.setAttribute("currentUser", u);
			 userService.updateUser(u);
			 return "redirect:/tounpublishteacherPaper";
		 }else if(usertype==1) {
			 u.setUsertype(0);
			 req.setAttribute("currentUser", u);
			 userService.updateUser(u);
			 return "redirect:/toStudentPaperIndex";
		 }
		 //更新
		 
		 }
		//在没有角色信息时为默认身份是学生。
		 
			 return "login";
		
		 
		
		 
		
		
		
	}

}
