package de.rwth.idsg.bikeman.service;

import de.rwth.idsg.bikeman.domain.User;
import org.apache.commons.lang.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * Service for sending e-mails.
 * <p/>
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Inject
    private Environment env;

    @Inject
    private JavaMailSenderImpl javaMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    /**
     * System default email address that sends the e-mails.
     */
    private String from;
    private String passwordUrl;
    private String activationUrl;

    @PostConstruct
    public void init() {
        this.from = env.getProperty("spring.mail.from");
        this.passwordUrl = env.getProperty("spring.mail.passwordUrl");
        this.activationUrl = env.getProperty("spring.mail.activationUrl");
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws Exception {
        log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent e-mail to User '{}'", to);
        } catch (Exception e) {
            throw new Exception("E-mail could not be sent to user '" + to + "', exception is: " + e.getMessage());
        }
    }

    @Async
    public void sendActivationEmail(User user, String key) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);
            message.setTo(user.getLogin());
            message.setFrom(from);
            message.setSubject("Welcome");
            message.setText("Hello,\n" +
                "please open the following link to activate your user account:\n" +
                "<" + this.activationUrl + ">\n", false);

            javaMailSender.send(mimeMessage);

            log.debug("Sent e-mail to User '{}'", user.getLogin());
        } catch (MessagingException e) {
            log.error("Error creating Mail: ", e);
        } catch (MailSendException e) {
            log.error("Error sending mail: ", e);
        }
    }

    @Async
    public void sendPasswortResetEmail(User user, String key) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, CharEncoding.UTF_8);
            message.setTo(user.getLogin());
            message.setFrom(from);
            message.setSubject("Password reset");
            message.setText("Hello,\n" +
                "you or someone else requested a reset of your password.\n" +
                "To define a new password follow this link:\n" +
                "<" + this.passwordUrl + key + ">\n", false);

            javaMailSender.send(mimeMessage);

            log.debug("Sent e-mail to User '{}'", user.getLogin());
        } catch (MessagingException e) {
            log.error("Error creating Mail: ", e);
        } catch (MailSendException e) {
            log.error("Error sending mail: ", e);
        }
    }

    /*@Async
    public void sendActivationEmail(User user, String baseUrl) {
        log.debug("Sending activation e-mail to '{}'", user.getLogin());
//        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context();
//        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", baseUrl);
        String content = templateEngine.process("activationEmail", context);
//        String subject = messageSource.getMessage("email.activation.title", null, locale);
        String subject = messageSource.getMessage("email.activation.title", null, null);
        try {
            sendEmail(user.getLogin(), subject, content, false, true);
        } catch (Exception e) {
            log.warn("E-mail could not be sent to user '{}', exception is: {}", user.getLogin(), e.getMessage());
        }
    }*/
}
