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
import java.time.LocalDate;

/**
 * <h1>消息控制器</h1>
 * <hr/>
 * 消息控制器，包含信息获取接口
 *
 * @author 张睿相
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @DeleteMapping("/message/delete")
    public BaseResponse messageDelete(@RequestParam Long id, HttpServletRequest request) {
        if (id == null) {
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else return messageService.messageDelete(id, request);
    }

    @GetMapping("/message/get")
    public BaseResponse messageGet(@RequestParam(defaultValue = "1") Long page,
                                   @RequestParam(defaultValue = "10") Long pageSize,
                                   HttpServletRequest request,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("请求接口[GET]:/message/get");
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long uid = JwtUtil.getUserId(token);
        if (uid == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        } else return messageService.messageGet(begin, end, page, pageSize, uid);
    }

    @GetMapping("/message/get/all")
    public BaseResponse messageGetAll(@RequestParam(defaultValue = "1") Long page,
                                      @RequestParam(defaultValue = "10") Long pageSize,
                                      HttpServletRequest request,
                                      @RequestParam Long uid,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("请求接口[GET]:/message/get/all");
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long LoginId = JwtUtil.getUserId(token);
        if (LoginId == null) {
            return ResultUtil.error(ErrorCode.USER_NOT_EXIST);
        } else return messageService.messageGetAll(request, begin, end, page, pageSize, LoginId, uid);
    }

}


