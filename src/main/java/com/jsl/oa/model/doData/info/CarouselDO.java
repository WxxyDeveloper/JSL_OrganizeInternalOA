package com.jsl.oa.model.doData.info;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <h1>轮播图数据表</h1>
 * <hr/>
 * 内容进入自定义实体类
 *
 * @version v1.1.0
 * @since v1.1.0
 * @author 筱锋xiao_lfeng
 */
@Data
public class CarouselDO {
    private String order;
    private List<DataDO> data;

    @Data
    @Accessors(chain = true)
    public static class DataDO {
        private Integer displayOrder;
        private String image;
        private String title;
        private String description;
        private Boolean isActive;
        private String createdAt;
        private String updatedAt;
        private String author;
    }
}
