package com.whiterabbit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAjouterArticleException  extends RuntimeException {
    public ImpossibleAjouterArticleException(String message) {
        super(message);
    }
}
