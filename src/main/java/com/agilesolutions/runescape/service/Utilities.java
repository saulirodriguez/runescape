package com.agilesolutions.runescape.service;

import com.agilesolutions.runescape.exception.BadRequestException;
import com.agilesolutions.runescape.utils.ErrorInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class Utilities {
    private LoggerManager logger = LoggerManager.getInstance();

    public void logRequest(HttpServletRequest req, Exception e) {
        this.logRequest(req.getMethod(), req.getRequestURI());
        logger.log(e.toString(), LoggerManager.Level.ERROR);
    }

    public void logRequest(String method, String uri) {
        logger.log(method + ": " + uri);
    }

    public void throwMissingParametersError(String resource, String parameter) throws BadRequestException {
        throw new BadRequestException(resource, "Required Field: " + parameter);
    }

    public ResponseEntity<ErrorInfo> generateError(String resource, HttpServletRequest req, Exception e, HttpStatus status) {
        this.logRequest(req, e);
        e.printStackTrace(System.out);
        ErrorInfo error = new ErrorInfo(resource, e.getMessage(), req.getMethod(), req.getRequestURI());
        return new ResponseEntity<ErrorInfo>(error, status);
    }
}
