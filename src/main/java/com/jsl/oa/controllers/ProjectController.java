package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.*;
import com.jsl.oa.model.voData.business.info.ProjectShowVO;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 项目展示获取
     * 项目轮播图
     *
     * @param id
     * @return
     */
    @GetMapping("/project/header/get")
    public BaseResponse projectGetHeader(@RequestParam(required = false) Integer id) {
        log.info("请求接口[GET]: /project/header/get");
        return projectService.getHeader(id);
    }

    /**
     * 游客获取项目
     * @return
     */
    @GetMapping("/project/get/custom")
    public BaseResponse projectGetCustom(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false) List<String> tags,
                                         @RequestParam(required = false) Integer isFinish){
        log.info("请求接口[GET]: /project/all/get");
        return projectService.tget(id,tags,isFinish);
    }


    /**
     * 我负责的界面的获取项目
     *
     * @return
     */
    @GetMapping("/project/get")
    public BaseResponse projectGet(@RequestParam(required = false) Integer listAll,
                                   @RequestParam(required = false) List<String> tags,
                                   @RequestParam(required = false) Integer isFinish,
                                   HttpServletRequest request) {
        log.info("请求接口[GET]: /project/get");
        return projectService.get(listAll,request,tags,isFinish);
    }

    /**
     * 子模块子系统的查询
     *
     * @return
     */
    @GetMapping("/project/work/get")
    public BaseResponse projectWorkGet(@RequestParam(required = false) Integer listAll,
                                   @RequestParam(required = false) List<String> tags,
                                   @RequestParam(required = false) Integer isFinish,
                                   HttpServletRequest request) {
        log.info("请求接口[GET]: /project/work/get");
        return projectService.workget(listAll, request, tags, isFinish);
    }

    /**
     * 单个项目的详细
     * 项目轮播图
     *
     * @param name
     * @return
     */
    @GetMapping("/project/header")
    public BaseResponse projectGetByName(@RequestParam String name) {
        log.info("请求接口[GET]: /project/header");
        return projectService.getByName(name);
    }

    /**
     * 增加项目展示
     * 项目轮播图
     *
     * @param projectShowVO
     * @param request
     * @param bindingResult
     * @return
     */
    @PostMapping("/project/header/add")
    public BaseResponse projectAddHeader(@RequestBody @Validated ProjectShowVO projectShowVO, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/header/add");
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.addHeader(request, projectShowVO);
    }

    /**
     * 编辑展示的项目
     * 项目轮播图
     *
     * @param projectShowVO
     * @param id
     * @param request
     * @param bindingResult
     * @return
     */
    @PutMapping("/project/header/edit")
    public BaseResponse projectEditHeader(@RequestBody @Validated ProjectShowVO projectShowVO, @RequestParam Integer id, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/header/edit");
        // 参数校验
        if (bindingResult.hasErrors()) {
            log.warn("参数校验失败: {}", Processing.getValidatedErrorList(bindingResult));
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        if (id == null) {
            log.warn("参数校验失败: {}", "id不能为空");
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, "id不能为空");
        }
        return projectService.editHeader(request, projectShowVO, id);
    }


    /**
     * 删除项目展示
     * 项目轮播图
     *
     * @param id
     * @param request
     * @return
     */
    @DeleteMapping("/project/header/del")
    public BaseResponse projectDelHeader(@RequestParam Integer id, HttpServletRequest request) {
        log.info("请求接口[Delete]: /project/header/del");
        return projectService.delHeader(id, request);
    }

    /**
     * 项目表进行，项目增加
     *
     * @return
     */
    @PostMapping("/project/add")
    public BaseResponse projectAdd(HttpServletRequest request, @RequestBody @Validated ProjectInfoVO projectAdd, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectAdd(request, projectAdd);
    }


    /**
     * @Description: 项目的修改
     * @Date: 2024/3/10
     * @Param request:
 * @Param projectEdit:
 * @Param bindingResult:
 * @Param projectId:
     **/
    @PutMapping("/project/edit/{projectId}")
    public BaseResponse projectEditById(HttpServletRequest request, @RequestBody @Validated ProjectEditVO projectEdit, @NotNull BindingResult bindingResult, @PathVariable("projectId") Long projectId) {
        log.info("请求接口[PUT]: /project/edit/{projectId}");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(request, projectEdit,projectId);
    }


    /**
     * 子系统子模块的增加
     * @param request
     * @param bindingResult
     * @return
     */
    @PostMapping("/project/work/add")
    public BaseResponse projectWorkAdd(HttpServletRequest request, @RequestBody @Validated ProjectWorkVO projectWorkVO, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/work/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projecWorktAdd(request, projectWorkVO);
    }


    /**
     * 用户获取所分到的项目模块
     *
     * @param uid
     * @return
     */
    @GetMapping("/project/cut/user")
    public BaseResponse projectGetUserInCutting(@RequestParam Long uid) {
        log.info("请求接口[GET]: /project/cut/user");
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectGetUserInCutting(uid);
    }

    /**
     * 给某用户分配项目模块
     *
     * @param uid
     * @param pid
     * @return
     */
    @PostMapping("/project/cut/user/add")
    public BaseResponse projectAddUserForCutting(HttpServletRequest request, @RequestParam Long uid, @RequestParam Long pid) {
        log.info("请求接口[POST]: /project/cut/user/add");
        // 判断是否有参数错误
        if (uid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectAddUserForCutting(request, uid, pid);
    }

    /**
     * 删除某项目记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/project/delete")
    public BaseResponse projectDelete(HttpServletRequest request, @RequestParam Long id) {
        log.info("请求接口[Delete]: /project/delete");
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectDelete(request, id);
    }

    /**
     * @Description: 添加项目模块
     * @Date: 2024/1/21
     * @Param request
     * @Param projectCuttingAddVO
     * @Param bindingResult
     **/
    @PostMapping("/project/cut/add")
    public BaseResponse projectCuttingAdd(HttpServletRequest request, @RequestBody @Validated ProjectCuttingAddVO projectCuttingAddVO, @NotNull BindingResult bindingResult) {
        log.info("请求接口[Post]: /project/cut/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.addProjectCutting(request, projectCuttingAddVO);
    }


    @PutMapping("/project/cut/edit")
    public BaseResponse projectCuttingEdit(HttpServletRequest request, @RequestBody @Validated ProjectCuttingEditVO projectCuttingEditVO, @NotNull BindingResult bindingResult) {
        log.info("请求接口[Put]: /project/cut/edit");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.editProjectCutting(request, projectCuttingEditVO);
    }

    @PutMapping("/project/cut/user/to")
    public BaseResponse projectToOtherUserForCutting(HttpServletRequest request, @RequestParam Long oldUid
            , @RequestParam Long pid,@RequestParam Long newUid) {
        log.info("请求接口[Put]: /project/cut/user/to");
        // 判断是否有参数错误
        if (oldUid == null || pid == null || newUid==null) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }
        return projectService.projectToOtherUserForCutting(request, oldUid, pid , newUid);
    }

}
