package com.jsl.oa.controllers;


import com.jsl.oa.annotations.NeedPermission;
import com.jsl.oa.model.vodata.ProjectDailyAddVO;
import com.jsl.oa.model.vodata.ProjectDailyUpdateVO;
import com.jsl.oa.services.ProjectDailyService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



/**
 * 项目日报(ProjectDaily)表控制层
 *
 * @author zrx
 * @since 2024-04-18 11:40:52
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectDailyController {
    /**
     * 服务对象
     */

    private final ProjectDailyService projectDailyService;

    /**
     * 新增日报
     *
     * @param projectDailyAddVO 日报添加实体
     * @return 新增结果
     */
    @PostMapping("/daily/add")
    @NeedPermission("daily:add")
    public BaseResponse addDaily(@RequestBody @Validated ProjectDailyAddVO projectDailyAddVO,
                            @NotNull BindingResult bindingResult,
                            HttpServletRequest request) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectDailyService.addDaily(projectDailyAddVO, request);
    }

    @PutMapping("/daily/update")
    public BaseResponse updateDaily(@RequestBody @Validated ProjectDailyUpdateVO projectDailyUpdateVO,
                                    @NotNull BindingResult bindingResult,
                                    HttpServletRequest request) {

        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }

        return projectDailyService.updateDaily(projectDailyUpdateVO, request);
    }

    @DeleteMapping("/daily/delete")
    @NeedPermission("project:daily_delete")
    public BaseResponse delete(@RequestParam Integer dailyId,
                               HttpServletRequest request) {

        if (dailyId == null) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }

        return projectDailyService.deleteDaily(dailyId, request);
    }


    @GetMapping("/daily/getMyDaily")
    public BaseResponse getMyDaily(@RequestParam Integer page,
                                   @RequestParam Integer pageSize,
                                   HttpServletRequest request) {

        if (page == null || pageSize == null) {
            return ResultUtil.error(ErrorCode.PAGE_NUMBER_IS_NULL);
        }

        return projectDailyService.getMyDaily(page, pageSize, request);
    }


    @GetMapping("/daily/search")
    public BaseResponse searchMyDaily(@RequestParam Integer page,
                                      @RequestParam Integer pageSize,
                                      Integer projectId,
                                      String beginTime,
                                      String endTime,
                                      HttpServletRequest request) {

        if (page == null || pageSize == null) {
            return ResultUtil.error(ErrorCode.PAGE_NUMBER_IS_NULL);
        }

        if (projectId == null) {
            return ResultUtil.error(ErrorCode.ID_NOT_EXIST);
        }

        return projectDailyService.searchMyDaily(projectId,
                page,
                pageSize,
                Processing.convertStringToDate(beginTime),
                Processing.convertStringToDate(endTime),
                request);
    }


}



