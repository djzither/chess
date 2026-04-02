package websocket.commands;

import java.util.Objects;

public class MoveCommand extends UserGameCommand{
    private final String from;
    private final String to;

    public MoveCommand(String authToken, Integer gameId, String from, String to){
        super(CommandType.MAKE_MOVE, authToken, gameId);
        this.from = from;
        this.to = to;

    }
    public String getFrom(){
        return from;
    }
    public String getTo(){
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MoveCommand that = (MoveCommand) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), from, to);
    }
}
