package firstweek.board.dto;

import firstweek.board.entity.Board;
import jakarta.persistence.Column;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String name;
    private String password;
    private String title;
    private String contents;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;


    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.name = board.getName();
        this.password = board.getPassword();
        this.title = board.getTitle();
        this.contents = board.getContents();
        this.createAt = board.getCreateAt();
        this.modifiedAt = board.getModifiedAt();
    }
}
