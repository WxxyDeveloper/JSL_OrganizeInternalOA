package com.jsl.oa.services.impl;


import com.jsl.oa.common.constant.ReviewConstants;
import com.jsl.oa.dao.ProjectDAO;
import com.jsl.oa.dao.ReviewDAO;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.mapper.UserMapper;
import com.jsl.oa.model.dodata.ProjectChildDO;
import com.jsl.oa.model.dodata.ProjectDO;
import com.jsl.oa.model.dodata.ProjectModuleDO;
import com.jsl.oa.model.dodata.ReviewDO;
import com.jsl.oa.model.vodata.ReviewAddVO;
import com.jsl.oa.model.vodata.ReviewDataVO;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final UserDAO userDAO;
    private final ReviewDAO reviewDAO;
    private final ProjectDAO projectDAO;

    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;


    /**
     * @Description: 获取用户未审核的数据(只包括管理项目下未审核的数据)
     * @Date: 2024/4/19
     * @Param page: 当前页码
     * @Param pageSize: 每页大小
     * @Param request: request请求
     **/
    @Override
    public BaseResponse getUserPendingApprovalReview(Integer page,
                                                     Integer pageSize,
                                                     HttpServletRequest request) {
        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);


        //存储审核数据的数组
        List<ReviewDO> reviewData = new ArrayList<>();

        //先获取用户为项目负责人的项目列表
        projectDAO.getProjectByPrincipalUser(userId);

        //先从用户为 项目负责人 的项目中获取对应 审核信息
        for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewFromProject(projectDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(reviewDOS);
        }

        //在从用户为 子系统负责人 的项目中获取对应 审核信息
        for (ProjectChildDO projectChildDO : projectDAO.getAllProjectChildByUId(userId)) {
            //查询每个项目下状态为2的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewsFromSubsystem(projectChildDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(reviewDOS);
        }


        //在从用户为 子模块负责人 的项目中获取对应 审核信息
        for (ProjectModuleDO projectModuleDO : projectDAO.getAllModuleByUId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectApprovedResultReviewsFromSubModule(projectModuleDO.getId(),
                            ReviewConstants.PENDING);
            //封装VO类
            reviewData.addAll(reviewDOS);
        }

        //根据id进行去重
        reviewData = reviewData.stream()
                .collect(Collectors.toMap(ReviewDO::getId, review -> review, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewDO>() {
            @Override
            public int compare(ReviewDO review1, ReviewDO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        //封装对应VO类
        List<ReviewVO> result = encapsulateArrayClass(reviewData);

        //封装结果类与数据总数
        ReviewDataVO reviewDataVO = getReviewsByPage(result, page, pageSize);

        return ResultUtil.success(reviewDataVO);
    }


    /**
     * @Description: 获取我的审核数据(用户管理项目下与自己的记录)
     * @Date: 2024/4/19
     * @Param page: 当前页码
     * @Param pageSize: 每页大小
     * @Param request: request请求
     **/
    @Override
    public BaseResponse getUserReview(Integer page,
                                      Integer pageSize,
                                      HttpServletRequest request) {

        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewDO> reviewData = new ArrayList<>();

        //先获取用户管理下的所有审核信息
        reviewData.addAll(getAllReviewFromProject(userId));

        //获取自己的审核记录
        List<ReviewDO> myReviewDO = reviewDAO.getReviewByUser(userId);
        reviewData.addAll(myReviewDO);

        //根据id进行去重
        reviewData = reviewData.stream()
                .collect(Collectors.toMap(ReviewDO::getId, review -> review, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewDO>() {
            @Override
            public int compare(ReviewDO review1, ReviewDO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        //封装对应VO类
        List<ReviewVO> result = encapsulateArrayClass(reviewData);

        //封装结果类与数据总数
        ReviewDataVO reviewDataVO = getReviewsByPage(result, page, pageSize);

        return ResultUtil.success(reviewDataVO);
    }


    /**
     * @Description: 添加审核接口
     * @Date: 2024/4/19
     * @Param reviewAddVO: 审核添加实体类
     * @Param request: request请求
     **/
    @Override
    public BaseResponse addReview(ReviewAddVO reviewAddVO, HttpServletRequest request) {

        //获取用户
        Integer userId = Math.toIntExact(Processing.getAuthHeaderToUserId(request));

        //检查对应项目，子系统，子模块是否存在
        if (!projectDAO.isExistProjectById(Long.valueOf(reviewAddVO.getProjectId()))) {
            return ResultUtil.error(ErrorCode.PROJECT_NOT_EXIST);
        }

        if (projectMapper.getProjectChildById(
                Math.toIntExact(reviewAddVO.getProjectChildId())) == null) {
            return ResultUtil.error(ErrorCode.PROJECT_CHILD_NOT_EXIST);
        }

//        子模块id不为空时查询，否则直接跳过
        if (reviewAddVO.getProjectModuleId() != null) {
            if (projectMapper.getModuleById(
                    Math.toIntExact(reviewAddVO.getProjectModuleId())) == null) {
                 return ResultUtil.error(ErrorCode.MODULE_NOT_EXIST);
            }
        }

        //定义要添加的审核实体类
        ReviewDO reviewDO = new ReviewDO();
        //现将属性相同的值拷贝
        Processing.copyProperties(reviewAddVO, reviewDO);

        //定义审核的类型（子模块id为空则为 子系统类型，否则为子模块类型）
        if (reviewAddVO.getProjectModuleId() == null) {
            reviewDO.setCategory(ReviewConstants.SUBSYSTEM);
        } else if (reviewAddVO.getProjectModuleId() != null) {
            reviewDO.setCategory(ReviewConstants.SUBMODULE);
        }

        //定义申请者id
        reviewDO.setSenderId(userId);
        reviewDO.setProjectId(Long.valueOf(reviewAddVO.getProjectId()));
        reviewDO.setProjectChildId(Long.valueOf(reviewAddVO.getProjectChildId()));
        reviewDO.setProjectModuleId(Long.valueOf(reviewAddVO.getProjectModuleId()));
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


    @Override
    public BaseResponse searchReview(String content,
                                     HttpServletRequest request,
                                     Integer page, Integer pageSize) {

//        获取我的审核数据
        List<ReviewVO> reviewVOS = getReview(request);

//         根据内容筛选
        if (content == null || content.equals("")) {
            //封装结果类与数据总数
            ReviewDataVO reviewDataVO = getReviewsByPage(reviewVOS, page, pageSize);

            return ResultUtil.success(reviewDataVO);
        } else {
            reviewVOS = reviewVOS.stream()
                    .filter(reviewVO -> reviewVO.getName().contains(content) || reviewVO.getContent().contains(content))
                    .collect(Collectors.toList());
        }


        //封装结果类与数据总数
        ReviewDataVO reviewDataVO = getReviewsByPage(reviewVOS, page, pageSize);

        return ResultUtil.success(reviewDataVO);
    }

    @Override
    public BaseResponse searchReviewRecords(String content,
                                            Short statue,
                                            HttpServletRequest request,
                                            Integer page,
                                            Integer pageSize) {

//        获取审核记录数据
        List<ReviewVO> reviewVOS = getReviewsByResult(request, statue);

//         根据内容筛选
        if (content == null || content.equals("")) {
            //封装结果类与数据总数
            ReviewDataVO reviewDataVO = getReviewsByPage(reviewVOS, page, pageSize);
            return ResultUtil.success(reviewDataVO);
        } else {
            reviewVOS = reviewVOS.stream()
                    .filter(reviewVO -> reviewVO.getName().contains(content) || reviewVO.getContent().contains(content))
                    .collect(Collectors.toList());
        }

        //封装结果类与数据总数
        ReviewDataVO reviewDataVO = getReviewsByPage(reviewVOS, page, pageSize);

        return ResultUtil.success(reviewDataVO);
    }


    private List<ReviewVO> getReview(HttpServletRequest request) {
        //获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewDO> reviewData = new ArrayList<>();

        //获取用户管理的项目下的审核数据
         reviewData.addAll(getAllReviewFromProject(userId));

        //根据id进行去重
        reviewData = reviewData.stream()
                .collect(Collectors.toMap(ReviewDO::getId, review -> review, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewDO>() {
            @Override
            public int compare(ReviewDO review1, ReviewDO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        //封装对应VO类
        List<ReviewVO> result = encapsulateArrayClass(reviewData);

        return result;
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
                    .setSenderName(userMapper.getUserById(Long.valueOf(reviewDO.getSenderId())).getNickname())
                    .setProjectName(projectDAO.getProjectById(reviewDO.getProjectId()).getName())
                    .setProjectChildName(projectMapper.getProjectChildById(
                                    Math.toIntExact(reviewDO.getProjectChildId())).getName())
                    .setResult(Processing.turnReviewResult(reviewDO.getReviewResult()))
                    .setSenderId(Long.valueOf(reviewDO.getSenderId()))
                    .setRecipientId(reviewDO.getRecipientId());
//            赋值可为空属性并进行判断
            if (reviewDO.getRecipientId() != null) {
                reviewVO.setRecipientName(userMapper.getUserById(reviewDO.getRecipientId()).getNickname());
            }
            if (reviewDO.getProjectModuleId() != null) {
                reviewVO.setProjectModuleName(
                        reviewDAO.getNameByModule(Math.toIntExact(reviewDO.getProjectModuleId())));
            } else {
                reviewVO.setProjectModuleName("无");
            }
//            将封装好的结果添加到结果集
            resultData.add(reviewVO);
        }

        return resultData;
    }


    public List<ReviewVO> getReviewsByResult(
                                             HttpServletRequest request,
                                             Short result) {
//     获取用户
        Long userId = Processing.getAuthHeaderToUserId(request);

        //存储审核数据的数组
        List<ReviewDO> reviewData = new ArrayList<>();

        //如果审核结果不为空，则根据审核结果进行审查
        if (result != null) {

            //先从用户为 项目负责人 的项目中获取对应 审核信息
            for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
                //查询每个项目下所有的审核信息
                List<ReviewDO> reviewDOS = reviewDAO.
                        selectApprovedResultReviewFromProject(projectDO.getId(),
                                result);
                //封装VO类
                reviewData.addAll(reviewDOS);
            }

            //在从用户为 子系统负责人 的项目中获取对应 审核信息
            for (ProjectChildDO projectChildDO : projectDAO.getAllProjectChildByUId(userId)) {
                //查询每个项目下状态为2的审核信息
                List<ReviewDO> reviewDOS = reviewDAO.
                        selectApprovedResultReviewsFromSubsystem(projectChildDO.getId(),
                                result);
                //封装VO类
                reviewData.addAll(reviewDOS);
            }


            //在从用户为 子模块负责人 的项目中获取对应 审核信息
            for (ProjectModuleDO projectModuleDO : projectDAO.getAllModuleByUId(userId)) {
                //查询每个项目下所有的审核信息
                List<ReviewDO> reviewDOS = reviewDAO.
                        selectApprovedResultReviewsFromSubModule(projectModuleDO.getId(),
                                result);
                //封装VO类
                reviewData.addAll(reviewDOS);
            }

//        获取自己的审核记录
            List<ReviewDO> myReviewDO = reviewDAO.getReviewByUserAndResult(userId, result);
            reviewData.addAll(myReviewDO);
        }

//        如果审核结果为空，获取全部
        if (result == null) {
            //获取用户管理项目下的所有审核数据
            reviewData.addAll(getAllReviewFromProject(userId));
            //获取用户自己的所有审核数据
            reviewData.addAll(reviewDAO.getReviewByUser(userId));
        }

        //根据id进行去重
        reviewData = reviewData.stream()
                .collect(Collectors.toMap(ReviewDO::getId, review -> review, (existing, replacement) -> existing))
                .values()
                .stream()
                .collect(Collectors.toList());

        //按照申请时间降序排序
        Collections.sort(reviewData, new Comparator<ReviewDO>() {
            @Override
            public int compare(ReviewDO review1, ReviewDO review2) {
                return review2.getApplicationTime().compareTo(review1.getApplicationTime());
            }
        });

        return encapsulateArrayClass(reviewData);
    }


    /**
     * @Description: 审核数据分页处理
     * @Date: 2024/4/19
     * @Param allReviews: 审核数据
     * @Param page: 当前页码
     * @Param pageSize: 每页大小
     **/
    public ReviewDataVO getReviewsByPage(List<ReviewVO> allReviews, int page, int pageSize) {
        ReviewDataVO reviewDataVO = new ReviewDataVO();
        int total = allReviews.size();
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);
        List<ReviewVO> reviewsOnPage = new ArrayList<>();
        if (startIndex <=  allReviews.size()) {
            reviewsOnPage = allReviews.subList(startIndex, endIndex);
        }
        reviewDataVO.setReviews(reviewsOnPage);
        reviewDataVO.setTotalCount(allReviews.size());
        reviewDataVO.setPageSize(pageSize);
        reviewDataVO.setCurrentPage(page);

        return reviewDataVO;
    }



    /**
     * @Description: 获取用户管理项目下的所有审核数据
     * @Date: 2024/4/19
     * @Param userId:
     **/
    public List<ReviewDO> getAllReviewFromProject(Long userId) {

        List<ReviewDO> reviewData = new ArrayList<>();

        //先从用户为 项目负责人 的项目中获取对应 审核信息
        for (ProjectDO projectDO : projectDAO.getProjectByPrincipalUser(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectAllReviewFromProject(projectDO.getId());
            //封装VO类
            reviewData.addAll(reviewDOS);
        }

        //在从用户为 子系统负责人 的项目中获取对应 审核信息
        for (ProjectChildDO projectChildDO : projectDAO.getAllProjectChildByUId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectReviewFromSubsystem(projectChildDO.getId());
            //封装VO类
            reviewData.addAll(reviewDOS);
        }


        //在从用户为 子模块负责人 的项目中获取对应 审核信息
        for (ProjectModuleDO projectModuleDO : projectDAO.getAllModuleByUId(userId)) {
            //查询每个项目下所有的审核信息
            List<ReviewDO> reviewDOS = reviewDAO.
                    selectReviewFromSubmodule(projectModuleDO.getId());
            //封装VO类
            reviewData.addAll(reviewDOS);
        }

        return reviewData;
    }


}


