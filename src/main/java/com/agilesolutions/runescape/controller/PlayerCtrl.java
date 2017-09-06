package com.agilesolutions.runescape.controller;

import com.agilesolutions.runescape.utils.ErrorInfo;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.service.PlayerService;
import com.agilesolutions.runescape.service.Utilities;
import com.agilesolutions.runescape.exception.BadRequestException;
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
import java.util.Optional;

@RestController
@RequestMapping("/player")
public class PlayerCtrl {
    private String resource = "Player";

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Utilities utilities;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Player>> findAll(@RequestParam(required = false) String name) {
        this.utilities.logRequest("GET", "/player");
        return ResponseEntity
                .ok(this.playerService.findAll(Optional.ofNullable(name)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Player> findOne(@PathVariable Long id) throws ResourceNotFoundException {
        this.utilities.logRequest("GET", "/player/" + id);

        Player player = this.playerService.findOne(id);
        if (player == null) {
            throw new ResourceNotFoundException(this.resource);
        }

        return ResponseEntity.ok(player);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Player> create(@RequestBody LinkedHashMap reqBody) throws BadRequestException {
        this.utilities.logRequest("POST", "/player");
        String name = reqBody.getOrDefault("name", "").toString();

        if (name.isEmpty()) {
            this.utilities.throwMissingParametersError(this.resource, "name");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.playerService.save(new Player(name)));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Player> put(@RequestBody LinkedHashMap reqBody, @PathVariable Long id) throws ResourceNotFoundException {
        this.utilities.logRequest("PUT", "/player/" + id);
        String name = reqBody.getOrDefault("name", "").toString();

        return ResponseEntity.ok(this.playerService.update(id, new Player(name)));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id) {
        this.utilities.logRequest("DELETE", "/player/" + id);
        this.playerService.delete(id);
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