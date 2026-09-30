package com.library;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestControllerAdvice class ApiExceptionHandler { @ExceptionHandler(NoSuchElementException.class) ResponseEntity<?> notFound(NoSuchElementException e){return ResponseEntity.status(404).body(Map.of("error","NOT_FOUND","message",e.getMessage()));} @ExceptionHandler(Exception.class) ResponseEntity<?> error(Exception e){return ResponseEntity.status(500).body(Map.of("error","INTERNAL_ERROR","message","Request could not be completed"));} }
