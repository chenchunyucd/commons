package ccy.work.commons.utils.excel;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: bianfeng
 * Date: 12-2-26
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
public class TemplateExcelExportStrategy extends ExportStrategy {
    /**
     * 存储导出数据
     */
    private Map<String, Object> dataMap;
    /**
     * 模版路径
     */
    private String template;

    public TemplateExcelExportStrategy() {
    }

    public TemplateExcelExportStrategy(String template, Map<String, Object> dataMap) {
        this.template = template;
        this.dataMap = dataMap;
    }

    public void export(OutputStream outputStream) {
        XLSTransformer transformer = new XLSTransformer();
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(template));
            HSSFWorkbook workbook = transformer.transformXLS(is, dataMap);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("模版文件没有找到!", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException("输入流关闭失败!", e);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("输出流关闭失败!", e);
                }
            }
        }
    }

    /**
     * @param request
     * @param response
     * @param exportName   生成的下载文件名
     * @param templateName 模版文件名
     * @param dataMap      要导出的数据
     * @throws Exception
     */
    public void export(HttpServletRequest request, HttpServletResponse response, String exportName, String templateName, Map<String, Object> dataMap) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        response.addHeader("Content-Disposition", "attachment;filename=\"" + exportName + "\"");

        String templateFullPath = request.getSession().getServletContext().getRealPath("/") + templateName;
        XLSTransformer transformer = new XLSTransformer();
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(templateFullPath));
            HSSFWorkbook workbook = transformer.transformXLS(is, dataMap);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException("输入流关闭失败!", e);
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    throw new RuntimeException("输出流关闭失败!", e);
                }
            }
        }
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
