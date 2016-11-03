package ccy.work.commons.utils.excel;



import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bianfeng
 * Date: 12-2-26
 * Time: 下午12:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class ExportStrategy {
    /**
     * 存储导出标题
     */
    private String[] titles;
    /**
     * 存储导出内容
     */
    private List<List<Object>> dataList = new ArrayList<List<Object>>();

    /**
     * 设置标题内容
     */
    public void setTitle(String... title) {
        this.titles = title;
    }

    /**
     * 获取标题内容
     */
    public String[] getTitles() {
        return titles;
    }

    /**
     * 设置一行中的数据
     */
    public void setData(Object... data) {
        if (data.length > getCols()) {
            throw new IllegalArgumentException("数据数量多于标题列数");
        }
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < data.length; i++) {
            list.add(data[i]);
        }
        dataList.add(list);
    }

    /**
     * 获取指定位置的数据
     */
    public Object getData(int row, int col) {
        if (row > getRows() || col > getCols()) {
            throw new IllegalArgumentException("无效的位置");
        }
        return dataList.get(row).get(col);
    }

    /**
     * 获取导出数据的列数
     *
     * @return
     */
    public int getCols() {
        return titles.length;
    }

    /**
     * 获取导出数据的行数
     *
     * @return
     */
    public int getRows() {
        return dataList.size();
    }

    /**
     * 在响应流中设置下载文件名
     */
    public void setDownloadFileName(HttpServletRequest request,HttpServletResponse response, String fileName) {
        try {
            boolean is2007Excel=false;
            //根据文件后缀判断需要导出的是否是2007，只支持.xlsx后缀
            if(StringUtils.isNotBlank(fileName)){
                is2007Excel=fileName.endsWith(".xlsx");
            }
            String agent = request.getHeader("User-Agent");
            boolean isMSIE = ((agent != null && agent.indexOf("MSIE") != -1 )
                    || ( null != agent && -1 != agent.indexOf("like Gecko")));    //判断版本,后边是判断IE11的
            if (isMSIE) {
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            }
            if(is2007Excel){
                //设置2007格式头
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }else {
                response.setContentType("application/vnd.ms-excel;charset=utf-8");
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("无法设置文件头!", e);
        }

    }

    /**
     * 将数据按照具体策略中的格式导出到输出流中
     *
     * @param outputStream
     */
    public abstract void export(OutputStream outputStream);

    /**
     * 导出数据到2007版本
     * @param outputStream
     */
    public void export2007(OutputStream outputStream){

    }
}
