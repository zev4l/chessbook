package domain;


import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ChessPosition implements Serializable {
    private int row;  // 0:7
    private int col; // 0:7

    public ChessPosition() {

    }

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append((char) ('a' + col)).append(row + 1);

        return sb.toString();
    }

    public boolean equals(ChessPosition other) {
        return other.getRow() == this.getRow() && other.getCol() == this.getCol();
    }


    // TODO: Talvez criar um toString que converta para posição tradicional
}
