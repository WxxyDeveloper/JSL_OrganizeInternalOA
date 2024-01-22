package com.jsl.oa.dao;

import com.google.gson.Gson;
import com.jsl.oa.mapper.InfoMapper;
import com.jsl.oa.model.doData.info.CarouselDO;
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
        log.info("\t> 执行 DAO 层 InfoDAO.getCarousel 方法");
        log.info("\t\t> 从 MySQL 获取数据");
        String getCarouselSql = infoMapper.getCarousel();
        CarouselDO getCarousel = null;
        if (getCarouselSql != null && !getCarouselSql.equals("{}")) {
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
        return getCarousel;
    }

    private void sortCarousel(CarouselDO getCarousel) {
        for (int i = 0; i < getCarousel.getData().size(); i++) {
            for (int j = 0; j < getCarousel.getData().size(); j++) {
                if (getCarousel.getOrder().equals("desc")) {
                    if (getCarousel.getData().get(i).getDisplayOrder() > getCarousel.getData().get(j).getDisplayOrder()) {
                        Collections.swap(getCarousel.getData(), i, j);
                    }
                } else {
                    if (getCarousel.getData().get(i).getDisplayOrder() < getCarousel.getData().get(j).getDisplayOrder()) {
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
        log.info("\t> 执行 DAO 层 InfoDAO.setCarousel 方法");
        sortCarousel(carouselDO);
        String setCarouselSql = gson.toJson(carouselDO);
        log.info("\t\t> 从 MySQL 获取数据");
        return infoMapper.setCarousel(setCarouselSql);
    }
}
