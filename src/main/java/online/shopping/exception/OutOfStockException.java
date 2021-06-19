package online.shopping.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OutOfStockException extends RuntimeException {
	private static final long serialVersionUID = -6110930734541141099L;

	public OutOfStockException() {
        super();
    }
    
    public OutOfStockException(String message) {
        super(message);
    }
}
