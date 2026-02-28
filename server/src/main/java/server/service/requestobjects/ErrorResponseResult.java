package server.service.requestobjects;

public record ErrorResponseResult(String message) {
    public String getMessage(){
        return message;
    }
}
