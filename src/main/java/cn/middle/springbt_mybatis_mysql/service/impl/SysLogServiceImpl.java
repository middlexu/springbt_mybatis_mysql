package cn.middle.springbt_mybatis_mysql.service.impl;

import cn.middle.springbt_mybatis_mysql.entity.SysLogBO;
import cn.middle.springbt_mybatis_mysql.service.SysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author 15510
 * @create 2019-06-20 22:28
 */
@Service
@Slf4j//如果不想每次都写private  final Logger logger = LoggerFactory.getLogger(XXX.class); 可以用注解@Slf4j
public class SysLogServiceImpl implements SysLogService {
    @Override
    public boolean save(SysLogBO sysLogBO) {
        // 这里就不做具体实现了
        log.info("这里是注解@SysLog打印出来的日志" + sysLogBO.getParams());
        return true;
    }
}
