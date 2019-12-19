package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.ReportService;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表控制层
 * @author wangxin
 * @version 1.0
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private ReportService reportService;

    /**
     * 会员数量折线图
     */
    @RequestMapping(value = "/getMemberReport",method = RequestMethod.GET)
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();//得到日历对象
        calendar.add(Calendar.MONTH,-12);//一年前时间
        //定义list集合存放年月
        List<String> stringList = new ArrayList<>();
        for (int i = 0;i<12;i++){
            calendar.add(Calendar.MONTH,1);//循环每次+1个月
            stringList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));//当前时间放入list集合中
        }
        //调用服务获取年月对应会员人数
        List<Integer> integerList =  reportService.getMembersByYearMoth(stringList);

        //months memberCount
        Map<String,Object> map = new HashMap<>();
        map.put("months",stringList);
        map.put("memberCount",integerList);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }


    /**
     * 套餐预约占比饼形图
     */
    @RequestMapping(value = "/getSetmealReport",method = RequestMethod.GET)
    public Result getSetmealReport(){
        Map map = reportService.getSetmealReport();
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }


    /**
     * 运营数据统计
     */
    @RequestMapping(value = "/getBusinessReportData",method = RequestMethod.GET)
    public Result getBusinessReportData(){
        Map<String,Object> map = null;
        try {
            map = reportService.getBusinessReportData();
        } catch (Exception e) {
            //需根据不同的异常返回不同的错误信息
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

    /**
     * 运营数据统计报表导出
     */
    /*@RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //a.获取需要导出的运营数据 getBusinessReportData()
            Map<String, Object> result = reportService.getBusinessReportData();
            //b.将需要导出的运营数据统计定义模板
            String filePath =  request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));//文件模板对象
            //c.将数据填充到模板中即可
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            //获取数据
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>)result.get("hotSetmeal");
            row.getCell(5).setCellValue(reportDate);
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            //设置热门套餐数据
            int rowNum = 12;//从13行开始遍历数据
            if(hotSetmeal != null && hotSetmeal.size()>0){
                for (Map map : hotSetmeal) {
                    //name, setmeal_count,  proportion,ts.remark
                    XSSFRow rowHotSetmeal = sheet.getRow(rowNum);
                    rowHotSetmeal.getCell(4).setCellValue((String)map.get("name"));
                    rowHotSetmeal.getCell(5).setCellValue(map.get("setmeal_count").toString());
                    rowHotSetmeal.getCell(6).setCellValue(map.get("proportion").toString());
                    rowHotSetmeal.getCell(7).setCellValue((String)map.get("remark"));
                    rowNum++;
                }
            }
            //d.分别通过HttpServletRequest获取模板以及HttpServletResponse返回文件对象
            ServletOutputStream outputStream = response.getOutputStream();
            //文件名称  attachment:附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            //文件类型 xls xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            xssfWorkbook.write(outputStream);
            //f.资源释放
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/


    /**
     * 运营数据统计报表导出
     */
    @RequestMapping(value = "/exportBusinessReport",method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //a.获取需要导出的运营数据 getBusinessReportData()
            Map<String, Object> result = reportService.getBusinessReportData();
            //b.将需要导出的运营数据统计定义模板
            String filePath =  request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));//文件模板对象
            //c.将数据填充到模板中即可
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(xssfWorkbook, result);
            //d.分别通过HttpServletRequest获取模板以及HttpServletResponse返回文件对象
            ServletOutputStream outputStream = response.getOutputStream();
            //文件名称  attachment:附件形式下载
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            //文件类型 xls xlsx
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            xssfWorkbook.write(outputStream);
            //f.资源释放
            xssfWorkbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();//得到日历对象
        calendar.add(Calendar.MONTH,-12);//一年前时间
        //定义list集合存放年月
        List<String> stringList = new ArrayList<>();
        for (int i = 0;i<12;i++){
            calendar.add(Calendar.MONTH,1);//循环每次+1个月
            stringList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));//当前时间放入list集合中
        }
        System.out.println("");
    }
}
