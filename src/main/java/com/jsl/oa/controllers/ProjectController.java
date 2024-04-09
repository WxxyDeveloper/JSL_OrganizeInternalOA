package com.jsl.oa.controllers;

import com.jsl.oa.model.voData.ProjectCuttingAddVO;
import com.jsl.oa.model.voData.ProjectCuttingEditVO;
import com.jsl.oa.model.voData.ProjectEditVO;
import com.jsl.oa.model.voData.ProjectWorkVO;
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
        log.info("请求接口[GET]: /project/header/get");
        return projectService.getHeader(id);
    }

    /**
     * @param id 获取项目 id
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get/id")
    public BaseResponse projectGetById(@RequestParam Integer id) {
        return projectService.getById(id);
    }

    /**
     * @param id 获取项目 id
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/getwork/id")
    public BaseResponse projectWorkGetById(@RequestParam Integer id) {
        return projectService.getWorkById(id);
    }

    /**
     * 游客获取项目
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get/custom")
    public BaseResponse projectGetCustom(@RequestParam(required = false) Integer id,
                                         @RequestParam(required = false) List<String> tags,
                                         @RequestParam(required = false) List<Integer> isFinish,
                                         @RequestParam(required = false, defaultValue = "1") Integer page,
                                         @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        log.info("请求接口[GET]: /project/all/get");
        return projectService.tget(id, tags, isFinish, page, pageSize);
    }

    /**
     * 我负责的界面的获取项目
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/get")
    public BaseResponse projectGet(@RequestParam(required = false) Integer listAll,
                                   @RequestParam(required = false) List<String> tags,
                                   @RequestParam(required = false) List<Integer> isFinish,
                                   @RequestParam(required = false, defaultValue = "1") Integer page,
                                   @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                   HttpServletRequest request) {
        log.info("请求接口[GET]: /project/get");
        return projectService.get(listAll, request, tags, isFinish, page, pageSize);
    }

    /**
     * 我管理的查询
     *
     * @return {@link BaseResponse}
     */
    @GetMapping("/project/work/get")
    public BaseResponse projectWorkGet(@RequestParam(required = false) Integer listAll,
                                       @RequestParam(required = false) List<String> tags,
                                       @RequestParam(required = false) List<Integer> isFinish,
                                       @RequestParam(required = false) Integer is,
                                       @RequestParam(required = false, defaultValue = "1") Integer page,
                                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                       HttpServletRequest request) {
        log.info("请求接口[GET]: /project/work/get");
        return projectService.workget(listAll, request, tags, isFinish, is, page, pageSize);
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
        log.info("请求接口[GET]: /project/header");
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
    public BaseResponse projectAddHeader(@RequestBody @Validated ProjectShowVO projectShowVO, HttpServletRequest request, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/header/add");
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
    public BaseResponse projectEditById(@RequestParam Long projectId, HttpServletRequest request, @RequestBody @Validated ProjectEditVO projectEdit, @NotNull BindingResult bindingResult) {
        log.info("请求接口[PUT]: /project/header/edit/{projectId}");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projectEdit(request, projectEdit, projectId);
    }

    /**
     * 子系统子模块的增加
     *
     * @param request       请求
     * @param bindingResult 参数校验
     * @return {@link BaseResponse}
     */
    @PostMapping("/project/work/add")
    public BaseResponse projectWorkAdd(HttpServletRequest request, @RequestBody @Validated ProjectWorkVO projectWorkVO, @NotNull BindingResult bindingResult) {
        log.info("请求接口[POST]: /project/work/add");
        // 判断是否有参数错误
        if (bindingResult.hasErrors()) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR, Processing.getValidatedErrorList(bindingResult));
        }
        return projectService.projecWorktAdd(request, projectWorkVO);
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
     * 用户获取所分到的项目模块
     *
     * @param uid 用户 id
     * @return {@link BaseResponse}
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
     * @param uid 用户 id
     * @param pid 项目 id
     * @return {@link BaseResponse}
     */
    @PostMapping("/project/cut/user/add")
    public BaseResponse projectAddUserForCutting(HttpServletRequest request, @RequestParam Long uid, @RequestParam Long pid) {
        log.info("请求接口[Post]: /project/cut/user/add");
        // 判断是否有参数错误
        if (uid == null || pid == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectAddUserForCutting(request, uid, pid);
    }

    /**
     * 删除某项目记录
     *
     * @param id 用户 id
     * @return {@link BaseResponse}
     */
    @DeleteMapping("/project/delete")
    public BaseResponse projectDelete(HttpServletRequest request, @RequestParam List<Long> id) {
        log.info("请求接口[Delete]: /project/delete");
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        }
        return projectService.projectDelete(request, id);
    }

    /**
     * 项目轮播图
     *
     * @param request             请求
     * @param projectCuttingAddVO 项目轮播图信息
     * @param bindingResult       参数校验
     * @return {@link BaseResponse}
     */
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
            , @RequestParam Long pid, @RequestParam Long newUid) {
        log.info("请求接口[Put]: /project/cut/user/to");
        // 判断是否有参数错误
        if (oldUid == null || pid == null || newUid == null) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }
        return projectService.projectToOtherUserForCutting(request, oldUid, pid, newUid);
    }


    @GetMapping("/project/file")
    public BaseResponse getProjectFile(HttpServletRequest request, @RequestParam Long projectId) {
        log.info("请求接口[Get]: /project/file");
        //判断是否有参数错误
        if (projectId == null) {
            return ResultUtil.error(ErrorCode.REQUEST_BODY_ERROR);
        }
        return projectService.projectFileGet(request, projectId);
    }


}
