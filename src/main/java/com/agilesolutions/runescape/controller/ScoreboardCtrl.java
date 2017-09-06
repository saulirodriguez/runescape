package com.agilesolutions.runescape.controller;

import com.agilesolutions.runescape.service.CategoryService;
import com.agilesolutions.runescape.service.Utilities;
import com.agilesolutions.runescape.utils.ErrorInfo;
import com.agilesolutions.runescape.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/scoreboard")
public class ScoreboardCtrl {
    private String resource = "Scoreboard";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private Utilities utilities;

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<List<LinkedHashMap>> findAll(@PathVariable String id) {
        this.utilities.logRequest("GET", "/scoreboard/" + id);

        return ResponseEntity
                .ok(this.categoryService.findTop(id));
    }

    // Exceptions Handling
    @ExceptionHandler({ResourceNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorInfo> ResourceNotFoundHandler(HttpServletRequest req, Exception e) {
        return this.utilities.generateError(this.resource, req, e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class,
            ClassCastException.class
    })
    public ResponseEntity<ErrorInfo> BadRequestHandler(HttpServletRequest req, Exception e) {
        return this.utilities.generateError(this.resource, req, e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> GenericErrorHandler(HttpServletRequest req, Exception e) {
        return this.utilities.generateError(this.resource, req, e, HttpStatus.SERVICE_UNAVAILABLE);
    }
}