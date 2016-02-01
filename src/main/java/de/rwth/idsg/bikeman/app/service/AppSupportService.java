package de.rwth.idsg.bikeman.app.service;

import de.rwth.idsg.bikeman.app.dto.CreatePedelecSupportDTO;
import de.rwth.idsg.bikeman.app.dto.CreateStationSupportDTO;
import de.rwth.idsg.bikeman.app.exception.AppErrorCode;
import de.rwth.idsg.bikeman.app.exception.AppException;
import de.rwth.idsg.bikeman.domain.Customer;
import de.rwth.idsg.bikeman.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
@Slf4j
public class AppSupportService {

    private String supportAddress;

    @Autowired
    private Environment env;

    @Autowired
    MailService mailService;


    @PostConstruct
    public void init() {
        this.supportAddress = env.getProperty("spring.mail.supportAddress");
    }


    public void sendFeedbackMail(Customer customer, String customerSubject, String customerContent) {
        String subject;
        String content;

        subject = "Feedback from " + customer.getFirstname() + " " + customer.getLastname();
        content = "CustomerID: " + customer.getCustomerId() + "\n" +
                "Firstname: " + customer.getFirstname() + "\n" +
                "Lastname: " + customer.getLastname() + "\n" +
                "E-Mail: " + customer.getLogin() + "\n" +
                "\n" +
                "Subject: " + customerSubject + "\n" +
                "Content: \n" +
                "----------------------------------------\n" +
                customerContent + "\n" +
                "----------------------------------------\n";

        try {
            mailService.sendEmail(this.supportAddress, subject, content, false, false);
        } catch (Exception e) {
            throw new AppException("Contacting support failed: exception is: {}" + e.getMessage(), AppErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }

    public void sendStationSupportMail(Customer customer, Long stationId,
                                       CreateStationSupportDTO.StationErrorCode error, Optional<String> comment) {
        String subject;
        String content;

        // TODO: check stationId

        subject = "Support needed on Station " + stationId.toString();

        content = "CustomerID: " + customer.getCustomerId() + "\n" +
                "Firstname: " + customer.getFirstname() + "\n" +
                "Lastname: " + customer.getLastname() + "\n" +
                "E-Mail: " + customer.getLogin() + "\n" +
                "\n" +
                "Error Code: " + error.toString() + "\n";

        if (comment.isPresent()) {
            content = content + "Comment: " + "\n" +
                    "----------------------------------------\n" +
                    comment.get() + "\n" +
                    "----------------------------------------\n";
        }

        try {
            mailService.sendEmail(this.supportAddress, subject, content, false, false);
        } catch (Exception e) {
            throw new AppException("Contacting support failed: exception is: {}" + e.getMessage(), AppErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }


    public void sendPedelecSupportMail(Customer customer, Long pedelecId,
                                       CreatePedelecSupportDTO.PedelecErrorCode error, Optional<String> comment) {
        String subject;
        String content;

        // TODO: check pedelecId

        subject = "Support needed on Pedelec " + pedelecId.toString();

        content = "CustomerID: " + customer.getCustomerId() + "\n" +
                "Firstname: " + customer.getFirstname() + "\n" +
                "Lastname: " + customer.getLastname() + "\n" +
                "E-Mail: " + customer.getLogin() + "\n" +
                "\n" +
                "Error Code: " + error.toString() + "\n";

        if (comment.isPresent()) {
            content = content +
                    "----------------------------------------\n" +
                    comment.get() + "\n" +
                    "----------------------------------------\n";
        }

        try {
            mailService.sendEmail(this.supportAddress, subject, content, false, false);
        } catch (Exception e) {
            throw new AppException("Contacting support failed: exception is: {}" + e.getMessage(), AppErrorCode.UNKNOWN_SERVER_ERROR);
        }
    }
}
