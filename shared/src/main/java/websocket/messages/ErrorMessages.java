package websocket.messages;


public class ErrorMessages extends ServerMessage {
    private final String errorMessage;

    public ErrorMessages(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
    public String getErrorText(){
        return errorMessage;
    }
}
