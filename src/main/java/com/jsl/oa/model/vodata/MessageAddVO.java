package com.jsl.oa.model.vodata;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageAddVO {
    private String text;
    private String title;
    private Long uid;
    private Long sid;
    private String type;
    private Integer toId;

}
