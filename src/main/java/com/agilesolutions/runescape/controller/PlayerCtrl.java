package com.agilesolutions.runescape.controller;

import com.agilesolutions.runescape.exception.BadRequestException;
import com.agilesolutions.runescape.exception.ResourceNotFoundException;
import com.agilesolutions.runescape.utils.ErrorInfo;
import com.agilesolutions.runescape.model.Player;
import com.agilesolutions.runescape.service.LoggerManager;
import com.agilesolutions.runescape.service.PlayerService;
import com.agilesolutions.runescape.utils.Utilities;
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
@RequestMapping("/player")
public class PlayerCtrl {
    private LoggerManager logger = LoggerManager.getInstance();
    private String resource = "Player";

    @Autowired
    private PlayerService playerService;
    @Autowired
    private Utilities utilities;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Player>> findAll(@RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        return ResponseEntity
                .ok(this.playerService.findAll(Optional.ofNullable(name), Optional.ofNullable(description)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Player> findOne(@PathVariable Long id) throws ResourceNotFoundException {
        Player todo = this.playerService.findOne(id);
        if (todo == null) {
            throw new ResourceNotFoundException(this.resource);
        }

        return ResponseEntity.ok(todo);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Player> create(@RequestBody Player player) throws BadRequestException {
        if (player.getFirstname() == null || player.getFirstname().isEmpty()) {
            throw new BadRequestException(this.resource, "Firstname Empty ");
        }

        if(player.getId() != null) {
            player.setId(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.playerService.save(player));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<Player> put(@RequestBody Player player, @PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(this.playerService.update(id, player));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void delete(@PathVariable Long id) {
        this.playerService.delete(id);
    }

    // Exceptions Handling

    @ExceptionHandler({ResourceNotFoundException.class, EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorInfo> ResourceNotFoundHandler(HttpServletRequest req, Exception e) {
        this.utilities.logRequestError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public ResponseEntity<ErrorInfo> BadRequestHandler(HttpServletRequest req, Exception e) {
        this.utilities.logRequestError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> GenericErrorHandler(HttpServletRequest req, Exception e) {
        this.utilities.logRequestError(req, e);
        ErrorInfo error = new ErrorInfo(this.resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }
}