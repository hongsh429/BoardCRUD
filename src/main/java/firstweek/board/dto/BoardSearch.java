package firstweek.board.dto;

import firstweek.board.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardSearch {
    private String name;
    private String title;
    private String contents;
    private LocalDateTime createAt;


    public BoardSearch(Board board) {
        this.name = board.getName();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createAt = board.getCreateAt();
    }
}
