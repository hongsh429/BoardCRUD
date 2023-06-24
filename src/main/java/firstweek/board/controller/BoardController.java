package firstweek.board.controller;


import firstweek.board.dto.BoardRequestDto;
import firstweek.board.dto.BoardResponseDto;
import firstweek.board.dto.BoardSearch;
import firstweek.board.entity.Board;
import firstweek.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public BoardResponseDto createBoard(@RequestBody BoardRequestDto requestDto) {
        return boardService.createBoard(requestDto);
    }

    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/board/{id}")
    public BoardSearch findBoardById(@PathVariable Long id) {
        return boardService.findBoardById(id);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto editBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.editBoard(id, requestDto);
    }

    @DeleteMapping("/board/{id}")
    public String deleteBoard(@PathVariable Long id, @RequestBody Map<String, Object> map) {
        String result;
        result = boardService.deleteBoard(id, map.get("password"));
        return result;
    }
}
