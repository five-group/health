package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.SetmealDao;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 报表业务层处理
 * @author wangxin
 * @version 1.0
 */
@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 会员折线图
     * @param monthList
     * @return
     */
    @Override
    public List<Integer> getMembersByYearMoth(List<String> monthList) {
        //sql:select count(*) from t_member where regTime <= '2019-04-31'
        List<Integer> memberCounts = new ArrayList<>();
        if(monthList != null){
            for (String yearMonth : monthList) {
                //拼接+“-31”
                String newYearMonth = yearMonth+"-31";
                int memberCount = memberDao.findMemberCountBeforeDate(newYearMonth);
                memberCounts.add(memberCount);
            }
        }
        return memberCounts;
    }

    /**
     * 套餐预约占比饼图
     * @return
     */
    @Override
    public Map getSetmealReport() {
        //"setmealNames":["套餐一","套餐二"]
        //"setmealCount":[{value:22,name:'套餐一'},{value:33,name:'套餐二'}]
        //select ts.name,count(*) value from t_setmeal ts,t_order o where ts.id = o.setmeal_id group by ts.name
        //1.获取每个套餐对应的预约数量
        List<Map> mapList = setmealDao.setmealCount();
        //2.定义List<String>存放套餐名称
        List<String> setmealNames = new ArrayList<>();
        //3.获取套餐名称集合
        if(mapList != null && mapList.size()>0){
            for (Map map : mapList) {
                String setmealName = (String)map.get("name");
                setmealNames.add(setmealName);
            }
        }
        //4.定义map最终返回页面需要的数据
        Map<String,Object> map = new HashMap<>();
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",mapList);
        return map;
    }

    /**
     * 运营数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        String reportDate = DateUtils.parseDate2String(DateUtils.getToday());//当前报表的日期
        //获得本周一的日期
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获得本月第一天的日期
        String firstDay4ThisMonth =
                DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        Integer todayNewMember = memberDao.findMemberCountByDate(reportDate);//今天新增会员数
        Integer totalMember = memberDao.findMemberTotalCount();//总会员数

        //本周新增会员数
        Integer thisWeekNewMember = memberDao.findMemberCountAfterDate(thisWeekMonday);

        //本月新增会员数
        Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderDao.findOrderCountByDate(reportDate);

        //本周预约数
        Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
        //1.定义Map<String, Object>返回结果对象
        Map<String,Object> rsMap = new HashMap<>();
        List<Map> hotSetmeal = setmealDao.hotSetmeal();//热门套餐列表
        //套餐名称（t_setmeal）、预约数量(t_order)、占比(当前套餐预约数量/总的套餐预约数量)、备注（t_setmeal）
        //select ts.name,count(ts.id) setmeal_count,count(ts.id)/(select count(*) from t_setmeal) proportion,ts.remark
        // from t_setmeal ts,t_order o where ts.id = o.setmeal_id group by ts.name

        //2.根据需求将页面需要的数据放入返回结果对象中
        rsMap.put("reportDate",reportDate);//当前报表的日期
        rsMap.put("todayNewMember",todayNewMember);//今天新增会员数
        rsMap.put("totalMember",totalMember);//总会员数
        rsMap.put("thisWeekNewMember",thisWeekNewMember);
        rsMap.put("thisMonthNewMember",thisMonthNewMember);
        rsMap.put("todayOrderNumber",todayOrderNumber);
        rsMap.put("todayVisitsNumber",todayVisitsNumber);
        rsMap.put("thisWeekOrderNumber",thisWeekOrderNumber);
        rsMap.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        rsMap.put("thisMonthOrderNumber",thisMonthOrderNumber);
        rsMap.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        rsMap.put("hotSetmeal",hotSetmeal);//热门套餐
        return rsMap;
    }

}
