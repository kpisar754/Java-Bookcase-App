package com.infoshare.bookcase.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public interface ErrorController extends org.springframework.boot.web.servlet.error.ErrorController {

    @SuppressWarnings("unused")
    String getErrorPath();
}
