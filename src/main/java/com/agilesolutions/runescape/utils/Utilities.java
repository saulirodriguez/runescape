package com.agilesolutions.runescape.utils;

import com.agilesolutions.runescape.service.LoggerManager;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class Utilities {
    private LoggerManager logger = LoggerManager.getInstance();

    public void logRequestError(HttpServletRequest req, Exception e) {
        String reqUri = req.getMethod() + ": " + req.getRequestURI();
        logger.log(reqUri + ":\n" + e.toString(), LoggerManager.Level.ERROR);
    }
}
