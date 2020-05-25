package wiki.zimo.scorecrawler.service;

import java.util.Map;

public interface LoginService {
    Map<String, String> login(String login_url, String needcaptcha_url, String captcha_url, String username, String password) throws Exception;

    Map<String, String> login(String username, String password) throws Exception;
}
