package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.jsl.oa.mapper.InfoMapper;
import com.jsl.oa.model.dodata.info.CarouselDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

/**
 * <h1>轮播图数据表</h1>
 * <hr/>
 * 内容进入自定义实体类
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InfoDAO {
    private final InfoMapper infoMapper;
    private final Gson gson;

    /**
     * <h2>获取轮播图</h2>
     * <hr/>
     * 获取轮播图
     *
     * @return {@link CarouselDO}
     */
    public CarouselDO getCarousel() {
        String getCarouselSql = infoMapper.getCarousel();
        CarouselDO getCarousel = null;
        if (getCarouselSql != null && !"{}".equals(getCarouselSql)) {
            getCarousel = gson.fromJson(getCarouselSql, CarouselDO.class);
        }
        if (getCarousel == null) {
            // 初始化
            getCarousel = new CarouselDO();
            getCarousel.setOrder("desc");
            getCarousel.setData(new ArrayList<>());
            try {
                infoMapper.insertCarousel();
            } catch (DuplicateKeyException ignored) {
            }
        }
        // 获取排序
        sortCarousel(getCarousel);
        // 添加id
        for (int i = 0; i < getCarousel.getData().size(); i++) {
            getCarousel.getData().get(i).setId(i + 1);
        }
        return getCarousel;
    }

    private void sortCarousel(CarouselDO getCarousel) {
        for (int i = 0; i < getCarousel.getData().size(); i++) {
            for (int j = 0; j < getCarousel.getData().size(); j++) {
                CarouselDO.DataDO dataDO = getCarousel.getData().get(i);
                if ("desc".equals(getCarousel.getOrder())) {
                    if (dataDO.getDisplayOrder() > dataDO.getDisplayOrder()) {
                        Collections.swap(getCarousel.getData(), i, j);
                    }
                } else {
                    if (dataDO.getDisplayOrder() < dataDO.getDisplayOrder()) {
                        Collections.swap(getCarousel.getData(), i, j);
                    }
                }
            }
        }
    }

    /**
     * <h2>设置轮播图</h2>
     * <hr/>
     * 设置轮播图
     *
     * @param carouselDO 轮播图
     * @return {@link Boolean}
     */
    public boolean setCarousel(CarouselDO carouselDO) {
        sortCarousel(carouselDO);
        // 添加id
        for (int i = 0; i < carouselDO.getData().size(); i++) {
            carouselDO.getData().get(i).setId(i + 1);
        }
        String setCarouselSql = gson.toJson(carouselDO);
        return infoMapper.setCarousel(setCarouselSql);
    }
}
