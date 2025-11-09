package com.bidy.notification.repository;

import com.bidy.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    // 확인하지 않은 알림을 최신순으로 조회
    List<Notification> findByRecipientMemberIdAndIsReadFalseOrderByCreatedAtDesc(Long memberId);
}
