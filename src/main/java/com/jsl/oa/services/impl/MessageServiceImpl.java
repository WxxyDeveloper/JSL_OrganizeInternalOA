package com.jsl.oa.services.impl;


import com.jsl.oa.mapper.MessageMapper;
import com.jsl.oa.model.doData.MessageDO;
import com.jsl.oa.services.MessageService;
import com.jsl.oa.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;

    @Override
    public BaseResponse messageDelete(Long mid, HttpServletRequest request) {
        //获取消息数据
        MessageDO messageDO = messageMapper.getMessageById(mid);
        //检测用户id与消息的uid是否相同
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (!(messageDO.getUid() == JwtUtil.getUserId(token))) {
            return ResultUtil.error(ErrorCode.MESSAGE_ONLY_DELETE_BY_THEMSELVES);
        }
        //执行删除
        if(!messageMapper.deleteMessage(mid)){
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }


}


