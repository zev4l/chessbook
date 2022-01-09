package domain;

import java.util.HashMap;
import java.util.List;

public class ChessBoard {
    private ChessPiece[][] board;

    public ChessBoard() {
        resetBoard();
    }

    public ChessPiece get(int row, int col) {
        return this.board[row][col];
    }

    public void set(int row, int col, ChessPiece piece) {
        this.board[row][col] = piece;
    }

    // TODO: talvez alterar para initialize
    public void resetBoard() {
        board = new ChessPiece[8][8];
        // White pieces
        // Rooks
        set(0, 0, new ChessPiece(ChessPieceKind.ROOK, Color.WHITE));
        set(0, 7, new ChessPiece(ChessPieceKind.ROOK, Color.WHITE));

        // Knights
        set(0, 1, new ChessPiece(ChessPieceKind.KNIGHT, Color.WHITE));
        set(0, 6, new ChessPiece(ChessPieceKind.KNIGHT, Color.WHITE));

        // Bishops
        set(0, 2, new ChessPiece(ChessPieceKind.BISHOP, Color.WHITE));
        set(0, 5, new ChessPiece(ChessPieceKind.BISHOP, Color.WHITE));

        // King and Queen
        set(0, 3, new ChessPiece(ChessPieceKind.QUEEN, Color.WHITE));
        set(0, 4, new ChessPiece(ChessPieceKind.KING, Color.WHITE));

        // Pawns
        for (int i = 0; i < 8; i++) {
            set(1, i, new ChessPiece(ChessPieceKind.PAWN, Color.WHITE));
        }

        // Black pieces
        // Rooks
        set(7, 0, new ChessPiece(ChessPieceKind.ROOK, Color.BLACK));
        set(7, 7, new ChessPiece(ChessPieceKind.ROOK, Color.BLACK));

        // Knights
        set(7, 1, new ChessPiece(ChessPieceKind.KNIGHT, Color.BLACK));
        set(7, 6, new ChessPiece(ChessPieceKind.KNIGHT, Color.BLACK));

        // Bishops
        set(7, 2, new ChessPiece(ChessPieceKind.BISHOP, Color.BLACK));
        set(7, 5, new ChessPiece(ChessPieceKind.BISHOP, Color.BLACK));

        // King and Queen
        set(7, 3, new ChessPiece(ChessPieceKind.QUEEN, Color.BLACK));
        set(7, 4, new ChessPiece(ChessPieceKind.KING, Color.BLACK));

        // Pawns
        for (int i = 0; i < 8; i++) {
            set(6, i, new ChessPiece(ChessPieceKind.PAWN, Color.BLACK));
        }

    }

    public void update(ChessMove move) throws IllegalMoveException {
        ChessPosition origin = move.getOrigin();
        ChessPosition destination = move.getDestination();

        if (checkValidity(move)) {
            // In case it's a capture
            if (isCapture(move)) {
                move.setCapture(get(destination.getRow(), destination.getCol()));
            }

            // Set new piece at new position
            set(destination.getRow(), destination.getCol(), move.getPiece());

            // Set origin square to empty
            set(origin.getRow(), origin.getCol(), null);
        } else {
            throw new IllegalMoveException("Jogada inválida!");
        }
    }

    // ONLY PROVIDE LAST MOVE, USED FOR VALIDITY CHECKING ONLY
    public void undoMove(ChessMove move) {
        if (move.getCapture() != null) {
            // Revert captured piece
            set(move.getDestination().getRow(), move.getDestination().getCol(), move.getCapture());
        } else {
            // Set destination back to null
            set(move.getDestination().getRow(), move.getDestination().getCol(), null);
        }

        // Set moved piece back in place
        set(move.getOrigin().getRow(), move.getOrigin().getCol(), move.getPiece());
    }

    public void rebuildBoard(List<ChessMove> moves) {
        resetBoard();
        for (ChessMove move : moves) {
            try {
                update(move);
            } catch (IllegalMoveException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isCapture(ChessMove move) {
        ChessPiece currentPiece = get(move.getDestination().getRow(), move.getDestination().getCol());
        ChessPiece newPiece = move.getPiece();

        if (currentPiece == null) {
            return false;
        }
        else {
            return newPiece.getColor() != currentPiece.getColor();
        }
    }

    public boolean isPromotion(ChessMove move) {
        // Verificar se é um peão na ultima linha
        return (move.getPiece().getChessPieceKind() == ChessPieceKind.PAWN)
                && ((move.getPiece().getColor() == Color.WHITE && move.getDestination().getRow() == 7)
                || (move.getPiece().getColor() == Color.BLACK && move.getDestination().getRow() == 0));
    }

    public boolean checkValidity(ChessMove move) {
        return Validation.checkValidity(move, this);
    }

    public HashMap<ChessPiece, ChessPosition> getAllTeamPieces(Color teamColor) {
        HashMap<ChessPiece, ChessPosition> teamPieces = new HashMap<ChessPiece, ChessPosition>();

        // Find all attacking pieces
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!isEmpty(y, x)) {
                    if (get(y, x).getColor() == teamColor) {
                        teamPieces.put(get(y, x), new ChessPosition(y, x));
                    }
                }
            }
        }

        return teamPieces;
    }

    public boolean isEmpty(int row, int col){
        return get(row, col) == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = 0; i < 8; i++) {
            sb.append("  ").append((char) ('a' + i)).append("  ");
        }
        sb.append("\n  +");
        for (int i = 0; i < 8; i++) {
            sb.append("----+");
        }
        sb.append("\n");
        for (int i = 7; i >= 0; i--) {
            sb.append("").append(i+1).append(" ");
            for (int j = 0; j < 8; j++) {
                if (get(i, j) != null) {
                    sb.append("| ").append(get(i, j)).append(" ");
                } else {
                    sb.append("|    ");
                }
            }
            sb.append("|\n  +");
            for (int k = 0; k < 8; k++) {
                sb.append("----+");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
