package com.parth.email_assistent.Controller;
import com.parth.email_assistent.Model.EmailReq;
import com.parth.email_assistent.Service.EmailGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.util.Elements;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailGenerator emailGenerator;
    @PostMapping("/generateResponse")
//    ResponseEntity<String>
    public ResponseEntity<Map<String, String>> generateEmail(@RequestBody EmailReq emailReq)
    {
        String response= emailGenerator.generateEmailResponse(emailReq);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("response", response);
        return ResponseEntity.ok(responseBody);
    }
}
