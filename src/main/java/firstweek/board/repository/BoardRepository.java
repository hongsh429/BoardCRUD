package firstweek.board.repository;


import firstweek.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    // 조회를 위한 Query method
    List<Board> findAllByOrderByCreateAtDesc();
}
