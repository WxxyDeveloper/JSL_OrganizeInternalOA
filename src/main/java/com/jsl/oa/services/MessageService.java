package com.jsl.oa.services;

import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * <h1>消息服务接口</h1>
 * <hr/>
 * 用于消息控制
 *
 * @author 张睿相
 * @version v1.1.0
 * @since v1.1.0
 */
public interface MessageService {

    BaseResponse messageDelete(Long mid, HttpServletRequest request);

    BaseResponse messageGet(
            LocalDateTime begin,
            LocalDateTime end,
            Integer page,
            Integer pageSize,
            Long uid);

    void messageAdd(Integer pId, Integer systemId, Integer moddleId, Long uid, HttpServletRequest request);
    void messageAdd(Integer pId, Integer systemId, Integer moddleId, Long uid, Long isPass, HttpServletRequest request);
    void messageAdd(Integer pId, Integer type, Integer systemId, HttpServletRequest request);
    void messageAdd(Integer pId, Integer systmeId, Integer moddleId, Integer type, HttpServletRequest request);
    void messageAdd(Integer pId, Integer systemId, Integer moddleId);

    void messageRemind();
}
