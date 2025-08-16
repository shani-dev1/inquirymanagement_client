package ClientServer;

import java.io.Serializable;

public class ResponseData implements Serializable {
    private static final long serialVersionUID = 6634007134381918066L;

    private ResponseStatus status;
    private String message;
    private Object result;

    public ResponseData(ResponseStatus status, String message, Object result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
