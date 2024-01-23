package com.jsl.oa.services.impl;


import com.jsl.oa.mapper.MessageMapper;
import com.jsl.oa.mapper.RoleMapper;
import com.jsl.oa.model.doData.MessageDO;
import com.jsl.oa.model.doData.PageBeanDO;
import com.jsl.oa.services.MessageService;
import com.jsl.oa.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final RoleMapper roleMapper;

    @Override
    public BaseResponse messageDelete(Long mid, HttpServletRequest request) {
        //获取消息数据
        MessageDO messageDO = messageMapper.getMessageById(mid);
        //检测用户id与消息的uid是否相同
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        if (!(messageDO.getUid().equals(JwtUtil.getUserId(token)))) {
            return ResultUtil.error(ErrorCode.MESSAGE_ONLY_DELETE_BY_THEMSELVES);
        }
        //执行删除
        if (!messageMapper.deleteMessage(mid)) {
            return ResultUtil.error(ErrorCode.DATABASE_UPDATE_ERROR);
        }
        return ResultUtil.success();
    }

    @Override
    public BaseResponse messageGet(LocalDate begin, LocalDate end, Long page, Long pageSize, Long uid) {
        //1.记录总数据数
        Long count = messageMapper.count(uid);

        //2.获取分页数据列表
        //默认获取时间为最近30天
        if (begin == null) {
            begin = LocalDate.now();
            end = begin.minusDays(30);
        }
        Long start = (page - 1) * pageSize;
        List<MessageDO> empList = messageMapper.page(begin, end, uid, start, pageSize);

        //3.封装PageBean对象
        PageBeanDO<MessageDO> pageBean = new PageBeanDO<>(count, empList);
        return ResultUtil.success(pageBean);
    }

    @Override
    public BaseResponse messageGetAll(HttpServletRequest request, LocalDate begin, LocalDate end, Long page, Long pageSize, Long loginId, Long uid) {
        log.info("请求接口服务层");
        if (!Processing.checkUserIsAdmin(request, roleMapper)) {
            return ResultUtil.error(ErrorCode.NOT_ADMIN);
        } else {
            //1.记录总数据数
            Long count = messageMapper.count(uid);

            //2.获取分页数据列表
            //默认获取时间为最近30天
            if (begin == null) {
                begin = LocalDate.now();
                end = begin.minusDays(30);
            }
            Long start = (page - 1) * pageSize;
            List<MessageDO> messageDOList = messageMapper.page(begin, end, uid, start, pageSize);

            //3.封装PageBean对象
            PageBeanDO<MessageDO> pageBean = new PageBeanDO<>(count, messageDOList);
            return ResultUtil.success(pageBean);
        }
    }


}


