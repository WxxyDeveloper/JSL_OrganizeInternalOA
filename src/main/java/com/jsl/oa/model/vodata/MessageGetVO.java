package com.jsl.oa.model.vodata;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import java.sql.Timestamp;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageGetVO {
    private Long id;
    private String text;
    private String title;
    private Timestamp createdAt;
    private String senderName;
    private String type;
    private Long toId;

}
