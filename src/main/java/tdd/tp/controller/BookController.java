package tdd.tp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import tdd.tp.entity.Book;

@Controller
public class BookController {

    // Render the form
    @RequestMapping(value="/",method= RequestMethod.GET)
    public void get()
    {
        throw new RuntimeException("Not Implemented");
    }

    @RequestMapping(value="/",method=RequestMethod.POST)
    public void create(Book book)
    {

    }
}
