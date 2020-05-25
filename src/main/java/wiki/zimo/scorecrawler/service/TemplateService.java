package wiki.zimo.scorecrawler.service;

import com.deepoove.poi.XWPFTemplate;
import wiki.zimo.scorecrawler.domain.Student;

import java.io.IOException;

public interface TemplateService {
    XWPFTemplate renderWordTemplate(Student student) throws IOException;
}
