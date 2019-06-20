package cn.middle.springbt_mybatis_mysql.entity;

import lombok.Data;

/**
 * 日志信息实体类
 * @author 15510
 * @create 2019-06-20 22:23
 */

@Data
public class SysLogBO {

    private String className;

    private String methodName;

    private String params;

    private Long exeuTime;

    private String remark;

    private String createDate;

}
