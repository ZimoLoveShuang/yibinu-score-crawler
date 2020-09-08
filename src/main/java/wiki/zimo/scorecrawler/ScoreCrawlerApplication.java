package wiki.zimo.scorecrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@SpringBootApplication
@EnableScheduling
public class ScoreCrawlerApplication extends SpringBootServletInitializer {

    public static String fileRootPath = null;

    public static void main(String[] args) {
        init();
        SpringApplication.run(ScoreCrawlerApplication.class, args);

    }

    /**
     * @auther: 子墨
     * @date: 2018/11/24 19:24
     * @describe: Tomcat启动Springboot项目
     * @param: [builder]
     * @return: org.springframework.boot.builder.SpringApplicationBuilder
     * @version v1.0
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        init();
        return builder.sources(this.getClass());
    }

    /**
     * 初始化获取当前项目启动目录
     */
    private static void init() {
        File file = new File("");
        fileRootPath = file.getAbsolutePath() + File.separator + "tempfiles" + File.separator;
        file = new File(fileRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        System.out.println("yibinu-score-crawler start at: " + fileRootPath);
    }
}
