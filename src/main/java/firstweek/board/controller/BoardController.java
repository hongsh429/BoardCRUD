package firstweek.board.controller;


import firstweek.board.dto.BoardRequestDto;
import firstweek.board.dto.BoardResponseDto;
import firstweek.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class BoardController {

    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<BoardResponseDto> createBoard(@RequestBody BoardRequestDto requestDto) {
        return new ResponseEntity<BoardResponseDto>(boardService.createBoard(requestDto), HttpStatus.OK);
    }

    @GetMapping("/boards")
    public List<BoardResponseDto> getBoards() {
        return boardService.getBoards();
    }

    @GetMapping("/board/{id}")
    public BoardResponseDto findBoardById(@PathVariable Long id) {
        return boardService.findBoardById(id);
    }

    @PutMapping("/board/{id}")
    public BoardResponseDto editBoard(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        return boardService.editBoard(id, requestDto);
    }

    @DeleteMapping("/board/{id}")
    public Map<String, Object> deleteBoard(@PathVariable Long id) {

        return boardService.deleteBoard(id);
    }
}
