package br.com.criandoapi.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class ControllerAdvice {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "O arquivo é muito grande! O tamanho máximo permitido é 10MB.");
		return "redirect:/uploadStatus"; 
	}
}