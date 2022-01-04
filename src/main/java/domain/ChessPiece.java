package domain;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ChessPiece implements Serializable {
    private Color color;
    private ChessPieceKind type;

    public ChessPiece() {};

    public ChessPiece(ChessPieceKind type, Color color) {
        this.type = type;
        this.color = color;
    }

    // Setters
    // Necessário para promoção de peão
    public void setType(ChessPieceKind type) {
        this.type = type;
    }

    // Getters

    public Color getColor() {
        return color;
    }

    public ChessPieceKind getChessPieceKind() {
        return type;
    }

    public boolean equals(ChessPiece other) {
        if (this == other) return true;
        return color == other.color && type == other.type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Substituir procedimento por HashMap talvez
        switch (getChessPieceKind()){
            case KING:
                sb.append("K");
                break;
            case QUEEN:
                sb.append("Q");
                break;
            case BISHOP:
                sb.append("B");
                break;
            case ROOK:
                sb.append("R");
                break;
            case KNIGHT:
                /* De acordo com as regras de notação algébrica */
                sb.append("N");
                break;
            case PAWN:
                sb.append("P");
                break;
        }

        switch (getColor()) {
            case WHITE:
                sb.append("w");
                break;
            case BLACK:
                sb.append("b");
                break;
        }

        return sb.toString();
    }

}
