package com.frost.bfriend.util.certification;

import com.frost.bfriend.exception.user.FailedToSendSmsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.frost.bfriend.constants.SmsConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private final CertificationGenerator certificationGenerator;

    private DefaultMessageService messageService;

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secretkey}")
    private String apiSecretKey;

    @Value("${sms.api.domain}")
    private String domain;

    private void initialize() {
        messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, domain);
    }

    public String sendCertificationSms(String phone) {
        initialize();
        String code = certificationGenerator.createRandomCode();
        Message smsMessage = createSms(phone,CERTIFICATION_CONTENT + code);
        SingleMessageSentResponse smsResponse = messageService.sendOne(new SingleMessageSendingRequest(smsMessage));
        if(!smsResponse.getStatusCode().equals(STATUS_ACCEPTED)) {
            throw new FailedToSendSmsException("SMS 인증코드 전송에 실패했습니다.");
        }

        return code;
    }

    private Message createSms(String to, String content) {
        Message certificationMessage = new Message();
        certificationMessage.setFrom(SMS_FROM);
        certificationMessage.setTo(to);
        certificationMessage.setText(content);

        return certificationMessage;
    }
}
