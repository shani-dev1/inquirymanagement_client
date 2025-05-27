package ClientServer;
import java.io.Serializable;
import java.util.List;
public class RequestData implements Serializable {
    private static final long serialVersionUID = 6634007134381918066L;
    InquiryManagerActions action;
    List<Object> parameters;
    public RequestData(InquiryManagerActions inquiryManagerActions , List<Object> parameters){
       action = inquiryManagerActions;
       this.parameters=parameters;
    }
}
