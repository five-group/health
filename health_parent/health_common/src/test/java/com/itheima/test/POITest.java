package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * POI入门案例
 * @author wangxin
 * @version 1.0
 */
public class POITest {

    /**
     * 方式一
     * @throws IOException
     */
    //@Test
    public void readExcel() throws IOException {
        //1.获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\admin\\Desktop\\read.xlsx");
        //2.获取Sheet对象
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //3.获取Row对象 (循环遍历Sheet)
        for (Row cells : sheetAt) {
            //4.获取Cell对象
            for (Cell cell : cells) {
                //某一行记录每一个单元格
                System.out.println(cell.getStringCellValue());
            }
            //分行显示
            System.out.println("************************************");
        }
        //5.释放资源
        xssfWorkbook.close();

    }


    /**
     * 方式二
     * @throws IOException
     */
    //@Test
    public void readExcel2() throws IOException {
        //1.获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\Users\\admin\\Desktop\\read.xlsx");
        //2.获取Sheet对象
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();//1
        for(int i=0;i<=lastRowNum;i++){
            //获取每一行对象
            XSSFRow row = sheetAt.getRow(i);
            //获取每一列对象
            short lastCellNum = row.getLastCellNum();
            for (int j = 0;j<lastCellNum;j++){ // 0 1 2
                String cellValue = row.getCell(j).getStringCellValue();
                System.out.println(cellValue);
            }
            System.out.println("************************************");
        }

        //5.释放资源
        xssfWorkbook.close();

    }


    /**
     * 创建Excel
     */
    //@Test
    public void CreateExcel() throws IOException {
        //1.创建Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();//空Excel对象 内存中
        //2.创建Sheet对象
        XSSFSheet sheet = xssfWorkbook.createSheet("user");
        //3.创建Row对象
        //标题行
        XSSFRow titleRow = sheet.createRow(0);

        XSSFRow dataRow = sheet.createRow(1);
        //4.创建Cell对象，设置单元格数据
        titleRow.createCell(0).setCellValue("编号");
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        dataRow.createCell(0).setCellValue("001");
        dataRow.createCell(1).setCellValue("老王");
        dataRow.createCell(2).setCellValue("18");
        //5.从内存中将数据写入磁盘中
        //输出流对象
        OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\admin\\Desktop\\abc.xlsx"));
        xssfWorkbook.write(outputStream);
        //6.释放资源
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();
    }
}
