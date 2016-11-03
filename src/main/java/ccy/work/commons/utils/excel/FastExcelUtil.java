package ccy.work.commons.utils.excel;


import ccy.work.commons.exception.WrongTemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 快速简单操作Excel的工具
 *
 * @author peiyu
 */
public final class FastExcelUtil implements Closeable {
    private static final Log LOG = LogFactory.getLog(FastExcelUtil.class);
    /**
     * 时日类型的数据默认格式化方式
     */
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private int startRow;
    private String sheetName;
    private Workbook workbook;

    /**
     * 文件后缀
     */
    private static final String FILE_SUFFIX = ".xls";

    /**
     * 文件路径
     */
    private static final String FILE_PATH = "static/excel/";

    public FastExcelUtil() {
        this.startRow = 0;
        this.sheetName = "Sheet1";
//        this.workbook = WorkbookFactory.create(excelFile);
    }

    /**
     * 开始读取的行数，这里指的是标题内容行的行数，不是数据开始的那行
     *
     * @param startRow 开始行数
     */
    public void setStartRow(int startRow) {
        if (startRow < 1) {
            throw new RuntimeException("最小为1");
        }
        this.startRow = --startRow;
    }

    /**
     * 设置需要读取的sheet名字，不设置默认的名字是Sheet1，也就是excel默认给的名字，所以如果文件没有自已修改，这个方法也就不用调了
     *
     * @param sheetName 需要读取的Sheet名字
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 设置时间数据格式
     *
     * @param format 格式
     */
    public void setFormat(String format) {
        this.format = new SimpleDateFormat(format);
    }


    /**
     * @param <T>
     * @param clazz     excel对象
     * @param excelFile 要解析的excel类型的文件
     * @return
     */
    public <T> List<T> parseExcel(Class<T> clazz, File excelFile)throws WrongTemplateException {
        List<T> resultList = null;
        try {
            Workbook workbook = WorkbookFactory.create(excelFile);
            Sheet sheet = workbook.getSheet(this.sheetName);
            if (null != sheet && sheet.getLastRowNum() > 0) {
                resultList = new ArrayList<T>(sheet.getLastRowNum() - 1);
                Row row = sheet.getRow(this.startRow);

                Map<String, Field> fieldMap = new HashMap<String, Field>();
                Map<String, String> titalMap = new HashMap<String, String>();

                Field[] fields = clazz.getDeclaredFields();

                if(!this.validateCellNames(row,fields)){
                    throw new Exception("文件解析异常", new Exception());
                }

                //这里开始处理映射类型里的注解
                for (Field field : fields) {
                    if (field.isAnnotationPresent(MapperCell.class)) {
                        MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                        fieldMap.put(mapperCell.cellName(), field);
                    }
                }

                for (Cell tital : row) {
                    CellReference cellRef = new CellReference(tital);
//                    titalMap.put(cellRef.getCellRefParts()[2], tital.getRichStringCellValue().getString());
                    titalMap.put(cellRef.getCellRefParts()[2], tital.getStringCellValue());
                }
//                if (titalMap.size() != fieldMap.size()) {
//                    throw new WrongTemplateException("错误的模板！");
//                }
                for (int i = this.startRow + 1; i <= sheet.getLastRowNum(); i++) {
                    T t = clazz.newInstance();
                    Row dataRow = sheet.getRow(i);
                    if (dataRow == null) {
                        continue;
                    }
                    for (Cell data : dataRow) {
                        CellReference cellRef = new CellReference(data);
                        String cellTag = cellRef.getCellRefParts()[2];
                        String name = titalMap.get(cellTag);
                        Field field = fieldMap.get(name);
                        if (null != field) {
                            field.setAccessible(true);
                            getCellValue(data, t, field);
                        }
                    }
                    resultList.add(t);
                }
            }
        } catch (InstantiationException e) {
            LOG.error("初始化异常", e);
            throw new WrongTemplateException("初始化异常",e);
        } catch (IllegalAccessException e) {
            LOG.error("初始化异常", e);
            throw new WrongTemplateException("初始化异常",e);
        } catch (ParseException e) {
            LOG.error("时间格式化异常:{}", e);
            throw new WrongTemplateException("时间格式化异常",e);
        } catch (InvalidFormatException e) {
            LOG.error("初始化异常", e);
            throw new WrongTemplateException("初始化异常",e);
        } catch (IOException e) {
            LOG.error("初始化异常", e);
            throw new WrongTemplateException("初始化异常",e);
        } catch (NullPointerException e){
            LOG.error("文件解析异常", e);
            throw new WrongTemplateException("文件解析异常,请检查文件的格式和内容!",e);
        } catch (Exception e) {
            LOG.error("文件解析异常", e);
            throw new WrongTemplateException("文件解析异常,请检查文件的格式和内容!",e);
        }
        return resultList;
    }


    private boolean validateCellNames(Row row, Field[] fields){
        List<String> list = new ArrayList<String>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(MapperCell.class)) {
                MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                String cellName = mapperCell.cellName();
                list.add(cellName);
            }
        }
        if (null == list || list.size() == 0) return false;
        for (Cell tital : row) {
            if (!list.contains(tital.getStringCellValue())){
                return false;
            }else{
                continue;
            }
        }
        return true;
    }


    private void getCellValue(Cell cell, Object o, Field field) throws IllegalAccessException, ParseException {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                field.setBoolean(o, cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                field.setByte(o, cell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                field.set(o, cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (field.getType().getName().equals(Date.class.getName())) {
                        field.set(o, cell.getDateCellValue());
                    } else {
                        field.set(o, format.format(cell.getDateCellValue()));
                    }
                } else {
                    if (field.getType().isAssignableFrom(Integer.class) || field.getType().getName().equals("int")) {
                        field.setInt(o, (int) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Short.class) || field.getType().getName().equals("short")) {
                        field.setShort(o, (short) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Float.class) || field.getType().getName().equals("float")) {
                        field.setFloat(o, (float) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Byte.class) || field.getType().getName().equals("byte")) {
                        field.setByte(o, (byte) cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(Double.class) || field.getType().getName().equals("double")) {
                        field.setDouble(o, cell.getNumericCellValue());
                    } else if (field.getType().isAssignableFrom(String.class)) {
                        String s = String.valueOf(cell.getNumericCellValue());
                        if (s.contains("E")) {
                            s = s.trim();
                            BigDecimal bigDecimal = new BigDecimal(s);
                            s = bigDecimal.toPlainString();
                        } else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            s = cell.getStringCellValue();
                        }
                        field.set(o, s);
                    } else {
                        field.set(o, cell.getNumericCellValue());
                    }
                }
                break;
            case Cell.CELL_TYPE_STRING:
                if (field.getType().getName().equals(Date.class.getName())) {
                    field.set(o, format.parse(cell.getRichStringCellValue().getString()));
                } else {
//                    field.set(o, cell.getRichStringCellValue().getString());
                    field.set(o,cell.getStringCellValue());
                }
                break;
            default:
                field.set(o, cell.getStringCellValue());
                break;
        }
    }


    /**
     * 导出excel
     *
     * @param <T>
     * @param request
     * @param response
     * @param list
     * @param fileName
     * @return
     * @throws InvalidFormatException
     */
    public <T> boolean exportExcel(HttpServletRequest request, HttpServletResponse response, List<T> list, String fileName) throws InvalidFormatException {
        boolean result = false;
        if (null != list && !list.isEmpty()) {
            T test = list.get(0);
            Map<String, Field> fieldMap = new HashMap<String, Field>();
            Map<Integer, String> titalMap = new TreeMap<Integer, String>();
            Field[] fields = test.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MapperCell.class)) {
                    MapperCell mapperCell = field.getAnnotation(MapperCell.class);
                    fieldMap.put(mapperCell.cellName(), field);
                    titalMap.put(mapperCell.order(), mapperCell.cellName());
                }
            }
            OutputStream os = null;
            try {
                File excelFile = new File(request.getSession().getServletContext().getRealPath("/") + FILE_PATH + fileName + FILE_SUFFIX);
                Workbook workbook = WorkbookFactory.create(excelFile);
                Sheet sheet = workbook.getSheet(this.sheetName);

                if (null == sheet) {
                    LOG.error("流异常");
                    throw new RuntimeException("sheet name '" + this.sheetName + "'is not exist");
                }
                Collection<String> values = titalMap.values();
                String[] s = new String[values.size()];
                values.toArray(s);

                for (int i = 0, length = list.size(); i < length; i++) {
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < s.length; j++) {
                        if (i == 0) {
                            Cell cell = row.createCell(j);
                            cell.setCellValue(s[j]);
                        } else {
                            Cell cell = row.createCell(j);
                            for (Map.Entry<String, Field> data : fieldMap.entrySet()) {
                                if (data.getKey().equals(s[j])) {
                                    Field field = data.getValue();
                                    field.setAccessible(true);
                                    cell.setCellValue(field.get(list.get(i)).toString());
                                    break;
                                }
                            }
                        }

                    }
                }
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + FILE_SUFFIX);

                os = response.getOutputStream();
                workbook.write(os);
                os.flush();
            } catch (IOException e) {
                LOG.error("IOException", e);
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                LOG.error("IllegalAccessException", e);
                throw new RuntimeException(e);
            } finally {
                if (null != os) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        LOG.error("关闭流异常", e);
                        throw new RuntimeException(e);
                    }
                }
            }
            result = true;
        }
        return result;
    }

//    /**
//     * 获取指定单元格的值
//     *
//     * @param rowNumber  行数，从1开始
//     * @param cellNumber 列数，从1开始
//     * @return 该单元格的值
//     */
//    public String getCellValue(int rowNumber, int cellNumber) {
//        String result;
//        checkRowAndCell(rowNumber, cellNumber);
//        Sheet sheet = this.workbook.getSheet(this.sheetName);
//        Row row = sheet.getRow(--rowNumber);
//        Cell cell = row.getCell(--cellNumber);
//        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_BLANK:
//                result = cell.getStringCellValue();
//                break;
//            case Cell.CELL_TYPE_BOOLEAN:
//                result = String.valueOf(cell.getBooleanCellValue());
//                break;
//            case Cell.CELL_TYPE_ERROR:
//                result = String.valueOf(cell.getErrorCellValue());
//                break;
//            case Cell.CELL_TYPE_FORMULA:
//                result = cell.getCellFormula();
//                break;
//            case Cell.CELL_TYPE_NUMERIC:
//                if (DateUtil.isCellDateFormatted(cell)) {
//                    result = format.format(cell.getDateCellValue());
//                } else {
//                    result = String.valueOf(cell.getNumericCellValue());
//                }
//                break;
//            case Cell.CELL_TYPE_STRING:
//                result = cell.getRichStringCellValue().getString();
//                break;
//            default:
//                result = cell.getStringCellValue();
//                break;
//        }
//        return result;
//    }

    @Override
    public void close() throws IOException {
        if (this.workbook != null) {
            this.workbook.close();
        }
    }

//    private void checkRowAndCell(int rowNumber, int cellNumber) {
//        if (rowNumber < 1) {
//            throw new RuntimeException("rowNumber less than 1");
//        }
//        if (cellNumber < 1) {
//            throw new RuntimeException("cellNumber less than 1");
//        }
//    }
}
