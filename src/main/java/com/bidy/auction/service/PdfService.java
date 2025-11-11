package com.bidy.auction.service;

import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.layout.Document;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import jakarta.mail.MessagingException;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;

import javax.print.attribute.standard.Media;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
public class PdfService {
    private final JavaMailSender javaMailSender;

    public PdfService(JavaMailSender javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "JavaMailSender must not be null");
    }

    // 낙찰 완료 증명서 생성 및 이메일 발송
    @Async
    public void generateAndSendCompletionCertificate(Member winner, Product product, int finalPrice) {
        if(winner == null || product == null){
            throw new IllegalArgumentException("Winner or product must not be null");
        }

        // 1. PDF 파일 데이터 생성 (바이트 배열)
        byte[] pdfBytes = createCompletionCertificaticatePdf(winner, product, finalPrice);

        // 2. 이메일 발송 정보 설정
        String recipientEmail = winner.getMemberEmail();
        String recipientNickname = winner.getMemberNickname();

        String subject = "[BiDY] " + product.getPostName() + " 상품 낙찰 완료 증명서 발급";
        String body = String.format(
                "안녕하세요, %s님.\n\n" +
                        "축하합니다! 경매에서 %s 상품에 최종 낙찰되셨습니다.\n" +
                        "자세한 내용은 첨부된 '낙찰 완료 증명서.pdf'를 확인해 주십시오.\n",
                recipientNickname, product.getPostName()
        );
        sendEmailWithAttachment(recipientEmail, subject, body, pdfBytes, "낙찰 완료 증명서.pdf");
    }

    public byte[] createCompletionCertificaticatePdf(Member winner, Product product, int finalPrice) {
        // 폰트 리소스 경로는 그대로 유지
        final String FONT_RESOURCE = "/fonts/NanumGothic.ttf";

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             java.io.InputStream fontStream = getClass().getResourceAsStream(FONT_RESOURCE)) {

            if (fontStream == null) {
                throw new RuntimeException("폰트 리소스를 클래스패스에서 찾을 수 없습니다: " + FONT_RESOURCE);
            }

            byte[] fontData = fontStream.readAllBytes();

            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            FontProgram fontProgram = FontProgramFactory.createFont(fontData);
            PdfFont koreanFont = PdfFontFactory.createFont(fontProgram, PdfEncodings.IDENTITY_H);

            document.add(new Paragraph("경매 낙찰 완료 증명서").setFont(koreanFont));
            document.add(new Paragraph("낙찰 상품: " + product.getPostName()).setFont(koreanFont));
            document.add(new Paragraph("최종 낙찰가: " + finalPrice + "원").setFont(koreanFont));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("낙찰 증명서 PDF 생성 중 오류 발생", e);
        }
    }

    // 첨부 파일과 함께 이메일 발송
    private void sendEmailWithAttachment(String to, String subject, String text, byte[] attachment, String attachmentName){
        MimeMessage message = javaMailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("bidy.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            // 첨부 파일(바이트 배열)
            ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, MediaType.APPLICATION_PDF_VALUE);
            helper.addAttachment(attachmentName, dataSource);

            javaMailSender.send(message);

            System.out.println("DEBUG: 이메일 발송 성공 - 수신자: " + to);

        } catch (MessagingException e) {
            System.err.println("경고: 이메일 발송 중 오류 발생. 수신자: " + to + ", 오류: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("경고: 이메일 발송 중 예상치 못한 오류 발생. 수신자: " + to + ", 오류: " + e.getMessage());
        }
    }
}
