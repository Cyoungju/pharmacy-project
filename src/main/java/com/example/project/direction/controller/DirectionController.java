package com.example.project.direction.controller;

import com.example.project.direction.entity.Direction;
import com.example.project.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;


@RequiredArgsConstructor
@Slf4j
@Controller
public class DirectionController {
    private final DirectionService directionService;


    @GetMapping("/dir/{encodingId}")
    public String searchDirection(@PathVariable("encodingId") String encodingId){
         String result = directionService.findDirectionUrlById(encodingId);

        log.info("[DirectionController searchirection] direction url : {}", result);

        return "redirect:" +result;
    }
}
