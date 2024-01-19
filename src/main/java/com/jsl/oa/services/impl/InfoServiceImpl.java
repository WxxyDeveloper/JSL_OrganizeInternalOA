package com.jsl.oa.services.impl;

import com.jsl.oa.dao.InfoDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.UserDO;
import com.jsl.oa.model.doData.info.CarouselDO;
import com.jsl.oa.model.voData.business.info.CarouselVO;
import com.jsl.oa.services.InfoService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoServiceImpl implements InfoService {
    private final RoleMapper roleMapper;
    private final InfoDAO infoDAO;
    private final UserDAO userDAO;

    @Override
    public BaseResponse addHeaderImage(HttpServletRequest request, CarouselVO carouselVO) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.getUserById(userId);
        // 获取轮播图信息
        CarouselDO carouselDO = infoDAO.getCarousel();
        // 添加轮播图
        CarouselDO.DataDO carousel = new CarouselDO.DataDO();
        carousel.setDisplayOrder(carouselVO.getDisplayOrder())
                .setImage(carouselVO.getImage())
                .setDescription(carouselVO.getDescription())
                .setTitle(carouselVO.getTitle())
                .setIsActive(carouselVO.getIsActive())
                .setAuthor(userDO.getUsername())
                .setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
        carouselDO.getData().add(carousel);
        // 保存轮播图
        if (infoDAO.setCarousel(carouselDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse editHeaderImage(HttpServletRequest request, CarouselVO carouselVO, Integer id) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);
        UserDO userDO = userDAO.getUserById(userId);
        // 获取轮播图信息
        CarouselDO carouselDO = infoDAO.getCarousel();
        // 获取指定轮播图
        if (id > carouselDO.getData().size()) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }
        CarouselDO.DataDO carousel = carouselDO.getData().get(id - 1);
        carousel.setDisplayOrder(carouselVO.getDisplayOrder())
                .setImage(carouselVO.getImage())
                .setDescription(carouselVO.getDescription())
                .setTitle(carouselVO.getTitle())
                .setIsActive(carouselVO.getIsActive())
                .setAuthor(userDO.getUsername())
                .setUpdatedAt(new Timestamp(System.currentTimeMillis()).toString());
        // 保存轮播图
        if (infoDAO.setCarousel(carouselDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse getHeaderImage(Integer id) {
        CarouselDO carouselDO = infoDAO.getCarousel();
        if (id != null) {
            if (id > carouselDO.getData().size()) {
                return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
            }
            ArrayList<CarouselDO.DataDO> newCarouselDO = new ArrayList<>();
            newCarouselDO.add(carouselDO.getData().get(id - 1));
            carouselDO.setData(newCarouselDO);
        }
        return ResultUtil.success(carouselDO);
    }

    @Override
    public BaseResponse delHeaderImage(HttpServletRequest request, Integer id) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取轮播图信息
        CarouselDO carouselDO = infoDAO.getCarousel();
        // 删除指定轮播图
        if (id > carouselDO.getData().size()) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }
        CarouselDO.DataDO data = carouselDO.getData().remove(id - 1);
        // 保存轮播图
        if (infoDAO.setCarousel(carouselDO)) {
            return ResultUtil.success(data);
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }

    @Override
    public BaseResponse editSettingHeaderImage(HttpServletRequest request, Boolean showType) {
        // 用户权限校验
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        }
        // 获取轮播图信息
        CarouselDO carouselDO = infoDAO.getCarousel();
        carouselDO.setOrder(showType ? "asc" : "desc");
        // 保存轮播图
        if (infoDAO.setCarousel(carouselDO)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
    }
}
