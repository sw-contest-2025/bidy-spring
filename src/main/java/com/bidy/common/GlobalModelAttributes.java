package com.bidy.common;

import com.bidy.member.domain.Member;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

	@ModelAttribute("loggedIn")
	public boolean addLoggedIn(HttpSession session) {
		Object loginMember = (session != null) ? session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
		return loginMember != null;
	}

	@ModelAttribute("loginMember")
	public Member addLoginMember(HttpSession session) {
		return (session != null) ? (Member) session.getAttribute(SessionConst.LOGIN_MEMBER) : null;
	}
}

