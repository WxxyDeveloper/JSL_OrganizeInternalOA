package com.jsl.oa.services.impl;


import com.jsl.oa.common.constant.ReviewConstants;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ReviewDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectWorkDO;
import com.jsl.oa.model.dodata.ReviewDO;
import com.jsl.oa.model.vodata.ReviewAddVO;
import com.jsl.oa.model.vodata.ReviewUpdateResultVO;
import com.jsl.oa.model.vodata.ReviewVO;
import com.jsl.oa.services.ReviewService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserDAO userDAO;
    private final ReviewDAO reviewDAO;
    private final ProjectDAO projectDAO;

    private final UserMapper userMapper;


    @Override
    public BaseResponse getUserPendingApprovalReview(HttpServletRequest request) {
        log.info("\t> 执行 Service 层 ReviewService.getUserPendingApprovalReview 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewVO> reviewData = new ArrayList<>();

        //先获取用户为项目负责人的项目列表
        projectDAO.getProjectByPrincipalUser(userId);

        //先从用户为 项目负责人 的项目中获取对应 审核信息
        for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewFromProject(projectDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }

        //在从用户为 子系统负责人 的项目中获取对应 审核信息
        for (ProjectWorkDO projectWorkDO : projectDAO.getAllSubsystemByUserId(userId)) {
            //查询每个项目下状态为2的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewsFromSubsystem(projectWorkDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }


        //在从用户为 子模块负责人 的项目中获取对应 审核信息
        for (ProjectWorkDO projectWorkDO : projectDAO.getAllSubmoduleByUserId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewsFromSubsystem(projectWorkDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }


        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewVO>() {
            @Override
            public int compare(ReviewVO review1, ReviewVO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        return ResultUtil.success(reviewData);
    }


    @Override
    public BaseResponse getUserReview(HttpServletRequest request) {
        log.info("\t> 执行 Service 层 ReviewService.getUserReview 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewVO> reviewData = new ArrayList<>();

        //先获取用户为项目负责人的项目列表
        projectDAO.getProjectByPrincipalUser(userId);

        //先从用户为 项目负责人 的项目中获取对应 审核信息
        for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectAllReviewFromProject(projectDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }

        //在从用户为 子系统负责人 的项目中获取对应 审核信息
        for (ProjectWorkDO projectWorkDO : projectDAO.getAllSubsystemByUserId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectReviewFromSubsystem(projectWorkDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }


        //在从用户为 子模块负责人 的项目中获取对应 审核信息
        for (ProjectWorkDO projectWorkDO : projectDAO.getAllSubmoduleByUserId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectReviewFromSubmodule(projectWorkDO.getId());
            //封装VO类
            reviewData.addAll(encapsulateArrayClass(reviewDOS));
        }

        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewVO>() {
            @Override
            public int compare(ReviewVO review1, ReviewVO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        return ResultUtil.success(reviewData);
    }


    @Override
    public BaseResponse addReview(ReviewAddVO reviewAddVO, HttpServletRequest request) {
        log.info("\t> 执行 Service 层 ReviewService.addReview 方法");

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //检查审核事项名称是否重复
        if (!reviewDAO.checkNameIsExist(reviewAddVO.getName())) {
            return ResultUtil.error(ErrorCode.REVIEW_NAME_REPEAT);
        }

        //定义要添加的审核实体类
        ReviewDO reviewDO = new ReviewDO();
        //现将属性相同的值拷贝
        Processing.copyProperties(reviewAddVO, reviewDO);

        //定义审核的类型（子模块id为空则为 子系统类型，否则为子模块类型）
        if (reviewAddVO.getProjectSubmoduleId() == null) {
            reviewDO.setCategory(ReviewConstants.SUBSYSTEM);
        } else if (reviewAddVO.getProjectSubmoduleId() != null) {
            reviewDO.setCategory(ReviewConstants.SUBMODULE);
        }

        //定义申请者id
        reviewDO.setSenderId(userId);
        //添加数据
        reviewDAO.addReview(reviewDO);

        return ResultUtil.success("申请成功");
    }


    @Override
    public BaseResponse updateReviewResult(ReviewUpdateResultVO reviewUpdateResultVO, HttpServletRequest request) {

        //获取当前用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //获取对应审核信息
        ReviewDO reviewDO = reviewDAO.selectReviewById(reviewUpdateResultVO.getId());

        if (reviewDO == null) {
            return ResultUtil.error(ErrorCode.REVIEW_NOT_EXIST);
        }

        //设置对应属性
        reviewDO.setReviewTime(new Date());
        reviewDO.setRecipientId(userId);
        reviewDO.setReviewResult(reviewUpdateResultVO.getResult());

        //更新数据
        reviewDAO.updateReview(reviewDO);

        return ResultUtil.success();
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
//            赋值其他非空属性
            reviewVO.setCategory(Processing.turnReviewCategory(reviewDO.getCategory()))
                    .setSenderName(userMapper.getUserById(reviewDO.getSenderId()).getNickname())
                    .setProjectName(projectDAO.getProjectById(reviewDO.getProjectId()).getName())
                    .setSubsystemName(reviewDAO.getNameBySubproject(reviewDO.getProjectSubsystemId()))
                    .setResult(Processing.turnReviewResult(reviewDO.getReviewResult()));
//            赋值可为空属性并进行判断
            if (reviewDO.getRecipientId() != null) {
                reviewVO.setRecipientName(userMapper.getUserById(reviewDO.getRecipientId()).getNickname());
            }
            if (reviewDO.getProjectSubmoduleId() != null) {
                reviewVO.setSubmoduleName(reviewDAO.getNameBySubproject(reviewDO.getProjectSubmoduleId()));
            } else {
                reviewVO.setSubsystemName("无");
            }
//            将封装好的结果添加到结果集
            resultData.add(reviewVO);
        }

        return resultData;
    }


}


