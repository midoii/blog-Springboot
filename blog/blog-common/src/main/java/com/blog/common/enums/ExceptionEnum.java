package com.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    SYS_LOG_NOT_FOUND(404, "没有找到系统日志"),
    INVALID_STATUS_VALUE(404, "错误的状态码"),
    UPLOAD_ERROR(500, "文件上传失败"),
    BRAND_SAVE_ERROR(500, "服务器新增品牌失败"),
    GOODS_SAVE_ERROR(500, "商品新增失败"),
    CATEGORY_BRAND_SAVE_ERROR(500, "新增品牌分类中间表失败"),
    BRAND_NOT_FOUND(404, "没有找到指定商品"),
    DETAIL_NOT_FOUND(404, "商品详情不存在"),
    GOODS_STOCK_NOT_FOUND(404, "商品详情不存在"),
    SKU_NOT_FOUND(404, "商品SKU不存在"),
    SPEC_GROUP_NOT_FOUND(404, "商品规格组没查到"),
    SPEC_PARAM_NOT_FOUND(404, "商品规格参数不存在"),
    PRICE_CANNOT_BE_NULL(400,"价格不能为空"),
    CATEGORY_NOT_FOUND(404,"商品分类没查到"),
    ;
    private int code;
    private String msg;

}
