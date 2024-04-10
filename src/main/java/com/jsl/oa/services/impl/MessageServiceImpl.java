package com.jsl.oa.services.impl;
import com.jsl.oa.annotations.CheckUserAbleToUse;
import com.jsl.oa.dao.UserDAO;
import com.jsl.oa.mapper.MessageMapper;
import com.jsl.oa.mapper.ProjectMapper;
import com.jsl.oa.model.dodata.MessageDO;
import com.jsl.oa.model.vodata.MessageAddVO;
import com.jsl.oa.model.vodata.MessageGetVO;
import com.jsl.oa.services.MessageService;
import com.jsl.oa.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static java.lang.System.*;

/**
 * <h1>消息服务层实现类</h1>
 * <hr/>
 * 用于消息服务层的实现类
 *
 * @author xiao_lfeng | xiangZr-hhh | 176yunxuan
 * @version v1.1.0
 * @since v1.1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;
    private final UserDAO userDAO;
    private final ProjectMapper projectMapper;

    @Override
    @CheckUserAbleToUse
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

    @SuppressWarnings("checkstyle:Regexp")
    @Override
    @CheckUserAbleToUse
    public BaseResponse messageGet(LocalDateTime beginTime,
                                   LocalDateTime endTime,
                                   Integer page,
                                   Integer pageSize,
                                   Long uid) {
        //默认获取时间为最近30天
        if (beginTime == null) {
            endTime = LocalDateTime.now();
            beginTime = endTime.minusDays(30);
        }
        List<MessageDO> messageDOList = messageMapper.page(beginTime, endTime, uid);
        //封装返回数据
        List<MessageGetVO> messageGetVOList = new ArrayList<>();
        for (MessageDO messageDO : messageDOList) {
            MessageGetVO messageGetVO1 = new MessageGetVO();
            messageGetVO1.setText(messageDO.getText());
            messageGetVO1.setTitle(messageDO.getTitle());
            messageGetVO1.setCreatedAt(messageDO.getCreatedAt());
            if (messageDO.getSid() != null) {
                messageGetVO1.setSenderName(userDAO.getUserById(messageDO.getSid()).getUsername());
            }
            messageGetVOList.add(messageGetVO1);
        }
        //分页返回
        int start = (page - 1) * pageSize;
        int end = start + pageSize;
        List<MessageGetVO> pageData = messageGetVOList.subList(start,
                Math.min(end, messageGetVOList.size()));
        return ResultUtil.success(pageData);
    }

    /**
     * 添加指派消息
     *
     * @param pId    项目id
     * @param systemId 系统id
     * @param moddleId 模块id
     * @param uid   用户id
     * @param request
     */
    @Override
    public void messageAdd(
            Integer pId,
            Integer systemId,
            Integer moddleId,
            Long uid,
            HttpServletRequest request) {
        // 拿到token，获取发送人id
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long sid = JwtUtil.getUserId(token);
        // 获取发送人名字，项目名，子系统名，子模块名
        String senderName = userDAO.getUserById(sid).getUsername();
        String projectName = projectMapper.tgetProjectById(pId).getName();
        String systemName = projectMapper.getWorkById(systemId).getName();
        // 添加消息
        MessageAddVO messageAddVO = new MessageAddVO();
        messageAddVO.setSid(sid);
        messageAddVO.setUid(uid);
        messageAddVO.setTitle("指派消息");
        if (moddleId == null) {
            messageAddVO.setText(senderName + "指派了" + projectName + "项目的" + systemName + "子系统给您");
        } else {
            String moddleName = projectMapper.getWorkById(moddleId).getName();
            messageAddVO.setText(senderName + "指派了" + projectName + "项目的" + systemName + "子系统的" + moddleName + "子模块给您");
        }
        messageAddVO.setType("跳转审批页");
        messageMapper.messageAdd(messageAddVO);
    }

    /**
     * 添加审批消息
     *
     * @param pId     项目id
     * @param systemId 系统id
     * @param moddleId 模块id
     * @param uid     用户id
     * @param isPass  是否通过 1:通过 0:未通过
     * @param request
     */
    @Override
    public void messageAdd(
            Integer pId,
            Integer systemId,
            Integer moddleId,
            Long uid,
            Long isPass,
            HttpServletRequest request) {
        // 获取发送人名字，项目名，子系统名，子模块名
        String projectName = projectMapper.tgetProjectById(pId).getName();
        String systemName = projectMapper.getWorkById(systemId).getName();
        // 添加消息
        MessageAddVO messageAddVO = new MessageAddVO();
        messageAddVO.setUid(uid);
        messageAddVO.setTitle("审批消息");
        String moddleName = projectMapper.getWorkById(moddleId).getName();
        if (isPass == 1) {
            messageAddVO.setText("您申请的" + projectName + "项目的" + systemName + "系统的" + moddleName + "模块负责人已通过");
        } else {
            messageAddVO.setText("您申请的" + projectName + "项目的" + systemName + "系统的" + moddleName + "模块负责人未通过");
        }
        messageAddVO.setType("消息详情");
        messageMapper.messageAdd(messageAddVO);
    }

    /**
     * 添加项目变动消息
     * @param pId 项目id
     * @param type 类型 1:上传文档 2:修改状态 3:修改负责人
     * @param systemId 系统id
     * @param request
     */
    @Override
    public void messageAdd(
            Integer pId,
            Integer type,
            Integer systemId,
            HttpServletRequest request) {
        // 获取项目名,负责人名
        String projectName = projectMapper.tgetProjectById(pId).getName();
        String senderName = userDAO.getUserById(Processing.getAuthHeaderToUserId(request)).getUsername();
        String systemName = projectMapper.getWorkById(systemId).getName();
        // 添加消息
        // 1:上传文档 2:修改状态 3:修改负责人
        List<Long> uidList = projectMapper.getMemberByProjectId(pId);
        for (Long uid : uidList) {
            MessageAddVO messageAddVO = new MessageAddVO();
            messageAddVO.setUid(uid);
            messageAddVO.setTitle("项目变动消息");
            if (type == 3) {
                messageAddVO.setText("项目负责人" + senderName + "调整了" + systemName + "子系统的负责人");
            } else if (type == 2) {
                messageAddVO.setText("项目负责人" + senderName + "修改了" + projectName + "项目的状态");
            } else if (type == 1) {
                messageAddVO.setText("项目负责人" + senderName + "上传了文档到" + projectName + "项目");
            }
            messageAddVO.setType("跳转项目页");
            messageAddVO.setToId(pId);
            messageMapper.messageAdd(messageAddVO);
        }
    }

    /**
     * 添加子系统变动消息
     *
     * @param pId     项目id
     * @param systmeId 系统id
     * @param moddleId 模块id
     * @param type   类型 1:删除模块 2:修改简介 3:修改周期
     * @param request
     */
    @Override
    public void messageAdd(
            Integer pId,
            Integer systmeId,
            Integer moddleId,
            Integer type,
            HttpServletRequest request) {
        // 获取项目名,负责人名
        String projectName = projectMapper.tgetProjectById(pId).getName();
        String senderName = userDAO.getUserById(Processing.getAuthHeaderToUserId(request)).getUsername();
        String systemName = projectMapper.getWorkById(systmeId).getName();
        String moddleName = projectMapper.getWorkById(moddleId).getName();
        // 添加消息
        // 1:删除模块 2:修改简介 3:修改周期
        if (type == 1) {
            MessageAddVO messageAddVO = new MessageAddVO();
            messageAddVO.setUid(projectMapper.getPid(moddleId));
            messageAddVO.setTitle("子系统变动消息");
            messageAddVO.setText("项目经理" + senderName + "删除了" + projectName + "项目的"
                    + systemName + "系统的" + moddleName + "模块");
            messageMapper.messageAdd(messageAddVO);
        } else {
            List<Long> uidList = projectMapper.getMemberBySystemId(systmeId);
            for (Long uid : uidList) {
                MessageAddVO messageAddVO = new MessageAddVO();
                messageAddVO.setUid(uid);
                messageAddVO.setTitle("子系统变动消息");
                if (type == 2) {
                    messageAddVO.setText("项目经理" + senderName + "修改了" + projectName + "项目的"
                            + systemName + "系统的简介说明");
                } else if (type == 3) {
                messageAddVO.setText("项目经理" + senderName + "修改了" + projectName + "项目的"
                        + systemName + "系统的系统周期/工作量");
                }
                messageAddVO.setType("跳转系统页");
                messageAddVO.setToId(systmeId);
                messageMapper.messageAdd(messageAddVO);
            }
        }
    }

    /**
     * 添加日报消息
     *
     * @param pId      项目id
     * @param systemId 系统id
     * @param moddleId 模块id
     */
    @Override
    public void messageAdd(
            Integer pId,
            Integer systemId,
            Integer moddleId) {
        // 获取项目名,系统名,模块名,负责人名
        String projectName = projectMapper.tgetProjectById(pId).getName();
        String systemName = projectMapper.getWorkById(systemId).getName();
        String moddleName = projectMapper.getWorkById(moddleId).getName();
        String principalName = userDAO.getUserById(projectMapper.getPid(moddleId)).getUsername();
        // 添加消息
        MessageAddVO messageAddVO = new MessageAddVO();
        messageAddVO.setUid(projectMapper.getPid(systemId));
        messageAddVO.setTitle("日报消息");
        messageAddVO.setText(projectName + "项目的" + systemName + "系统的" + moddleName + "模块负责人" + principalName
                + "刚刚填写了日报");
        messageAddVO.setType("跳转日报页");
        messageMapper.messageAdd(messageAddVO);
    }

}


