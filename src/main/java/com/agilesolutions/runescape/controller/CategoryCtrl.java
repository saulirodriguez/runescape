package com.agilesolutions.runescape.controller;

import com.agilesolutions.runescape.exception.BadRequestException;
import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.ErrorInfo;
import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.model.PlayerCategory;
import com.agilesolutions.runescape.service.LoggerManager;
import com.agilesolutions.runescape.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryCtrl {
    private LoggerManager logger = LoggerManager.getInstance();
    private String resource = "Category";

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findAll(@RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        return ResponseEntity
                .ok(this.categoryService.findAll(Optional.ofNullable(name), Optional.ofNullable(description)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Category> findOne(@PathVariable Long id) throws ResourceNotFoundException {
        Category todo = this.categoryService.findOne(id);
        if (todo == null) {
            throw new ResourceNotFoundException(this.resource);
        }

        return ResponseEntity.ok(todo);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Category> create(@RequestBody Category category) throws BadRequestException {
        if (category.getName() == null || category.getName().isEmpty()) {
            throw new BadRequestException(this.resource, "Name Empty ");
        }

        if(category.getId() != null) {
            category.setId(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.create(category));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Category> put(@RequestBody Category category, @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.categoryService.update(id, category));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id) {
        this.categoryService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/player/{playerId}")
    public ResponseEntity<Category> addPlayer(@RequestBody PlayerCategory playerCategory, @PathVariable Long categoryId, @PathVariable Long playerId) throws BadRequestException {
        if (playerCategory.getLevel() == null) {
            throw new BadRequestException(this.resource, "Level Empty");
        }
        if (playerCategory.getScore() == null) {
            throw new BadRequestException(this.resource, "Score Empty");
        }

        return ResponseEntity.ok(this.categoryService.addPlayer(categoryId, playerId, playerCategory));
    }

    // Exceptions Handling

    @ExceptionHandler({ResourceNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorInfo> ResourceNotFoundHandler(HttpServletRequest req, Exception e) {
        this.logError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorInfo> BadRequestHandler(HttpServletRequest req, Exception e) {
        this.logError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> GenericErrorHandler(HttpServletRequest req, Exception e) {
        this.logError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Helper Methods

    private void logError(HttpServletRequest req, Exception e) {
        String reqUri = req.getMethod() + ": " + req.getRequestURI();
        logger.log(reqUri + ":\n" + e.toString(), LoggerManager.Level.ERROR);
    }
}