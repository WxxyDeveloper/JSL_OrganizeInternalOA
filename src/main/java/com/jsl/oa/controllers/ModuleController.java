package com.jsl.oa.controllers;

import com.jsl.oa.services.ModuleService;
import com.jsl.oa.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


/**
 * <h1>模块控制器</h1>
 * <hr/>
 * 模块控制器，包含模块获取接口
 *
 * @since v1.1.0
 * @version v1.1.0
 * @author xiangZr-hhh | xiao_lfeng | 176yunxuan
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    /**
     * 获取子系统
     *
     * @param projectId 项目ID
     * @param request   请求对象
     * @return 子系统列表
     */
    @GetMapping("/module/get")
    public BaseResponse moduleGetByProjectId(@RequestParam Integer projectId, HttpServletRequest request) {

        log.info("[Controller] 执行 moduleGetByProjectId 接口");
        return moduleService.getByProjectId(projectId, request);
    }

    /**
     * 获取子模块
     *
     * @param sysId   系统ID
     * @param request 请求对象
     * @return 子模块列表
     */
    @GetMapping("/module/get/min")
    public BaseResponse moduleGetBySysId(@RequestParam Integer sysId, HttpServletRequest request) {

        log.info("[Controller] 执行 moduleGetBySysId 接口");
        return moduleService.getBySysId(sysId, request);
    }

    /**
     * 删除模块
     *
     * @param id      模块ID
     * @param request 请求对象
     * @return 删除结果
     */
    @DeleteMapping("/module/delete/{id}")
    public BaseResponse moduleDeleteById(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("[Controller] 执行 moduleDeleteById 接口");
        return moduleService.deleteById(request, id);
    }

}
