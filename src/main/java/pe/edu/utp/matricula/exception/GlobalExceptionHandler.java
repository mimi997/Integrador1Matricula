package pe.edu.utp.matricula.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(RecursoNoEncontradoException ex, Model model) {
        logger.error("Recurso no encontrado: {}", ex.getMessage());
        model.addAttribute("error", "El recurso que buscas no existe.");
        return "error/404";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        logger.error("Acceso denegado: {}", ex.getMessage());
        model.addAttribute("error", "No tienes permisos para acceder a esta página.");
        return "error/403";
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public String handleReglaNegocio(ReglaNegocioException ex, Model model) {
        logger.warn("Regla de negocio: {}", ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/500"; // Podría ser una vista de error genérica o redirect
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralError(Exception ex, Model model) {
        logger.error("Error inesperado", ex);
        model.addAttribute("error", "Ocurrió un error inesperado. Por favor contacta a soporte.");
        return "error/500";
    }
}
