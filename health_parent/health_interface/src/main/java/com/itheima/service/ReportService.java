package com.itheima.service;

import java.util.List;
import java.util.Map;

/**
 * 报表接口服务
 * @author wangxin
 * @version 1.0
 */
public interface ReportService {
    /**
     * 会员折线图
     * @param stringList
     * @return
     */
    List<Integer> getMembersByYearMoth(List<String> stringList);

    /**
     * 套餐预约占比饼图
     * @return
     */
    Map getSetmealReport();

    /**
     * 运营数据统计
     * @return
     */
    Map<String,Object> getBusinessReportData() throws Exception;

}
