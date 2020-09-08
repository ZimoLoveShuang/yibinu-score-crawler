package wiki.zimo.scorecrawler.service.impl;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.util.TableTools;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import wiki.zimo.scorecrawler.domain.Score;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.TemplateService;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemplateServiceImpl implements TemplateService {
    @Override
    public XWPFTemplate renderWordTemplate(Student student) throws IOException {
        File templateFile = ResourceUtils.getFile("classpath:template.docx");
//        System.out.println(templateFile.getAbsolutePath());

        // 模板填充docx对象
        XWPFTemplate template = XWPFTemplate.compile(templateFile);

        // 获取模板中的表格
        XWPFTable table = template.getXWPFDocument().getAllTables().get(0);

        // 准备塞入模板中的文本信息
        Map<String, Object> model = new HashMap<>();
        model.put("xh", student.getXh());
        model.put("xm", student.getXm());
        model.put("bj", student.getBj());
        model.put("nj", student.getNj());
        model.put("yxmc", student.getYxmc());
        model.put("zymc", student.getZymc());
        model.put("xz", student.getXz());
        model.put("qdxf", student.getQdxf());
        model.put("pjjd", student.getPjjd());

        Calendar now = Calendar.getInstance();
        model.put("year", now.get(Calendar.YEAR));
        model.put("month", now.get(Calendar.MONTH) + 1);
        model.put("day", now.get(Calendar.DAY_OF_MONTH));

        // 构造塞入模板的成绩信息，模板中表格固定是35行，21列，分为3个区域，每7列一个区域
        List<Score> scores = student.getScores();
        if (scores == null || scores.size() < 1) {
            return null;
        }

        // 划分模板中的表格区域
        final int rows = table.getRows().size();
        final int cols = table.getRow(0).getTableCells().size();
        final int area = cols / 3;
        int n = 3;// 需要循环写几次
        int pos = 0;// 标记当前正在填入的score数据集下标
        int lastPos = -1;// 标记上一次学年划分的下标
        int xn = 1;// 标记当前成绩是第几学年

        label:
        for (int time = 0; time < n; time++) { // 单次循环
            for (int i = 1; i < rows; i++) { // 0行是表头，所以行从1开始
                Score current = scores.get(pos);

                // 渲染成绩
                for (int j = time * area; j < area + time * area; j++) { // 每次只填7列
                    String key = "r" + i + "c" + j;

                    // 处理成绩渲染和插入学年
                    if (xn == 1) { // 第一学年
                        // 处理合并单元格
                        mergeTable(template, table, cols, i, j, area, time);
                        // 渲染
                        model.put(key, getXn(xn));
                        xn++;
                        break;
                    } else if (isAcademicYearDivision(xn, lastPos, pos, scores)) { // 其他学年
                        lastPos = pos;
//                        System.out.println("pos=" + pos);
//                        System.out.println("其他学年+" + getXn(xn));
                        // 处理合并单元格
                        mergeTable(template, table, cols, i, j, area, time);
                        // 渲染
                        model.put(key, getXn(xn));
                        xn++;
                        break;
                    }

                    TextRenderData val = null;
                    switch (j - time * area) {
                        case 0:
                            val = new TextRenderData(String.valueOf(current.getXqSort()));
                            break;
                        case 1:
                            val = getKcmc(current.getKcmc());
                            break;
                        case 2:
                            val = getKclb(current.getKclb());
                            break;
                        case 3:
                            val = new TextRenderData(current.getXdzk());
                            break;
                        case 4:
                            val = new TextRenderData(String.valueOf(current.getXf()));
                            break;
                        case 5:
                            val = new TextRenderData(String.valueOf(current.getCj()));
                            break;
                        case 6:
                            val = new TextRenderData(String.valueOf(current.getJd()));
                            pos++;
                            break;
                    }
                    model.put(key, val);
//                    System.out.println(key + "," + val);
                }


                // 所有成绩信息已经填完，应该退出大循环了
                if (pos >= scores.size()) {
                    break label;
                }
            }
        }
//        System.out.println("n=" + n);

//        FileOutputStream out = new FileOutputStream(new File("C:\\Users\\zimo\\Desktop\\out.docx"));
//        template.write(out);
//        out.close();

        // 塞入模板文档渲染
        template.render(model);
//        System.out.println(model);

//        System.out.println("pos=" + pos);
//        if (pos == scores.size()) {
//            System.out.println("渲染成功");
//        } else {
//            System.out.println("渲染失败，size=" + scores.size());
//        }

        return template;
    }

    /**
     * 处理表格合并
     *
     * @param template
     * @param table
     * @param cols
     * @param i
     * @param j
     * @param area
     * @param time
     */
    private void mergeTable(XWPFTemplate template, XWPFTable table, int cols, int i, int j, int area, int time) {
        // 合并表格之前需要先删除模板中多余的项，否则会抛出异常
        List<ElementTemplate> elementTemplates = template.getElementTemplates();
        for (int k = j + 1; k < area + time * area; k++) {
            String s = "{{r" + i + "c" + k + "}}";
            // 看了poi-tl实现的源码，发现是ArrayList且实体类没有重写equals方法，于是倒着删除
            for (int x = elementTemplates.size() - 1; x >= 0; x--) {
                if (elementTemplates.get(x).toString().equals(s)) {
                    elementTemplates.remove(x);
                }
            }
        }

        int fromCol = j;
        int size = table.getRow(i).getTableCells().size();
        if (size < cols) { // 判断当前行是否合并过单元格
            switch (time) {
                case 1: // 第二轮填的时候
                    fromCol = 1;
                    break;
                case 2: // 第三轮填的时候
                    switch (size) {
                        case 9: // 第一轮第二轮都合并过
                            fromCol = 2;
                            break;
                        case 15: // 第一轮合并过，第二轮没合并
                            fromCol = 8;
                            break;
                    }
                    break;
            }
        }

        // 合并
        TableTools.mergeCellsHorizonal(table, i, fromCol, fromCol + area - 1);
    }

    /**
     * 获取含中文的字符串的长度
     *
     * @param value
     * @return
     */
    private int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 获取课程名称并设置样式
     *
     * @param kcmc
     * @return
     */
    private TextRenderData getKcmc(String kcmc) {
        TextRenderData textRenderData = new TextRenderData();
        Style style = new Style();
        textRenderData.setText(kcmc);
        int fontSize = 7;
        int len = length(kcmc);
        if (len > 26) {
            fontSize -= 2;
        }
//        System.out.println(kcmc + ",len=" + len + ",length=" + kcmc.length() + ",fontsize=" + fontSize);
        style.setFontSize(fontSize);
        textRenderData.setStyle(style);
        return textRenderData;
    }

    /**
     * 获取课程类别并设置样式
     *
     * @param kclb
     * @return
     */
    private TextRenderData getKclb(String kclb) {
        TextRenderData textRenderData = new TextRenderData();
        Style style = new Style();
        textRenderData.setText(kclb);
        int fontSize = 7;
        if (kclb.length() > 6) {
            fontSize--;// 字体减小1号
        }
//        System.out.println(kclb + "," + kclb.length() + "," + fontSize);
        style.setFontSize(fontSize);
        textRenderData.setStyle(style);
        return textRenderData;
    }

    /**
     * 获取学年并设置样式
     *
     * @param xn
     * @return
     */
    private TextRenderData getXn(int xn) {
        TextRenderData textRenderData = new TextRenderData();
        Style style = new Style();
        style.setBold(true);
        style.setFontFamily("宋体");
        style.setFontSize(8);
        textRenderData.setStyle(style);
        String xnStr = null;
        switch (xn) {
            case 1:
                xnStr = "第一学年";
                break;
            case 2:
                xnStr = "第二学年";
                break;
            case 3:
                xnStr = "第三学年";
                break;
            case 4:
                xnStr = "第四学年";
                break;
        }
        textRenderData.setText(xnStr);
        return textRenderData;
    }

    /**
     * 判断是否是学年分界线
     *
     * @param xn
     * @param lastPost
     * @param pos
     * @param scores
     * @return
     */
    private boolean isAcademicYearDivision(int xn, int lastPost, int pos, List<Score> scores) {
        if (pos <= 0 || xn < 1 || xn > 4 || pos == lastPost) {
            return false;
        }
        Score current = scores.get(pos);
        Score last = scores.get(pos - 1);
        return current.getXqSort() - last.getXqSort() == 1 && current.getXqSort() % 2 == 1;
    }
}
