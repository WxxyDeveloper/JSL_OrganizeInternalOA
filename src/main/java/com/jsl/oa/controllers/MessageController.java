package com.jsl.oa.controllers;

import com.jsl.oa.services.MessageService;
import com.jsl.oa.services.impl.MessageServiceImpl;
import com.jsl.oa.utils.BaseResponse;
import com.jsl.oa.utils.ErrorCode;
import com.jsl.oa.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
    public BaseResponse messageDelete(@RequestParam Long id, HttpServletRequest request){
        if(id == null){
            return ResultUtil.error(ErrorCode.PARAMETER_ERROR);
        } else return messageService.messageDelete(id,request);
    }
}


