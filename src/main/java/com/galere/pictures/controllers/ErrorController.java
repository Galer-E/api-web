package com.galere.pictures.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <b>
 * 	Controller offrant une route pour gérer les erreurs internes.
 * </b>
 * 
 * @author Ilias HATTANE
 * @version 1.0
 *
 */
@ControllerAdvice
public class ErrorController {

	private final static Logger log = LoggerFactory.getLogger(ErrorController.class);

	/**
	 * <b> Méthide appelée en cas d'erreur interne du server. </b>
	 * 
	 * @param throwable Erreur apparut.
	 * @param model Attributs destinés à la page web.
	 * @return La page d'erreur.
	 */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Throwable throwable, final Model model) {
    	log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}
