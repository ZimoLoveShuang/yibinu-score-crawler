package wiki.zimo.scorecrawler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wiki.zimo.scorecrawler.service.VisitorService;

import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private VisitorService visitorService;
    @Value("${author.qq}")
    private String qq;

    @RequestMapping(path = {"/", "/index", "/index.html", "/index.jsp"})
    public String index(Map<String, Object> map) {
        map.put("qq", qq);
        map.put("visitorNum", visitorService.getVisitorNum());
        return "index";
    }
}
