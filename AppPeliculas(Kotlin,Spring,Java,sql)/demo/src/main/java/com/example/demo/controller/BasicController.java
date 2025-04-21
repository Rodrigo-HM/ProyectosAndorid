package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BasicController {
    @Autowired
    MovieService movieService;

    @RequestMapping("/")
    String index(){
        return "index";
    }

    @RequestMapping("/add")
    String add(){
        return "addMovie";
    }

    @PostMapping("/add")
    public String addMovie(@RequestParam String movie_name, ModelMap model) {
        Movie m = new Movie();
        m.setMovie_name(movie_name);
        movieService.addMovie(m);
        model.addAttribute("message", "La película '" + movie_name + "' ha sido añadida.");
        return "addMovie";
    }

}
