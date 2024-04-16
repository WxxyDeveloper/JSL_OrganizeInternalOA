package com.jsl.oa.common.constant;


/**
 * <h1>Review常量类</h1>
 * <hr/>
 * 用于存放审核信息的审批状态常量与类别常量
 *
 * @version v1.1.0
 * @since v1.1.0
 * @author zrx_hhh
 */
public class ReviewConstants {

//    审核状态 0：未通过；1：已通过；2：未审批
    public static final short NOT_APPROVED = 0;

    public static final short APPROVED = 1;

    public static final short PENDING = 2;

//    审核类型 0：子系统；1：子模块
    public static final short SUBSYSTEM = 0;

    public static final short SUBMODULE = 1;

}


