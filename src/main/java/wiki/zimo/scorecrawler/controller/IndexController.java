package wiki.zimo.scorecrawler.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class IndexController {

    @Value("${author.qq}")
    private String qq;

    @RequestMapping(path = {"/", "/index", "/index.html", "/index.jsp"})
    public String index(Map<String, Object> map) {
        map.put("qq", qq);
        return "index";
    }
}
