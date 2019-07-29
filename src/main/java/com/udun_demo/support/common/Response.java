package com.udun_demo.support.common;

public class Response {

    public static final ResponseBean REGISTER_SAVE_FAIL = new ResponseBean(1000, "注册保存数据失败");
    public static final ResponseBean USERNAME_EXIST = new ResponseBean(1001, "手机号已经注册");
    public static final ResponseBean GENERATE_ADDRESS_ERROR = new ResponseBean(1002, "获取地址失败");
    public static final ResponseBean GET_SUPPORT_COIN_ERROR = new ResponseBean(1003, "获取支持币种异常");
    public static final ResponseBean CHECK_ADDRESS_ERROR = new ResponseBean(1004, "地址校验失败");
    public static final ResponseBean USERNAME_OR_PASSWORD_ERROR = new ResponseBean(1005, "用户名或者密码错误");
    public static final ResponseBean NO_BALANCE_INFORMATION = new ResponseBean(1006, "没有账户信息");
    public static final ResponseBean SUPPORTED_THIS_COIN = new ResponseBean(1007,"已经支持该币种了");
    public static final ResponseBean INSUFFICIENT_BALANCE = new ResponseBean(1008, "资金不足");
    public static final ResponseBean SAVE_WITHDRAW_AUDIT_ERROR =  new ResponseBean(1009, "保存提币记录出错");
    public static final ResponseBean INIT_COINS_ERROR = new ResponseBean(1010, "初始化币种错误");
    public static final ResponseBean ADD_SUPPORT_COIN_ERROR = new ResponseBean(1011, "保存支持币种出错");
    public static final ResponseBean NO_SUPPORT_COIN = new ResponseBean(1012, "不支持该币种");
    public static final ResponseBean SEND_TRANSACTION_FAIL = new ResponseBean(1013, "发送交易失败");
    public static final ResponseBean NO_BUSINESS_ID_MATCH = new ResponseBean(1014, "没有匹配的业务id号");
    public static final ResponseBean UPDATE_WITHDRAW_AUDIT_ERROR = new ResponseBean(1015, "更新提币记录失败");
    public static final ResponseBean CALLBACK_PARSE_JSON_ERROR = new ResponseBean(1016, "回调解析json异常");
    public static final ResponseBean DEPOSIT_COIN_ERROR = new ResponseBean(1017, "提币出错");
    public static final ResponseBean SAVE_WITHDRAW_RECORD_ERROR = new ResponseBean(1018, "保存提币记录出错");
    public static final ResponseBean SAVE_DEPOSIT_RECORD_ERROR = new ResponseBean(1019, "保存充币记录出错");
    public static final ResponseBean REPEAT_CALLBACK_ERROR = new ResponseBean(1020, "重复推送");
    public static final ResponseBean WITHDRAW_AMOUNT_TOO_LARGE_OR_SMALL = new ResponseBean(1021, "提币数量不在商户配置的提币数量范围内");
    public static final ResponseBean TRANSACTION_PASSWORD_ERROR =new ResponseBean(1022, "交易密码错误") ;
    public static final ResponseBean UPDATE_TRANSACTION_PASSWORD_ERROR = new ResponseBean(1023, "更新交易密码错误");
    public static final ResponseBean ADDRESS_DATA_ERROR = new ResponseBean(1024, "特殊币种处理地址数据异常");

    public static final ResponseBean PARAM_ERROR = new ResponseBean(500, "参数错误");
    public static final ResponseBean USER_BALANCE_STATUS_NULL =  new ResponseBean(1025, "用户账户状态为空");
    public static final ResponseBean CHANGE_USER_BALANCE_STATUS_ERROR = new ResponseBean(1026, "改变用户账户状态出错");
    public static final ResponseBean MIN_DEPOSIT_AMOUNT = new ResponseBean(1027, "充币数量太小，系统不做处理");
    public static final ResponseBean SIGN_CHECK_ERROR = new ResponseBean(1027, "签名校验错误");
    public static final ResponseBean BAD_USERNAME = new ResponseBean(1028, "用户名不存在");
    public static final ResponseBean NEW_PASSWORD_IS_EMPTY = new ResponseBean(1029, "新密码不能为空");
    public static final ResponseBean SAVE_PASSWORD_ERROR = new ResponseBean(1030, "保存密码出错");
    public static final ResponseBean TRANSACTION_PASSWORD_EXIST = new ResponseBean(1031, "交易密码以及设置");
    public static final ResponseBean TRANSACTION_PASSWORD_EMPTY = new ResponseBean(1032, "交易密码为空");
    public static final ResponseBean SEND_WITHDRAW_COIN_ERROR = new ResponseBean(1033, "发送提币错误");
    public static final ResponseBean MUST_ADD_MAIN_COIN = new ResponseBean(1034, "必须先添加主币种");
    public static final ResponseBean PHONE_ERROR = new ResponseBean(1035, "手机号不合法");
    public static final ResponseBean WITHDRAW_AMOUNT_TOO_SMALL = new ResponseBean(1036, "提币数量太小");
    public static final ResponseBean WITHDRAW_AMOUNT_TOO_LARGE = new ResponseBean(1036, "提币数量太大");
    public static final ResponseBean DEPOSIT_AMOUNT_ERROR = new ResponseBean(1036, "充币数量错误");
}
