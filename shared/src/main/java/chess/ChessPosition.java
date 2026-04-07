package chess;

public record ChessPosition(int row, int col){
    public int getRow(){
        return row;
    }
    public int getColumn(){
        return col;
    }

    public static ChessPosition fromString(String pos){
        if (pos == null || pos.length() != 2){
            throw new IllegalArgumentException("Invalid chess move");

        }

        char oneChar = pos.charAt(0);
        char twoNum = pos.charAt(1);

        int col = oneChar - 'a' + 1;
        int row = Character.getNumericValue(twoNum);
        if (col < 1 || col > 8 || row < 1 || row > 8){
            throw new IllegalArgumentException("bad input");

        }
        return new ChessPosition(row, col);
    }
}
