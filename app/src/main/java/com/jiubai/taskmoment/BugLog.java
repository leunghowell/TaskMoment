package com.jiubai.taskmoment;

/**
 * 记录存在的bug
 */

/**
 * 发现日期：2015-9-23
 * 问题描述：获取验证码和登录时，soap通讯用的五位随机数必须相同，意味着存在一台手机上获取的验证码在别的手机上不能用的问题
 * 解决情况：未解决
 * 解决方案：无
 */

/**
 * 发现日期：2015-9-27
 * 问题描述：公司过多时，多余的会看不到，设置ScrollView又会导致ListView只显示一项
 * 解决情况：已解决
 * 解决方案：ScrollView + ListView只显示一项是因为高度计算出错，因此加载完成后重新计算高度即可
 */

/**
 * 发现日期：2015-10-5
 * 问题描述：Member的列表项操作完需要更新列表，难以转移到adapter实现单击事件，不转移就只能实现删除或查看其中一个功能
 * 解决情况：已解决
 * 解决方案：无
 */

@SuppressWarnings("unused")
public class BugLog {
}
