package com.jsl.oa.services.impl;


import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ReviewDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ReviewMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.dodata.ReviewDO;
import com.jsl.oa.model.vodata.ReviewVO;
import com.jsl.oa.services.ReviewService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserDAO userDAO;
    private final ReviewDAO reviewDAO;
    private final ProjectDAO projectDAO;

    private final UserMapper userMapper;
    private final ReviewMapper reviewMapper;


    @Override
    public BaseResponse getUserReview(Long projectId, HttpServletRequest request) {
        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewVO> reviewData = new ArrayList<>();

        //先获取用户为项目负责人的项目列表
        projectDAO.getProjectByPrincipalUser(userId);

        //先从用户为 项目负责人 的项目中获取对应 审核信息
        for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewMapper.
                    selectAllReviewFromProject(projectDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }

        //在从用户为 子系统负责人 的项目中获取对应 审核信息
        for (ProjectModuleDO projectWorkDO : projectDAO.getAllSubsystemByUserId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewMapper.
                    selectReviewFromSubsystem(projectWorkDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }


        //在从用户为 子模块负责人 的项目中获取对应 审核信息
        for (ProjectModuleDO projectWorkDO : projectDAO.getAllSubmoduleByUserId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewMapper.
                    selectReviewFromSubmodule(projectWorkDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }

        return ResultUtil.success(reviewData);
    }


    /**
     * @Description: 封装审核的VO类
     * @Date: 2024/4/11
     * @Param reviewDOS:
     **/
    public List<ReviewVO> encapsulateArrayClass(List<ReviewDO> reviewDOS) {

//        定义封装类结果集数组
        List<ReviewVO> resultData = new ArrayList<>();

        for (ReviewDO reviewDO : reviewDOS) {
            ReviewVO reviewVO = new ReviewVO();
//            现将相同的属性赋值
            Processing.copyProperties(reviewDO, reviewVO);
//            赋值其他属性
            reviewVO.setCategory(Processing.turnReviewCategory(reviewDO.getCategory()))
                    .setSenderName(userMapper.getUserById(reviewDO.getSenderId()).getNickname())
                    .setRecipientName(userMapper.getUserById(reviewDO.getRecipientId()).getNickname())
                    .setProjectName(projectDAO.getProjectById(reviewDO.getProjectId()).getName())
                    .setSubsystemName(reviewDAO.getNameBySubproject(reviewDO.getProjectSubsystemId()))
                    .setSubmoduleName(reviewDAO.getNameBySubproject(reviewDO.getProjectSubmoduleId()))
                    .setResult(Processing.turnReviewResult(reviewDO.getReviewResult()));
//            将封装好的结果添加到结果集
            resultData.add(reviewVO);
        }

        return resultData;
    }





}


