package com.jsl.oa.controllers;

import com.jsl.oa.services.ModuleService;
import com.jsl.oa.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ModuleContorller {
    private final ModuleService moduleService;

    /**
     * 获取子系统
     * @param projectId
     * @param request
     * @return
     */
    @GetMapping("/module/get")
    public BaseResponse moudleGetByProjectId(@RequestParam Integer projectId, HttpServletRequest request){

        log.info("ProjectIdcontroller");
        return moduleService.getByProjectId(projectId,request);
    }

    /**
     * 获取子模块
     * @param sysId
     * @param request
     * @return
     */
    @GetMapping("/module/get/min")
    public BaseResponse moudleGetBySysId(@RequestParam Integer sysId, HttpServletRequest request){

        log.info("SysIdcontorller");
        return moduleService.getBySysId(sysId,request);
    }



    @DeleteMapping("/module/delete/{id}")
    public BaseResponse moudleDeleteById( @PathVariable("id") Long id, HttpServletRequest request){
        log.info("请求接口[DELETE]: /module/delete/{id}");
        return moduleService.deleteById(request,id);
    }

}
