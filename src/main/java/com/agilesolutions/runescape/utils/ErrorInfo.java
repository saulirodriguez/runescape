package com.agilesolutions.runescape.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo {
    private String controller;
    private String message;
    private String method;
    private String uri;
}
