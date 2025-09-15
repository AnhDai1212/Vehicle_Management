package tipone.buone.api.vehiclemanagement.services.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tipone.buone.api.vehiclemanagement.enums.ErrorCode;
import tipone.buone.api.vehiclemanagement.exceptions.AppException;
import tipone.buone.api.vehiclemanagement.services.EmailService;
import tipone.buone.api.vehiclemanagement.services.OtpService;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final OtpService otpService;
    private final SpringTemplateEngine springTemplateEngine;
    @Value("${USERNAME_GOOGLE}")
    private String fromEmail;
    public static final String TITLE_SUBJECT_EMAIL = "Verification code notification";

    @Override
    public boolean sendOtpByEmail(String email) {
        String otp = otpService.generateOtp(email);
        try{
            Context context = new Context();
            context.setVariable("otp", otp);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true,"UTF-8");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(TITLE_SUBJECT_EMAIL);

            String htmlContent = springTemplateEngine.process("EmailTemplate", context);
            mimeMessageHelper.setText(htmlContent, true);
            javaMailSender.send(message );
            return true;
        } catch (MessagingException e) {
            throw new AppException(ErrorCode.MAIL_SEND_FAILED);
        }
    }
}