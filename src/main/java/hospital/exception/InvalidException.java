package hospital.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidException extends Exception{
    public InvalidException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "InvalidException{}";
    }
}
