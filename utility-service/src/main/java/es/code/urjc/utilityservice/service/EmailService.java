package es.code.urjc.utilityservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String destiny, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(Objects.requireNonNull(destiny));
            helper.setSubject(Objects.requireNonNull(subject));
            helper.setFrom("palomixnoreply@gmail.com");

            String body = "<html>" +
                    "<head>" +
                    "  <style>" +
                    "    .button:hover { background-color: #b20710 !important; }" +
                    "  </style>" +
                    "</head>" +
                    "<body style='margin: 0; padding: 0; background-color: #f4f4f4; font-family: Arial, sans-serif;'>" +
                    "  <table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                    "    <tr>" +
                    "      <td style='padding: 20px 0;'>" +
                    "        <table align='center' border='0' cellpadding='0' cellspacing='0' width='600' style='border-collapse: collapse; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);'>" +
                    "          <tr>" +
                    "            <td align='center' style='padding: 40px 0; background-color: #141414;'>" +
                    "              <h1 style='color: #e50914; margin: 0; font-size: 36px; letter-spacing: 2px;'><img src='cid:logoImage' height='100' width='100'></h1>" +
                    "            </td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 40px 30px;'>" +
                    "              <table border='0' cellpadding='0' cellspacing='0' width='100%'>" +
                    "                <tr>" +
                    "                  <td style='color: #333333; font-size: 16px; line-height: 24px;'>" +
                    "                    " + content +
                    "                  </td>" +
                    "                </tr>" +
                    "              </table>" +
                    "            </td>" +
                    "          </tr>" +
                    "          <tr>" +
                    "            <td style='padding: 30px; background-color: #f8f8f8; color: #888888; font-size: 12px; text-align: center;'>" +
                    "              <p style='margin: 0;'>Este es un mensaje automático generado por Palomix.</p>" +
                    "              <p style='margin: 5px 0 0 0;'>Por favor, no respondas a esta dirección de correo.</p>" +
                    "              <p style='margin: 5px 0 0 0;'>Este correo no se utiliza para nada más que el proyecto.</p>" +
                    "              <p style='margin: 20px 0 0 0;'>&copy; 2026 Grupo 6 - Práctica 2 URJC</p>" +
                    "            </td>" +
                    "          </tr>" +
                    "        </table>" +
                    "      </td>" +
                    "    </tr>" +
                    "  </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(body, true);
            ClassPathResource res = new ClassPathResource("static/images/logo.png");
            helper.addInline("logoImage", res);

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
