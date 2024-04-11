package com.jsl.oa.controllers;

import com.jsl.oa.services.MessageService;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.JwtUtil;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <h1>消息控制器</h1>
 * <hr/>
 * 消息控制器，包含信息获取接口
 *
 * @author xiangZr-hhh
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 删除消息
     *
     * @param id      消息id
     * @param request 请求对象
     * @return 删除结果
     */
    @DeleteMapping("/message/delete")
    public BaseResponse messageDelete(@RequestParam Long id, HttpServletRequest request) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else {
            return messageService.messageDelete(id, request);
        }
    }

    /**
     * 获取消息列表
     *
     * @param page     页码
     * @param pageSize 每页条数
     * @param request  请求对象
     * @param begin    开始日期
     * @param end      结束日期
     * @return 消息列表
     */
    @GetMapping("/message/get")
    public BaseResponse messageGet(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            HttpServletRequest request) {
        log.info("请求接口[GET]:/message/get");
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long uid = JwtUtil.getUserId(token);
        if (uid == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        } else {
            return messageService.messageGet(begin, end, page, pageSize, uid);
        }
    }
}

