package com.jsl.oa.controllers;
import com.jsl.oa.model.vodata.ProjectEditVO;
import com.jsl.oa.model.vodata.ProjectWorkVO;
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
    public BaseResponse projectAddHeader(@RequestBody @Validated ProjectShowVO projectShowVO,
                                         HttpServletRequest request,
                                         @NotNull BindingResult bindingResult) {
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
    public BaseResponse projectEditById(@RequestParam Long projectId,
                                        @RequestBody @Validated ProjectEditVO projectEdit,
                                        @NotNull BindingResult bindingResult,
                                        HttpServletRequest request) {
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
    public BaseResponse projectWorkAdd(HttpServletRequest request,
                                       @RequestBody @Validated ProjectWorkVO projectWorkVO,
                                       @NotNull BindingResult bindingResult) {
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
