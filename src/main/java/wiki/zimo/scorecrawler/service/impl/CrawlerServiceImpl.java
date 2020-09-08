package wiki.zimo.scorecrawler.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wiki.zimo.scorecrawler.domain.Score;
import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.CrawlerService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerServiceImpl implements CrawlerService {
    @Value("${SELECT_ROLE_API}")
    private String SELECT_ROLE_API;// 成绩查询之前的选择角色接口
    @Value("${SCORE_API}")
    private String SCORE_API;// 成绩查询接口
    @Value("${XSJBXX_URL}")
    private String XSJBXX_URL;// 学生基本信息查询页
    @Value("${XSJBXX_API}")
    private String XSJBXX_API;// 学生基本信息查询接口

    public enum SystemType {
        OLD,
        NEW
    }

    @Override
    public Student getStudentWithScore(Map<String, String> cookies) throws IOException {
        Student user = new Student();
        queryStudentInfo(cookies, user);
        queryScore(cookies, user, SystemType.NEW, null);

//        System.out.println("解析后的成绩：");
//        System.out.println(scores);


//        System.out.println(user);

        return user;
    }

    @Override
    public Student getStudentWithScore(Map<String, String> cookies, SystemType type, String path) throws IOException {
        Student user = new Student();
        queryStudentInfo(cookies, user);
        queryScore(cookies, user, SystemType.OLD, path);

//        System.out.println("解析后的成绩：");
//        System.out.println(scores);


//        System.out.println(user);

        return user;
    }

    /**
     * 查询成绩信息
     *
     * @param cookies
     * @param user
     * @param type
     * @throws IOException
     */
    private void queryScore(Map<String, String> cookies, Student user, SystemType type, String path) throws IOException {
        if (type == SystemType.NEW) {
            // 请求选择角色接口，获取成绩查询url
            Document select = Jsoup.connect(SELECT_ROLE_API.replace("{}", String.valueOf(System.currentTimeMillis()))).ignoreContentType(true).cookies(cookies).get();
            // 解析接口返回的json
            JSONObject json = JSON.parseObject(select.body().text());
            // 拿到成绩查询页的url
            String url = json.getJSONObject("data").getJSONArray("groupList").getJSONObject(0).getString("targetUrl");
            // 请求成绩查询页
            Connection.Response res = Jsoup.connect(url).cookies(cookies).execute();
            // 保存成绩查询cookie
            cookies.putAll(res.cookies());

            // 请求成绩查询接口，请求这个接口之前必须先登陆和请求成绩查询url
            Document doc = Jsoup.connect(SCORE_API).cookies(cookies).ignoreContentType(true).get();

            // 获取成绩json对象
            JSONObject scoreJson = JSON.parseObject(doc.body().text());

//        System.out.println("成绩查询接口：");
//        System.out.println(scoreJson);

            // 解析成绩
            JSONArray scoreJsonArray = scoreJson.getJSONObject("datas").getJSONObject("xscjcx").getJSONArray("rows");
            List<Score> scores = new ArrayList<>();
            for (int i = 0; i < scoreJsonArray.size(); i++) {
                JSONObject obj = scoreJsonArray.getJSONObject(i);
                Score score = new Score();
                score.setXq(obj.getString("XNXQDM"));
                score.setKcmc(obj.getString("KCM"));
                score.setKclb(obj.getString("KCLBDM_DISPLAY"));
                score.setXdzk(obj.getString("CXCKDM_DISPLAY"));
                score.setXf(obj.getDouble("XF"));
                score.setCj(obj.getInteger("ZCJ"));
                score.setJd(obj.getDouble("XFJD"));
                scores.add(score);
            }
            user.setScores(scores);
        } else if (type == SystemType.OLD) {
            // 老教务系统
            Document ss = Jsoup.parse(new File(path), "gbk");
            Element table = ss.getElementsByClass("tablebody").get(0);
            Elements trs = table.getElementsByTag("tr");
            List<Score> scores = new ArrayList<>();
            for (int i = 0; i < trs.size(); i++) {
                if (i == 0 || i == trs.size() - 1) continue;
                Element tr = trs.get(i);
                Elements tds = tr.getElementsByTag("td");
                Score score = new Score();
                for (int j = 0; j < tds.size(); j++) {
                    if (j == 0 || j == tds.size() - 1) continue;
                    Element td = tds.get(j);
                    switch (j) {
                        case 1:
                            score.setXq(td.text());
                            break;
                        case 3:
                            score.setKcmc(td.text());
                            break;
                        case 4:
                            score.setKclb(td.text());
                            break;
                        case 5:
                            score.setXf(Double.valueOf(td.text()));
                            break;
                        case 6:
                            double dcj = Double.valueOf(td.text());
                            score.setCj((int) (dcj));
                            break;
                        case 7:
                            int cj = score.getCj();
                            double jd = 0.0;
                            switch (cj / 10) {
                                case 10:
                                    jd = 5;
                                    break;
                                case 9:
                                    jd = 4;
                                    break;
                                case 8:
                                    jd = 3;
                                    break;
                                case 7:
                                    jd = 2;
                                    break;
                                case 6:
                                    jd = 1;
                                    break;
                            }
                            jd += (cj % 10 / 10.0);
                            score.setJd(jd);
                            break;
                    }
                }
                if (score.getCj() >= 60) {
                    score.setXdzk("初修");
                }
                scores.add(score);
            }
            user.setScores(scores);
        }
    }

    /**
     * 查询学生信息
     *
     * @param cookies
     * @param user
     * @throws IOException
     */
    private void queryStudentInfo(Map<String, String> cookies, Student user) throws IOException {
        // 请求学生基本信息页
        Connection.Response res = Jsoup.connect(XSJBXX_URL).cookies(cookies).execute();
        // 更新cookie
        cookies.putAll(res.cookies());

        // 请求学生基本信息接口，请求这接口之前必须先请求学生基本信息页以获取cookie
        Document doc = Jsoup.connect(XSJBXX_API).cookies(cookies).ignoreContentType(true).get();

//        System.out.println("学生基本信息接口：");
//        System.out.println(doc.body().text());

        // 解析学生基本信息
        JSONObject studentJson = JSON.parseObject(doc.body().text()).getJSONObject("datas").getJSONObject("cxxsjbxxlb").getJSONArray("rows").getJSONObject(0);
        user.setXh(studentJson.getString("XSBH"));
        user.setXm(studentJson.getString("XM"));
        user.setBj(studentJson.getString("BJMC"));
        user.setNj(studentJson.getString("XZNJ"));
        user.setYxmc(studentJson.getString("YXDM_DISPLAY"));
        user.setZymc(studentJson.getString("NDZYMC"));
        user.setXz(studentJson.getString("XZ"));
    }

}
