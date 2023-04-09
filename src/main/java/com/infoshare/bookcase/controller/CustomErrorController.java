package com.infoshare.bookcase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    private static final String ERROR_MAPPING = "/error";

    @RequestMapping(value = ERROR_MAPPING)
    public ModelAndView handleError(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("error");

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");

        String errorMessage = "An unknown error has occurred.";

        if (statusCode != null) {
            switch (statusCode) {
                case 403 -> errorMessage = "You do not have permissions to do this.";
                case 404 -> errorMessage = "The requested resource was not found.";
                case 500 -> errorMessage = "An internal server error has occurred.";
            }
        }

        mav.addObject("errorMessage", errorMessage);
        mav.addObject("exception", exception);
        mav.addObject("statusCode", statusCode);

        return mav;
    }

    @Override
    public String getErrorPath() {
        return ERROR_MAPPING;
    }
}
