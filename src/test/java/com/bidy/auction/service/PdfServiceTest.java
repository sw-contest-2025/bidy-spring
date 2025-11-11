package com.bidy.auction.service;

import com.bidy.member.domain.Member;
import com.bidy.member.repository.MemberRepository;
import com.bidy.post.domain.Product;
import com.bidy.post.repository.PostProductRepository;
import jakarta.mail.internet.MimeMessage;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

@SpringBootTest
public class PdfServiceTest {

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private MimeMessage mimeMessage;
    private PdfService pdfService;

    private Member testWinner;
    private Product testProduct;
    private final int FINAL_PRICE = 50000;
    private int TEST_PRODUCT_ID;

    @BeforeEach
    public void setUp() {
        // Mokito 초기화
        MockitoAnnotations.openMocks(this);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        PdfService actualService = new PdfService(mailSender);
        pdfService = Mockito.spy(actualService);

        // 테스트 Member
        testWinner = new Member();
        testWinner.setMemberEmail("winner@test.com");
        testWinner.setMemberNickname("winner");
        testWinner.setMemberName("테스터");
        testWinner.setMemberPw("1234");
        testWinner.setMemberRole("MEMBER");
        testWinner.setMemberBirthday(LocalDate.of(2000, 1, 1));

        // 테스트 판매자
        Member testSeller = new Member();
        testSeller.setMemberEmail("seller@test.com");
        testSeller.setMemberNickname("테스트판매자");
        testSeller.setMemberName("판매자이름");
        testSeller.setMemberPw("password123");
        testSeller.setMemberRole("MEMBER");
        testSeller.setMemberBirthday(LocalDate.of(1990, 5, 5));


        // 테스트 Product
        testProduct = new Product();
        testProduct.setPostName("가방");
        testProduct.setCategory("잡화");
        testProduct.setDeliveryMethod("택배");
        testProduct.setMinPrice(10000);
        testProduct.setCurrentPrice(10000);
        testProduct.setUser(testSeller);
        testProduct.setCreatedAt(LocalDateTime.now().minusDays(1));
        testProduct.setDurationDays(2);
        testProduct.setDurationHours(0);
        testProduct.setDurationMinutes(0);
        testProduct.setProductId(1L);

        try {
            Mockito.doReturn("MOCK_PDF_DATA".getBytes()).when(pdfService)
                    .createCompletionCertificaticatePdf(any(Member.class), any(Product.class), anyInt());
        } catch (Exception e) {
            throw new RuntimeException("Error during PDF mock setup", e);
        }
    }

    @Test
    @DisplayName("\"성공: 증명서가 생성되고 JavaMailSender의 send가 호출")
    void should_SendEmailWithPdfAttachment_OnSuccess(){
        // when
        pdfService.generateAndSendCompletionCertificate(testWinner, testProduct, FINAL_PRICE);
        // then
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(eq(mimeMessage));
    }
}
