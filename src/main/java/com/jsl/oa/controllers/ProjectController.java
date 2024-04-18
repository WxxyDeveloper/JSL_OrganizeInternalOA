package com.jsl.oa.controllers;

import com.jsl.oa.annotations.NeedPermission;
import com.jsl.oa.model.vodata.ProjectEditVO;
import com.jsl.oa.model.vodata.ProjectInfoVO;
import com.jsl.oa.model.vodata.ProjectChildAddVO;
import com.jsl.oa.model.vodata.ProjectModuleAddVO;
import com.jsl.oa.model.vodata.business.info.ProjectShowVO;
import com.jsl.oa.services.ProjectService;
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
import java.util.List;


/**
 * 项目控制器
 *
 * @author xiao_lfeng | xiangZr-hhh | 176yunxuan
 * @version 1.1.0
 * @since 1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    /**
     * 项目服务
     */
    private final ProjectService projectService;

    /**
     * 项目展示获取
     * 项目轮播图
     *
     * @param id 获取项目 id
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/header/get")
    public BaseResponse projectGetHeader(@RequestParam(required = false) final Integer id) {
        return projectService.getHeader(id);
    }


    /**
     * 获取子模块详细
     *
     * @param id 要查询的 id
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/module/id")
    public BaseResponse projectModuleGetById(@RequestParam Integer id) {
        return projectService.getModuleById(id);
    }

    /**
     * @param id 要查询项目的 id
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get/id")
    public BaseResponse projectGetById(@RequestParam Integer id) {
        return projectService.getById(id);
    }

    /**
     * 游客获取项目
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get/custom")
    public BaseResponse projectGetCustom(
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> isFinish,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize
    ) {
        return projectService.tGet(tags, isFinish, page, pageSize);
    }

    /**
     * 根据项目 id 获取项目详细信息
     * <hr/>
     * 根据项目 id 获取项目的详细信息，在地址后面有 projectId 的 path 部分需要补充完整（不可缺少）
     *
     * @param projectId 项目 id
     * @param request   请求
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get/{projectId}")
    public BaseResponse getProjectById(
            @PathVariable String projectId,
            HttpServletRequest request
    ) {
        // 对 projectId 进行判断
        if (!projectId.matches("^[0-9]+$")) {
            return ResultUtil.error("参数 projectId 不是一个数字", ErrorCode.PARAMETER_ERROR);
        }
        return projectService.getProjectById(request, Long.parseLong(projectId));
    }

    /**
     * 我管理的页面的项目查询
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/child/get")
    public BaseResponse projectModuleGet(
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) List<String> status,
            @RequestParam(required = false) Integer is,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request
    ) {
        return projectService.workGet(request, tags, status, is, page, pageSize);
    }

    /**
     * 单个项目的详细
     * 项目轮播图
     *
     * @param name 获取项目名称
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/header")
    public BaseResponse projectGetByName(@RequestParam String name) {
        return projectService.getByName(name);
    }

    /**
     * 增加项目展示
     * 项目轮播图
     *
     * @param projectShowVO 项目展示信息
     * @param request       请求
     * @param bindingResult 参数校验
     * @return {@link BaseResponse}
     */
    @PostMapping("/project/header/add")
    public BaseResponse projectAddHeader(
            @RequestBody @Validated ProjectShowVO projectShowVO,
            HttpServletRequest request,
            @NotNull BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.addHeader(request, projectShowVO);
    }

    /**
     * 编辑展示的项目
     * 项目轮播图
     *
     * @param projectId     项目 id
     * @param request       请求
     * @param projectEdit   项目信息
     * @param bindingResult 参数校验
     * @return {@link BaseResponse}
     */
    @PutMapping("/project/header/edit/{projectId}")
    public BaseResponse projectEditById(
            @PathVariable Long projectId,
            @RequestBody @Validated ProjectEditVO projectEdit,
            @NotNull BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(request, projectEdit, projectId);
    }


    /**
     * 获取我负责的项目
     *
     * @param page  页数
     * @param pageSize  每页大小
     * @param request
     * @return
     */
    @GetMapping("/project/my/get")
    public BaseResponse projectMyGet(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        return projectService.getPrincipalProject(page, pageSize, request);
    }

    /**
     *
     * 获取我参与的项目
     *
     * @param page  页数
     * @param pageSize  每页大小
     * @param request
     * @return
     */
    @GetMapping("/project/participate/get")
    public BaseResponse projectParticipateGet(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        return projectService.getParticipateProject(page, pageSize, request);
    }


    /**
     * 增加子模块
     *
     * @param projectModuleAddVO
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/project/module/add")
    @NeedPermission("project:module:add")
    public BaseResponse projectModuleAdd(
            @RequestBody @Validated ProjectModuleAddVO projectModuleAddVO,
            @NotNull BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectModuleAdd(request, projectModuleAddVO);
    }

    /**
     * 子系统的增加
     *
     * @param request       请求
     * @param bindingResult 参数校验
     * @return {@link BaseResponse}
     */
    @PostMapping("/project/child/add")
    @NeedPermission("project:child:add")
    public BaseResponse projectChildAdd(
            HttpServletRequest request,
            @RequestBody @Validated ProjectChildAddVO projectChildAddVO,
            @NotNull BindingResult bindingResult
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectChildAdd(request, projectChildAddVO);
    }


    /**
     * 项目添加
     *
     * @param request       请求
     * @param projectInfoVO 项目信息
     * @param bindingResult 参数校验
     * @return {@link BaseResponse}
     */

    @PostMapping("/project/add")
    @NeedPermission("project:add")
    public BaseResponse projectAdd(
            @RequestBody @Validated ProjectInfoVO projectInfoVO,
            @NotNull BindingResult bindingResult,
            HttpServletRequest request
    ) {
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectAdd(request, projectInfoVO);
    }




    /**
     * 获取负责人id
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/pri")
    public BaseResponse projectPrincipalGet() {
        return projectService.projectPrincipalGet();
    }

    /**
     * 删除某项目记录
     *
     * @param id 用户 id
     * @return {@link BaseResponse}
     */
    @DeleteMapping("/project/delete")
    public BaseResponse projectDelete(HttpServletRequest request, @RequestParam List<Long> id) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectDelete(request, id);
    }

    @GetMapping("/project/file")
    public BaseResponse getProjectFile(HttpServletRequest request, @RequestParam Long projectId) {
        //判断是否有参数错误
        if (projectId == null) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }
        return projectService.projectFileGet(request, projectId);
    }


    /**
     *  删除子系统
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/project/child/delete")
    public BaseResponse projectChildDelete(
            @RequestParam List<Long> id,
            HttpServletRequest request) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectChildDelete(request, id);
    }

    @DeleteMapping("/project/module/delete")
    public BaseResponse projectModuleDelete(
            @RequestParam List<Long> id,
            HttpServletRequest request) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectModuleDelete(request, id);
    }


    @GetMapping("/project/get/name")
    public BaseResponse projectGetName(
            @RequestParam String name,
            HttpServletRequest request
    ) {
        return projectService.projectGetName(name, request);
    }

}
