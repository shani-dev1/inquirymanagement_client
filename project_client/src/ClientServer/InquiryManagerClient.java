package ClientServer;

import Data.*;
import Exceptions.InquiryRunTimeException;
import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class InquiryManagerClient {
    private Socket connectToServer;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public InquiryManagerClient(String serverAddress, int serverPort) {
        try {
            connectToServer = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(connectToServer.getOutputStream());
            in = new ObjectInputStream(connectToServer.getInputStream());
            System.out.println("Connected to server successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            try {
                System.out.println("Choose an action:");
                System.out.println("1 - All inquiries");
                System.out.println("2 - Add inquiry");
                System.out.println("3 - Get inquiry status");
                System.out.println("4 - Cancel inquiry");
                System.out.println("5 - Add representative");
                System.out.println("6 - Delete representative");
                System.out.println("7 - Is representative active?");
                System.out.println("8 - Get representative inquiries");
                System.out.println("9 - Get representative name by inquiry code");
                System.out.println("10 - Get active representatives");
                System.out.println("0 - Exit");

                String choice = scanner.nextLine();
                if ("0".equals(choice)) {
                    running = false;
                    System.out.println("Exiting client...");
                    break;
                }

                RequestData request = createRequest(choice);
                if (request == null) {
                    continue;
                }

                out.writeObject(request);
                out.flush();

                Object response = in.readObject();
                if (response instanceof ResponseData) {
                    ResponseData responseObj = (ResponseData) response;
                    System.out.println("Server response: " + responseObj.getMessage());
                    System.out.println("Server status: " + responseObj.getStatus());
                    if (responseObj.getResult() != null) {
                        System.out.println("Server result: " + responseObj.getResult());
                    }
                } else {
                    System.out.println("Invalid response from server.");
                }

            } catch (IOException e) {
                System.out.println("Error communicating with server: " + e.getMessage());
                running = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                running = false;
            }
        }

        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (connectToServer != null) connectToServer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RequestData createRequest(String choice) {
        switch (choice) {
            case "1":
                return allInquiries();
            case "2":
                return addInquiry();
            case "3":
                return getInquiryStatus();
            case "4":
                return cancelInquiry();
            case "5":
                return addRepresentative();
            case "6":
                return deleteRepresentative();
            case "7":
                return isRepresentativeActive();
            case "8":
                return getRepresentativeInquiries();
            case "9":
                return getRepresentativeNameByInquiryCode();
            case "10":
                return getActiveRepresentatives();
            default:
                System.out.println("Invalid choice, please try again.");
                return null;
        }
    }

    private RequestData allInquiries() {
        return new RequestData(InquiryManagerActions.ALL_INQUIRY, null);
    }

    private RequestData addInquiry() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter 1 for Question, 2 for Request, 3 for Complaint, or any other key to exit: ");

        int code;
        try {
            code = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return null;
        }

        Inquiry inquiry;
        switch (code) {
            case 1:
                inquiry = new Question();
                inquiry.setClassName("Question");
                break;
            case 2:
                inquiry = new Request();
                inquiry.setClassName("Request");
                break;
            case 3:
                inquiry = new Complaint();
                inquiry.setClassName("Complaint");
                break;
            default:
                System.out.println("Exiting inquiry creation.");
                return null;
        }

        inquiry.fillDataByUser();
        return new RequestData(InquiryManagerActions.ADD_INQUIRY, List.of(inquiry));
    }

    private RequestData getInquiryStatus() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter inquiry code: ");
        String code = scanner.nextLine();
        return new RequestData(InquiryManagerActions.GET_INQUIRY_STATUS, List.of(code));
    }

    private RequestData cancelInquiry() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter inquiry code to cancel: ");
        String code = scanner.nextLine();
        return new RequestData(InquiryManagerActions.CANCEL_INQUIRY, List.of(code));
    }

    private RequestData addRepresentative() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter representative name: ");
        String name = scanner.nextLine();
        System.out.print("Enter representative ID number: ");
        int id = Integer.parseInt(scanner.nextLine());
        return new RequestData(InquiryManagerActions.ADD_REPRESENTATIVE, List.of(id, name));
    }

    private RequestData deleteRepresentative() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter representative ID number to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        return new RequestData(InquiryManagerActions.DELETE_REPRESENTATIVE, List.of(id));
    }

    private RequestData isRepresentativeActive() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter representative ID to check: ");
        int id = Integer.parseInt(scanner.nextLine());
        return new RequestData(InquiryManagerActions.IS_REPRESENTATIVE_ACTIVE, List.of(id));
    }

    private RequestData getRepresentativeInquiries() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter representative ID to get inquiries: ");
        int id = Integer.parseInt(scanner.nextLine());
        return new RequestData(InquiryManagerActions.GET_REPRESENTATIVE_INQUIRIES, List.of(id));
    }

    private RequestData getRepresentativeNameByInquiryCode() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter inquiry code to get representative name: ");
        String code = scanner.nextLine();
        return new RequestData(InquiryManagerActions.GET_REPRESENTATIVE_NAME_BY_INQUIRY_CODE, List.of(code));
    }

    private RequestData getActiveRepresentatives() {
        return new RequestData(InquiryManagerActions.GET_ACTIVE_REPRESENTATIVES, null);
    }
}