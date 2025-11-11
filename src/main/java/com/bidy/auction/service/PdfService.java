package com.bidy.auction.service;

import com.bidy.member.domain.Member;
import com.bidy.post.domain.Product;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class PdfService {
    private final JavaMailSender javaMailSender;

    public PdfService(JavaMailSender javaMailSender) {
        this.javaMailSender = Objects.requireNonNull(javaMailSender, "JavaMailSender must not be null");
    }

    @Async
    public void generateAndSendCompletionCertificate(Member winner, Product product, int finalPrice) {
        if (winner == null || product == null) {
            throw new IllegalArgumentException("Winner or product must not be null");
        }

        byte[] pdfBytes = createCompletionCertificaticatePdf(winner, product, finalPrice);
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
        String line1 = sanitize("Auction Completion Certificate");
        String line2 = buildPdfLine("Winner: ", winner.getMemberNickname(), winner.getMemberEmail());
        String line3 = buildPdfLine("Item: ", product.getPostName(), "Product ID: " + product.getProductId());
        String line4 = sanitize("Final Price: " + finalPrice + " KRW");

        String contentStream = buildContentStream(line1, line2, line3, line4);
        int streamLength = contentStream.getBytes(StandardCharsets.US_ASCII).length;

        String obj1 = "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n";
        String obj2 = "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n";
        String obj3 = "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\nendobj\n";
        String obj4 = "4 0 obj\n<< /Length " + streamLength + " >>\nstream\n" + contentStream + "endstream\nendobj\n";
        String obj5 = "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n";

        StringBuilder pdf = new StringBuilder();
        pdf.append("%PDF-1.4\n");

        int offset1 = pdf.length();
        pdf.append(obj1);
        int offset2 = pdf.length();
        pdf.append(obj2);
        int offset3 = pdf.length();
        pdf.append(obj3);
        int offset4 = pdf.length();
        pdf.append(obj4);
        int offset5 = pdf.length();
        pdf.append(obj5);
        int xrefStart = pdf.length();

        String xref = String.format(
                "xref\n0 6\n%010d 65535 f \n%010d 00000 n \n%010d 00000 n \n%010d 00000 n \n%010d 00000 n \n%010d 00000 n \n",
                0, offset1, offset2, offset3, offset4, offset5
        );
        pdf.append(xref);
        pdf.append("trailer\n<< /Size 6 /Root 1 0 R >>\n");
        pdf.append("startxref\n");
        pdf.append(xrefStart);
        pdf.append("\n%%EOF");

        return pdf.toString().getBytes(StandardCharsets.US_ASCII);
    }

    private String buildContentStream(String... lines) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n");
        sb.append("/F1 18 Tf\n");
        sb.append("18 TL\n");
        sb.append("1 0 0 1 50 780 Tm\n");
        for (String line : lines) {
            sb.append('(').append(escape(line)).append(") Tj\n");
            sb.append("T*\n");
        }
        sb.append("ET\n");
        return sb.toString();
    }

    private String sanitize(String text) {
        if (text == null) {
            return "";
        }
        return text.replaceAll("[^\\p{ASCII}]", "?");
    }

    private String buildPdfLine(String prefix, String primaryValue, String fallbackValue) {
        String sanitizedPrefix = sanitize(prefix);
        String sanitizedPrimary = sanitize(primaryValue);
        if (sanitizedPrimary.contains("?") && fallbackValue != null) {
            String sanitizedFallback = sanitize(fallbackValue);
            if (!sanitizedFallback.isBlank()) {
                sanitizedPrimary = sanitizedFallback;
            }
        }
        return sanitizedPrefix + sanitizedPrimary;
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\")
                   .replace("(", "\\(")
                   .replace(")", "\\)");
    }

    private void sendEmailWithAttachment(String to, String subject, String text, byte[] attachment, String attachmentName) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("bidy.service@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment, MediaType.APPLICATION_PDF_VALUE);
            helper.addAttachment(attachmentName, dataSource);

            javaMailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 중 오류 발생", e);
        }
    }
}
