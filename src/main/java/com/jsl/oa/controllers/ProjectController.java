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

    @GetMapping("/project/header/get")
    public BaseResponse projectGetHeader(@RequestParam(required = false) Integer id) {
        log.info("请求接口[GET]: /project/header/get");
        return projectService.getHeader(id);
    }

    @GetMapping("/project/get")
    public BaseResponse projectGet() {
        return projectService.get();
    }

    @GetMapping("/project/header")
    public BaseResponse projectGetByName(@RequestParam String name) {
        log.info("请求接口[GET]: /project/header");
        return projectService.getByName(name);
    }

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

    @PutMapping("/project/header/edit")
    public BaseResponse projectEditHeader(@RequestBody @Validated ProjectShowVO projectShowVO, @RequestParam Integer id, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/header/del");
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


    @DeleteMapping("/project/header/del")
    public BaseResponse projectDelHeader(@RequestParam Integer id, HttpServletRequest request) {
        log.info("请求接口[Delete]: /project/header/del");
        return projectService.delHeader(id, request);
    }

    @PostMapping("/project/add")
    public BaseResponse projectAdd(@RequestBody @Validated ProjectInfoVO projectAdd, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/header/del");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectAdd(projectAdd);
    }

    @PutMapping("/project/edit")
    public BaseResponse projectEdit(@RequestBody @Validated ProjectInfoVO projectEdit, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/header/del");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(projectEdit);
    }

    @GetMapping("/project/cut/user")
    public BaseResponse projectGetUserInCutting(@RequestParam Long uid) {
        log.info("请求接口[PUT]: /project/header/del");
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectGetUserInCutting(uid);
    }

    @PostMapping("/project/cut/user/add")
    public BaseResponse projectAddUserForCutting(@RequestParam Long uid, @RequestParam Long pid) {
        log.info("请求接口[PUT]: /project/header/del");
        // 判断是否有参数错误
        if (uid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectAddUserForCutting(uid, pid);
    }

    @DeleteMapping("/project/delete")
    public BaseResponse projectDelete(@RequestParam Long id){
        log.info("请求接口[Delete]: /project/delete");
        if(id == null){
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectDelete(id);
    }


}
