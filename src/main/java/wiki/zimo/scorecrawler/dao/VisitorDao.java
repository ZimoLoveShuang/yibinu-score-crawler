package wiki.zimo.scorecrawler.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import wiki.zimo.scorecrawler.domain.Visitor;

public interface VisitorDao extends JpaRepository<Visitor, Integer> {
}
