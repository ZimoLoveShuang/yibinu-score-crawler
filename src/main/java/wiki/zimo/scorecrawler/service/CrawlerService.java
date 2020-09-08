package wiki.zimo.scorecrawler.service;

import wiki.zimo.scorecrawler.domain.Student;
import wiki.zimo.scorecrawler.service.impl.CrawlerServiceImpl;

import java.io.IOException;
import java.util.Map;

public interface CrawlerService {
    Student getStudentWithScore(Map<String, String> cookies) throws IOException;

    Student getStudentWithScore(Map<String, String> cookies, CrawlerServiceImpl.SystemType type, String path) throws IOException;
}
