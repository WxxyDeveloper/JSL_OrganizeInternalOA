package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.ProjectInfoVO;
import com.jsl.oa.services.ProjectService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project/add")
    public BaseResponse projectAdd(@RequestBody @Validated ProjectInfoVO projectAdd, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectAdd(projectAdd);
    }

    @PutMapping("/project/edit")
    public BaseResponse projectEdit(@RequestBody @Validated ProjectInfoVO projectEdit, BindingResult bindingResult){
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(projectEdit);
    }

    @GetMapping("/project/cut/user")
    public BaseResponse projectGetUserInCutting(@RequestParam Long uid){
        // 判断是否有参数错误
        if (uid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectGetUserInCutting(uid);
    }

    @PostMapping("/project/cut/user/add")
    public BaseResponse projectAddUserForCutting(@RequestParam Long uid,@RequestParam Long pid){
        // 判断是否有参数错误
        if (uid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectAddUserForCutting(uid,pid);
    }
}
