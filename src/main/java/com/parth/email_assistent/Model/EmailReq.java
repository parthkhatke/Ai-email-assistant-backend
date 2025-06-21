package com.parth.email_assistent.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailReq {
    private String emailContent;
    private String tone;

}
