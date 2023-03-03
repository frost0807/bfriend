package com.frost.bfriend.exception;

import com.frost.bfriend.exception.matchpost.ForbiddenMatchPostException;
import com.frost.bfriend.exception.matchpost.ForbiddenReplyException;
import com.frost.bfriend.exception.matchpost.MatchPostNotFoundException;
import com.frost.bfriend.exception.matchpost.ReplyNotFoundException;
import com.frost.bfriend.exception.user.*;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class BFriendExceptionHandler {

    public void writeErrorLog(Exception e) {
        log.error("{} - {}", e.getClass().getSimpleName(), e.getLocalizedMessage());
    }

    public ErrorMsg getErrorMessage(Exception e) {
        return new ErrorMsg(e.getClass().getSimpleName(), e.getLocalizedMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorMsg handleConstraintViolationException(ConstraintViolationException e) {
        writeErrorLog(e);
        return new ErrorMsg(e.getClass().getSimpleName(), e.getLocalizedMessage().split(":")[1]);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicatedEmailException.class)
    public ErrorMsg handleDuplicatedEmailException(DuplicatedEmailException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicatedPhoneException.class)
    public ErrorMsg handleDuplicatedPhoneException(DuplicatedPhoneException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CertificationCodeNotFoundException.class)
    public ErrorMsg handleCertificationCodeNotFoundException(CertificationCodeNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IncorrectCertificationCodeException.class)
    public ErrorMsg handleIncorrectCertificationCodeException(IncorrectCertificationCodeException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FailedToSendSmsException.class)
    public ErrorMsg handleFailedToSendSmsException(FailedToSendSmsException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CertificationIdentifierNotFoundException.class)
    public ErrorMsg handleCertificationIdentifierNotFoundException(CertificationIdentifierNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CertificationIdentifierMismatchException.class)
    public ErrorMsg handleCertificationIdentifierMismatchException(CertificationIdentifierMismatchException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NotLoggedInException.class)
    public ErrorMsg handleNotLoggedInException(NotLoggedInException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorMsg handleUserNotFoundException(UserNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorMsg handleExpiredJwtException(ExpiredJwtException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CookieNotFoundException.class)
    public ErrorMsg handleCookieNotFoundException(CookieNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(WrongPasswordException.class)
    public ErrorMsg handleWrongPasswordException(WrongPasswordException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AlreadyMyPasswordException.class)
    public ErrorMsg handleAlreadyMyPasswordException(AlreadyMyPasswordException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MatchPostNotFoundException.class)
    public ErrorMsg handleMatchPostNotFoundException(MatchPostNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenMatchPostException.class)
    public ErrorMsg handleForbiddenMatchPostException(ForbiddenMatchPostException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReplyNotFoundException.class)
    public ErrorMsg handleParentReplyNotFoundException(ReplyNotFoundException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(ForbiddenReplyException.class)
    public ErrorMsg handleForbiddenReplyException(ForbiddenReplyException e) {
        writeErrorLog(e);
        return getErrorMessage(e);
    }
}