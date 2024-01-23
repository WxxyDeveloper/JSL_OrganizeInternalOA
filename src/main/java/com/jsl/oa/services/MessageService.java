package com.jsl.oa.services;

import com.jsl.oa.utils.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

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

    BaseResponse messageDelete(Long mid,HttpServletRequest request);

    BaseResponse messageGet(LocalDate begin,LocalDate end,Long page,Long pageSize,Long uid);

    BaseResponse messageGetAll(HttpServletRequest request,LocalDate begin, LocalDate end, Long page, Long pageSize, Long loginId, Long uid);
}
