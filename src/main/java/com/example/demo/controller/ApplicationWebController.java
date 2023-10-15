package com.example.demo.controller;

import com.example.demo.Service.ApplicationService;
import com.example.demo.config.ApplicationReviewStates;
import com.example.demo.model.Application;
import com.example.demo.model.ApplicationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ApplicationWebController {

    private final ApplicationService applicationService;


    @GetMapping
    public String hi(Model model){
        List<Application> applications = applicationService.getAll();
        model.addAttribute("apps", applications);
        model.addAttribute("name", "bob");
        return "home";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable long id, Model model) {
        ApplicationDto applicationDto = applicationService.getById(id);
        model.addAttribute("app", applicationDto);
        return "app";
    }


    @GetMapping("/addApplication")
    public String add() {
        return "add";
    }

    @PostMapping("/add-application")
    public String addApplication() {
        Application application = Application.builder()
                .applicationReviewStates(ApplicationReviewStates.START)
                .date(new Date())
                .build();
        applicationService.save(application);
        return "redirect:/";
    }

    @GetMapping("/{id}/{event}")
    public String updateStatus(@PathVariable long id, @PathVariable String event) {
        applicationService.updateState(id, event);
        return "redirect:/";
    }

}
