package com.agilesolutions.runescape.controller;

import com.agilesolutions.runescape.exception.BadRequestException;
import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.service.PlayerService;
import com.agilesolutions.runescape.utils.ErrorInfo;
import com.agilesolutions.runescape.model.Category;
import com.agilesolutions.runescape.service.LoggerManager;
import com.agilesolutions.runescape.service.CategoryService;
import com.agilesolutions.runescape.service.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/category")
public class CategoryCtrl {
    private LoggerManager logger = LoggerManager.getInstance();
    private String resource = "Category";

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private Utilities utilities;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Category>> findAll(@RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        this.utilities.logRequest("GET", "/category");
        return ResponseEntity
                .ok(this.categoryService.findAll(Optional.ofNullable(name), Optional.ofNullable(description)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Category> findOne(@PathVariable String id) throws ResourceNotFoundException {
        this.utilities.logRequest("GET", "/category/" + id);
        Category category = this.categoryService.findOne(id);

        return ResponseEntity.ok(category);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Category> create(@RequestBody LinkedHashMap reqBody) throws BadRequestException {
        this.utilities.logRequest("POST", "/category");
        String id = reqBody.getOrDefault("id", "").toString().toLowerCase().split(" ")[0];
        String name = reqBody.getOrDefault("name", "").toString();
        String description = reqBody.getOrDefault("description", "").toString();

        if(id.isEmpty()) {
            this.utilities.throwMissingParametersError(this.resource, "id");
        }
        if(name.isEmpty()) {
            name = id;
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.categoryService.save(new Category(id, name, description)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Category> put(@RequestBody LinkedHashMap reqBody, @PathVariable String id) throws ResourceNotFoundException {
        this.utilities.logRequest("PUT", "/category/" + id);
        String name = reqBody.getOrDefault("name", "").toString();
        String description = reqBody.getOrDefault("description", "").toString();

        return ResponseEntity.ok(this.categoryService.update(id, new Category(id, name, description)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable String id) {
        this.utilities.logRequest("DELETE", "/category/" + id);
        this.categoryService.delete(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{categoryId}/player/{playerId}")
    public ResponseEntity<Category> savePlayerScore(@RequestBody LinkedHashMap reqBody, @PathVariable String categoryId, @PathVariable Long playerId)
            throws ResourceNotFoundException, BadRequestException {
        this.utilities.logRequest("POST", "/category/" + categoryId + "/player/" + playerId);
        Integer level = (Integer) reqBody.get("level");
        Integer score = (Integer) reqBody.get("score");

        if (level == null) {
            this.utilities.throwMissingParametersError(this.resource, "level");
        }
        if (score == null) {
            this.utilities.throwMissingParametersError(this.resource, "score");
        }

        Player player = this.playerService.findOne(playerId);

        Category category = this.categoryService.savePlayerScore(categoryId, player, level, score);
        this.playerService.save(player);

        return ResponseEntity.ok(category);
    }

    // Exceptions Handling

    @ExceptionHandler({ResourceNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorInfo> ResourceNotFoundHandler(HttpServletRequest req, Exception e) {
        return this.utilities.generateError(this.resource, req, e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BadRequestException.class,
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