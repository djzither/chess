package chess;

public record ChessPosition(int row, int col){
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return col;
    }
}
