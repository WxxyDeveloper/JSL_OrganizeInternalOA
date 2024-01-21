package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.ProjectInfoVO;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 项目展示获取
     * @param id
     * @return
     */
    @GetMapping("/project/header/get")
    public BaseResponse projectGetHeader(@RequestParam(required = false) Integer id) {
        log.info("请求接口[GET]: /project/header/get");
        return projectService.getHeader(id);
    }

    /**
     * 全部项目的信息获取(打开项目页)
     * @return
     */
    @GetMapping("/project/get")
    public BaseResponse projectGet() {
        log.info("请求接口[GET]: /project/get");
        return projectService.get();
    }

    /**
     * 单个项目的详细
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
     * @param projectAdd
     * @param bindingResult
     * @return
     */
    @PostMapping("/project/add")
    public BaseResponse projectAdd(HttpServletRequest request,@RequestBody @Validated ProjectInfoVO projectAdd, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectAdd(request,projectAdd);
    }

    /**
     * 项目表进行，项目的修改
     * @param projectEdit
     * @param bindingResult
     * @return
     */
    @PutMapping("/project/edit")
    public BaseResponse projectEdit(HttpServletRequest request,@RequestBody @Validated ProjectInfoVO projectEdit, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/edit");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(request,projectEdit);
    }

    /**
     * 用户获取所分到的项目模块
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
     * @param uid
     * @param pid
     * @return
     */
    @PostMapping("/project/cut/user/add")
    public BaseResponse projectAddUserForCutting(HttpServletRequest request,@RequestParam Long uid, @RequestParam Long pid) {
        log.info("请求接口[POST]: /project/cut/user/add");
        // 判断是否有参数错误
        if (uid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectAddUserForCutting(request,uid, pid);
    }

    /**
     * 删除某项目记录
     * @param id
     * @return
     */
    @DeleteMapping("/project/delete")
    public BaseResponse projectDelete(HttpServletRequest request,@RequestParam Long id){
        log.info("请求接口[Delete]: /project/delete");
        if(id == null){
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectDelete(request,id);
    }


}
