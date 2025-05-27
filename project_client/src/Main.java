import ClientServer.InquiryManagerClient;
public class Main {
    public static void main(String[] args) {
        InquiryManagerClient inquiryManagerClient = new InquiryManagerClient("localhost", 6000);
        inquiryManagerClient.execute();
    }
}