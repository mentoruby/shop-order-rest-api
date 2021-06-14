package online.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotFoundException extends RuntimeException {
	private static final long serialVersionUID = -4132515044091371668L;

	public NotFoundException() {
        super();
    }
    
    public NotFoundException(String message) {
        super(message);
    }
}
