package wiki.zimo.scorecrawler.controller;

import com.deepoove.poi.XWPFTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wiki.zimo.scorecrawler.ScoreCrawlerApplication;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;
import wiki.zimo.scorecrawler.service.LoginService;
import wiki.zimo.scorecrawler.service.TemplateService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Map;


@Controller
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private TemplateService templateService;
//    @Autowired
//    private VisitorService visitorService;

    @RequestMapping("/getScoreReport")
    public void getScoreReport(@RequestParam("xh") String xh, @RequestParam("pwd") String pwd, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Student student = new Student(xh, pwd);
            Map<String, String> cookies = loginService.login(student.getXh(), student.getPwd());

            // 登陆成功
            // 抓取成绩信息
            // 默认新系统
            Student studentWithScore = crawlerService.getStudentWithScore(cookies);
            // 老系统
//            Student studentWithScore = crawlerService.getStudentWithScore(cookies, CrawlerServiceImpl.SystemType.OLD,"C:\\\\Users\\\\zimo\\\\Downloads\\\\详细成绩.html");
            // 获取模板
            XWPFTemplate template = templateService.renderWordTemplate(studentWithScore);
            String fileName = String.valueOf(System.currentTimeMillis());
            String filePath = ScoreCrawlerApplication.fileRootPath + fileName + ".docx";
            File tempFile = new File(filePath);
            // 渲染到doc文件
            template.writeToFile(tempFile.getAbsolutePath());

            // 设置http头信息
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(studentWithScore.getXm() + "成绩单", "utf-8") + ".docx");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Length", String.valueOf(tempFile.length()));

            // 写到网络下载流
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            InputStream in = new FileInputStream(tempFile);
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();

            // 删除临时文件
            tempFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
            // 登陆失败
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<script>alert('登陆失败，" + e.getMessage() + "');window.location.href='" + request.getContextPath() + "';</script>");
        }
    }
}
