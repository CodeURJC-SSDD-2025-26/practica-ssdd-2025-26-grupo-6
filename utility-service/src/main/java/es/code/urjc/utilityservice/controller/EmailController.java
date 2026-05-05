package es.code.urjc.utilityservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.code.urjc.utilityservice.service.EmailService;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest request) {
        emailService.sendMail(request.destiny(), request.subject(), request.content());
        return ResponseEntity.ok().build();
    }

    public record EmailRequest(String destiny, String subject, String content) {}
}
