package com.example.bookservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @GetMapping("/recommended")
    public String getBooks(){
        return "this is Books";
    }

}
