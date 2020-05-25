package wiki.zimo.scorecrawler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wiki.zimo.scorecrawler.dao.VisitorDao;
import wiki.zimo.scorecrawler.domain.Visitor;
import wiki.zimo.scorecrawler.service.VisitorService;

@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    private VisitorDao dao;

    @Override
    public void addVisitorNum() {
        Visitor visitor = dao.findById(1).get();
        visitor.setNum(visitor.getNum() + 1);
        dao.save(visitor);
    }

    @Override
    public int getVisitorNum() {
        return dao.findById(1).get().getNum();
    }
}
