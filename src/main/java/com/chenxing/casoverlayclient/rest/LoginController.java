package com.chenxing.casoverlayclient.rest;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class LoginController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	/**
	 * 创建日期:2018年2月3日<br/>
	 * 创建时间:下午5:32:41<br/>
	 * 创建用户:yellowcong<br/>
	 * 机能概要:单点登出
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/loginOut1")
	public String loginOut(HttpSession session) {
		log.info("--controller-loginOut1");
		session.invalidate();
		// http://yellowcong.com:8080/cas-client-maven/user/loginOut/success

		// 这个是直接退出，走的是默认退出方式
		return "redirect:https://test.mycasdomain.com/cas/logout";
	}

	@RequestMapping("/loginOut2")
	public String loginOut2(HttpSession session) {
		log.info("--controller-loginOut2");
		session.invalidate();
		// 退出登录后，跳转到退成成功的页面，不走默认页面
		return "redirect:https://test.mycasdomain.com/cas/logout?service=http://172.16.176.51:8080/loginOut/success";
	}

	@RequestMapping("/loginOut/success")
	@ResponseBody
	public String loginOut2() {
		log.info("--controller-loginOut-sucess");
		return "注销成功";
	}
}
