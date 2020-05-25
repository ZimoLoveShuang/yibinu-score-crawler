package wiki.zimo.scorecrawler.service;

import wiki.zimo.scorecrawler.domain.Student;

import java.io.IOException;
import java.util.Map;

public interface CrawlerService {
    Student getStudentWithScore(Map<String, String> cookies) throws IOException;

//    Student getStudentWithScore(Student user, Map<String, String> cookies) throws IOException;
}
