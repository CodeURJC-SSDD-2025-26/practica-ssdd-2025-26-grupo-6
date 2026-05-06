package es.code.urjc.palomix.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class EmailClientService {

    @Value("${utility.service.url:http://localhost:8080}")
    private String utilityServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendMail(String destiny, String subject, String content) {
        Map<String, String> request = Map.of(
            "destiny", destiny,
            "subject", subject,
            "content", content
        );
        restTemplate.postForObject(utilityServiceUrl + "/api/v1/email", request, Void.class);
    }
}
