package com.jsl.oa.services;

/**
 * <h1>邮件服务接口</h1>
 * <hr/>
 * 用于发送邮件
 *
 * @author 筱锋xiao_lfeng
 * @version v1.1.0
 * @since v1.1.0
 */
public interface MailService {
    /**
     * <h2>发送邮件通用模板</h2>
     * <hr/>
     * 更为广泛的内容发送，用于发送普通文本邮件
     *
     * @param sendTo  收件人
     * @param subject 主题
     * @param text    内容
     * @return 是否发送成功
     */
    boolean sendMail(String sendTo, String subject, String text);

    /**
     * <h2>发送邮件通用模板</h2>
     * <hr/>
     * 发送邮件通用模板，用于发送具有模板HTML邮件
     *
     * @param sendTo 收件人
     * @param model  模板
     * @return 是否发送成功
     */
    boolean sendMail(String sendTo, String model);

    /**
     * <h2>邮件登陆模块</h2>
     * <hr/>
     * 用于发送用户登陆邮件
     *
     * @param email 邮箱
     * @param code  验证码
     * @return 是否发送成功
     */
    boolean sendMailAboutUserLogin(String email, Integer code);
}

