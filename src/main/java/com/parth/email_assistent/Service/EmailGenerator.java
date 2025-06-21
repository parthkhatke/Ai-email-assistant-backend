package com.parth.email_assistent.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parth.email_assistent.Model.EmailReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class EmailGenerator {

    //web client
    private final WebClient webClient;

    @Value("${API_URL}")
    private  String apiUrl;
    @Value("${API_key}")
    private  String apiKey;

    public EmailGenerator(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    public String generateEmailResponse(EmailReq emailReq)
    {
        String prompt= buildPrompt(emailReq);
        Map<String , Object> requestApi=Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text",prompt)
                })
        });

        //using stream
        String response=webClient.post()
                .uri(apiUrl + apiKey)
                .header("Content-Type","application/json")
                .bodyValue(requestApi)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponseContent(response);
    }


    // to extract reesponse from json body
    private String extractResponseContent(String response) {

        try {

            //this turns json data into java object and vice versa
            ObjectMapper mapper = new ObjectMapper();
            //represnt the json tree and with .readtree we can navigate to all the elements of the json data
            JsonNode rootnode=mapper.readTree(response);

            //use of stream
            return rootnode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        }catch (Exception e)
        {
            return "error processing the request"+e.getMessage();
        }
    }


    private String buildPrompt(EmailReq emailReq) {

        StringBuilder prompt= new StringBuilder();
        prompt.append("generate a professional email reply for the following email  ").append("\n original email : ")
                .append(emailReq.getEmailContent());
        if(emailReq.getTone()!=null && !emailReq.getTone().isEmpty())
        {
            prompt.append("use a ").append(emailReq.getTone()).append(" tone in the reply message");
        }
        prompt.append("\n directly write the reply,do not add a subject line in the reply ," +
                " Make sure the reply is clear and appropriately structured." +
                "If there are many options of replies choose one by yourself and provide me");
        return prompt.toString();
    }
}
