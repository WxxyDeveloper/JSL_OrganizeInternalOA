package com.jsl.oa.model.voData.business.info;

import lombok.Data;

/**
 * <h1>轮播图VO</h1>
 * <hr/>
 * 轮播图VO，用于接收轮播图请求
 *
 * @version v1.1.0
 * @since v1.1.0
 * @author 筱锋xiao_lfeng
 */
@Data
public class CarouselVO {
    private Integer displayOrder;
    private String image;
    private String title;
    private String description;
    private Boolean isActive;
}
