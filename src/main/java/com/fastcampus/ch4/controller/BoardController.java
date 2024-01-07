package com.fastcampus.ch4.controller;

import com.fastcampus.ch4.domain.*;
import com.fastcampus.ch4.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.*;

import javax.servlet.http.*;
import java.time.*;
import java.util.*;

@Controller
@RequestMapping("/board")
public class BoardController {
    @Autowired
    BoardService boardService;

    @PostMapping("/modify")
    public String modify(Integer page, Integer pageSize, BoardDto boardDto,Model m,HttpSession session,RedirectAttributes rattr) {

        String writer = (String)session.getAttribute("id");

        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.modify(boardDto); // insert

            if(rowCnt!=1)
                throw new Exception("Modify failed");

//            m.addAttribute(boardDto);

            m.addAttribute("page",page);
            m.addAttribute("pageSize",pageSize);

            rattr.addFlashAttribute("msg","MOD_OK");
            return "redirect:/board/list";

        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
            m.addAttribute(boardDto);
            m.addAttribute("msg","MOD_ERR");
            return "board";
        }
    }

    @PostMapping("/write")
    public String write(BoardDto boardDto,Model m,HttpSession session,RedirectAttributes rattr) {

//        System.out.println("boardDto = " + boardDto);
//        System.out.println("rowCnt = " + rowCnt);

        String writer = (String)session.getAttribute("id");

        System.out.println("writer = " + writer);
        boardDto.setWriter(writer);

        try {
            int rowCnt = boardService.write(boardDto); // insert

            if(rowCnt!=1)
                throw new Exception("Write failed");

//            m.addAttribute(boardDto);

            rattr.addFlashAttribute("msg","WRT_OK");
            return "redirect:/board/list";

        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
            m.addAttribute(boardDto);
            m.addAttribute("msg","WRT_ERR");
            return "board";
        }
    }

    @GetMapping("/write")
    public String write(Model m){

        m.addAttribute("mode","new");
        return "board"; // 읽기와 쓰기에 사용. 쓰기에 사용할때는 mode =new
    }

    @PostMapping("/remove")
    public String remove(Integer bno,Integer page, Integer pageSize, Model m,HttpSession session,RedirectAttributes rattr){

        String writer = (String)session.getAttribute("id");

        try {
           int rowCnt =  boardService.remove(bno,writer);

            m.addAttribute("page",page);
            m.addAttribute("pageSize",pageSize);

            if(rowCnt!=1)
                throw new Exception("board remove error");

               rattr.addFlashAttribute("msg","DEL_OK");

        } catch (Exception e) {
            e.printStackTrace();
            rattr.addFlashAttribute("msg","DEL_ERR");
        }

        return "redirect:/board/list";
    }

    @GetMapping("/read")
    public String read(Integer bno,Integer page, Integer pageSize,Model m){
        try {
            BoardDto boardDto = boardService.read(bno);
//            m.addAttribute("boardDto",boardDto);// 아래 문장과 동일
            m.addAttribute(boardDto); // 타입의 첫글자를 이름으로 한다.
            m.addAttribute("page",page);
            m.addAttribute("pageSize",pageSize);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return "board";
    }

    @GetMapping("/list")
//    public String list(@RequestParam(defaultValue = "1") Integer page,@RequestParam(defaultValue = "10") Integer pageSize,
//                       String option, String keyword, Model m, HttpServletRequest request) {
    public String list(@ModelAttribute SearchCondition sc, Model m, HttpServletRequest request){

        if(!loginCheck(request))
            return "redirect:/login/login?toURL="+request.getRequestURL();  // 로그인을 안했으면 로그인 화면으로 이동

        try {
            int totalCnt = boardService.getSearchResultCnt(sc);
            m.addAttribute("totalCnt",totalCnt);

            PageHandler pageHandler = new PageHandler(totalCnt,sc);

            List<BoardDto> list = boardService.getSearchResultPage(sc);
            m.addAttribute("list",list);
            m.addAttribute("ph",pageHandler);

            Instant startOfToday = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant();
            m.addAttribute("startOfToday",startOfToday.toEpochMilli());
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("msg","LIST_ERR");
            m.addAttribute("totalCnt",0);
            throw new RuntimeException(e);
        }

        return "boardList"; // 로그인을 한 상태이면, 게시판 화면으로 이동
    }

    private boolean loginCheck(HttpServletRequest request) {
        // 1. 세션을 얻어서
        HttpSession session = request.getSession();
        // 2. 세션에 id가 있는지 확인, 있으면 true를 반환
        return session.getAttribute("id")!=null;
    }
}