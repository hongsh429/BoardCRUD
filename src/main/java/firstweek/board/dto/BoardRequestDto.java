package firstweek.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestDto {

    private String name;
    private String password;
    private String title;
    private String contents;
}
