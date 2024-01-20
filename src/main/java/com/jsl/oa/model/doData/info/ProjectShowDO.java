package com.jsl.oa.model.doData.info;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
public class ProjectShowDO {
    private String order;
    private List<com.jsl.oa.model.doData.info.ProjectShowDO.DataDO> data;

    @Data
    @Accessors(chain = true)
    public static class DataDO {
        private Integer displayOrder;
        private String name;
        private Long type;
        private Long status;
        private Boolean isActive;
        private String createdAt;
        private String updatedAt;
        private String author;
    }
}