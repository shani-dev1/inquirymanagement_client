package ClientServer;

import java.io.Serializable;
import java.util.List;

public class RequestData implements Serializable {
    private static final long serialVersionUID = 6634007134381918066L;

    private InquiryManagerActions action;
    private List<Object> parameters;

    public RequestData(InquiryManagerActions action, List<Object> parameters) {
        this.action = action;
        this.parameters = parameters;
    }

    public InquiryManagerActions getAction() {
        return action;
    }

    public List<Object> getParameters() {
        return parameters;
    }
}
