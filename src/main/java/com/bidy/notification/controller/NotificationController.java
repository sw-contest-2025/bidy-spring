package com.bidy.notification.controller;

import com.bidy.notification.domain.Notification;
import com.bidy.notification.repository.NotificationRepository;
import com.bidy.notification.service.NotificationService;
import com.bidy.member.domain.Member;
import com.bidy.session.SessionConst;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationService notificationService,
                                  NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/notification")
    public String getNotifications(HttpSession session, Model model) {

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) { //로그인 정보 없을 경우
            return "redirect:/login";
        }

        List<Notification> notifications = notificationService.getUnreadNotifications(loginMember.getMemberId());
        model.addAttribute("notifications", notifications);

        return "notification"; // templates/notification.html
    }
}
