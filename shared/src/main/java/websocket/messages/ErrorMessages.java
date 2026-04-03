package websocket.messages;


public class ErrorMessages extends ServerMessage {
    private final String errorText;

    public ErrorMessages(String errorText){
        super(ServerMessageType.ERROR);
        this.errorText = errorText;
    }
    public String getErrorText(){
        return errorText;
    }
}
