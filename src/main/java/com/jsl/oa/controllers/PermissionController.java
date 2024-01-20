package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.PermissionEditVo;
import com.jsl.oa.services.PermissionService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.Processing;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping("/permission/add")
    public BaseResponse permissionAdd(HttpServletRequest request, @RequestParam Long rid,@RequestParam Long pid) {
        log.info("请求接口[POST]: /permission/add");
        // 判断是否有参数错误
        if (rid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return permissionService.permissionAdd(request,rid,pid);
        }
    }

    @GetMapping("/permission/current")
    public BaseResponse permissionUser(HttpServletRequest request, @RequestParam Long uid) {
        log.info("请求接口[GET]: /permission/current");
        // 判断是否有参数错误
        if (uid == null ) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else return permissionService.permissionUser(request,uid);
    }

    @GetMapping("/permission/get")
    public BaseResponse permissionGet(HttpServletRequest request){
        log.info("请求接口[GET]: /permission/get");
        return permissionService.permissionGet(request);
    }


    @PutMapping("/permission/edit")
    public BaseResponse permissionEdit(@RequestBody @Validated PermissionEditVo permissionEditVo, BindingResult bindingResult, HttpServletRequest request){
        log.info("请求接口[PUT]: /permission/edit");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return permissionService.permissionEdit(permissionEditVo,request);
    }
}
