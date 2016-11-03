package ccy.work.commons.utils.excel;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: bianfeng
 * Date: 12-2-26
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class ExcelExportStrategy extends ExportStrategy {
    public ExcelExportStrategy() {
    }

    public void export(OutputStream outputStream) {
        //创建Excel工作簿
        HSSFWorkbook workBook = new HSSFWorkbook();
        //创建Excel工作页（表）
        HSSFSheet sheet = workBook.createSheet("Sheet1");
        try {
            String[] titles = getTitles();
            //创建一行
            HSSFRow row = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                // 创建一个单元格
                HSSFCell cell = row.createCell(i);
                //设置单元格的值
                cell.setCellValue(new HSSFRichTextString(titles[i]));
            }
            for (int i = 0; i < getRows(); i++) {
                row = sheet.createRow(i + 1);
                for (int j = 0; j < getCols(); j++) {
                    Object data = getData(i, j);
                    HSSFCell cell;
                    if (null != data) {
                        if (data instanceof Boolean) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_BOOLEAN);
                            cell.setCellValue((Boolean) getData(i, j));
                        } else if (data instanceof Integer) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Integer) getData(i, j));
                        } else if (data instanceof Short) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Short) getData(i, j));
                        } else if (data instanceof Long) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Long) getData(i, j));
                        } else if (data instanceof Double) {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Double) getData(i, j));
                        } else if (data instanceof String) {
                            if (data.toString().equals("错误原因")) {
                                cell = row.createCell(j, HSSFCell.CELL_TYPE_ERROR);
                            } else {
                                cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            }
                            cell.setCellValue((String) getData(i, j));
                        }else if(data instanceof BigDecimal){
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(((BigDecimal)getData(i, j)).floatValue());
                        }else {
                            cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue((String) getData(i, j));
                        }
                    } else {
                        cell = row.createCell(j, HSSFCell.CELL_TYPE_BLANK);
                    }

//                    HSSFCell cell = row.createCell(j);
//                    cell.setCellValue(new HSSFRichTextString(getData(i, j).toString()));
                }
            }
            //将excel的内容输出到流中
            workBook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException("无法导出内容!", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("输出流关闭失败!", e);
            }
        }

    }

    public void export2007(OutputStream outputStream) {
        String[] titles = getTitles();

        XSSFWorkbook workbook = new XSSFWorkbook();

        CellStyle style = workbook.createCellStyle();
        DataFormat df = workbook.createDataFormat();
        style.setDataFormat(df.getFormat("TEXT") );

        Sheet sh = workbook.createSheet("Sheet1");
        //导出表头
        Row row = sh.createRow(0);
        for(int i=0;i<titles.length;i++){
            createCell(style, row, i, titles[i]);
        }
        //导出模版数据
        for (int i = 0; i < getRows(); i++) {
            row = sh.createRow(i + 1);
            for (int j = 0; j < getCols(); j++) {
                Object data = getData(i, j);
                createCell(style, row, j, data);
            }
        }

        try {
            workbook.write(outputStream);
        }catch (IOException e) {
            throw new RuntimeException("无法导出内容!", e);
        }finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("输出流关闭失败!", e);
            }
        }
    }

    private void createCell(CellStyle style,Row row,int column,Object content){
        Cell cell = row.createCell(column);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(style);
        if(content != null){
            RichTextString ts = new XSSFRichTextString(content.toString()) ;
            cell.setCellValue(ts);
        }else{
            cell.setCellValue(new XSSFRichTextString(""));
        }
    }
}
