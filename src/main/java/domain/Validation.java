package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Validation {

    public static boolean checkValidity(ChessMove move, ChessBoard board){
        ChessPosition origPos = move.getOrigin();
        ChessPosition destPos = move.getDestination();
        int x1 = origPos.getCol();
        int y1 = origPos.getRow();
        int x2 = destPos.getCol();
        int y2 = destPos.getRow();

        ChessPiece pieceToMove = move.getPiece();
        ChessPiece pieceInDestination = board.get(y2, x2);
        if (pieceInDestination != null) {
            if(pieceInDestination.getColor() == pieceToMove.getColor()) return false;
        }

        if ((x2 == x1 || (x2 < 8 && x2 >= 0)) && (y2 == y1 || (y2 < 8 && y2 >= 0)) && (!(x2 == x1 && y2 == y1))) {
            switch (pieceToMove.getChessPieceKind()){
                case KING:
                    return checkKingValidity(x1, y1, x2, y2, board);
                case QUEEN:
                    return (checkRookValidity(x1, y1, x2, y2, board) || checkBishopValidity(x1, y1, x2, y2, board));
                case BISHOP:
                    return checkBishopValidity(x1, y1, x2, y2, board);
                case ROOK:
                    return checkRookValidity(x1, y1, x2, y2, board);
                case KNIGHT:
                     return checkKnightValidity(x1, y1, x2, y2, board);
                case PAWN:
                     return checkPawnValidity(x1, y1, x2, y2, board, move);
            }
        }
        return false;
    }
    
    private static boolean checkBishopValidity(int x1, int y1, int x2, int y2, ChessBoard board) {

        int xLength = Math.abs(x2-x1);
        int yLength = Math.abs(y2-y1);

        if (xLength == yLength) {
            // Check all cells before the destination
            if (x1 < x2) { // a ir para a direita
                if (y1 < y2) { // para cima
                    for (int i = 1; i < xLength; i++) {
                        // Peça no caminho
                        if(!board.isEmpty(y1 + i, x1 + i)) {
                            return false;
                        }
                    }
                }
                else { // para baixo
                    for (int i = 1; i < xLength; i++) {
                        // Peça no caminho
                        if(!board.isEmpty(y1 - i, x1 + i)) {
                            return false;
                        }
                    }
                }
            }
            else { // a ir para a esquerda
                if (y1 < y2) { // para cima
                    for (int i = 1; i < xLength; i++) {
                        // Peça no caminho
                        if(!board.isEmpty(y1 + i, x1 - i)) {
                            return false;
                        }
                    }
                }
                else { // para baixo
                    for (int i = 1; i < xLength; i++) {
                        // Peça no caminho
                        if(!board.isEmpty(y1 - i, x1 - i)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }

        return false;

    }

    private static boolean checkRookValidity(int x1, int y1, int x2, int y2, ChessBoard board){
            // horizontal move
            if (x2 != x1 && y2 == y1){
                // moving right
                if (x2 > x1) {
                    for (int i = x1; i < x2; i++) {
                        if (!(board.isEmpty(y1, i))) {
                            ChessPiece pieceOnTheWay = board.get(y1, i);
                            if (pieceOnTheWay.getChessPieceKind() != ChessPieceKind.KING) {
                                return false;
                            }
                        }
                    }
                }
                // moving left
                else {
                    for (int i = x2; i < x1; i++){
                        if (!(board.isEmpty(y1, i))) {
                            ChessPiece pieceOnTheWay = board.get(y1, i);
                            if (pieceOnTheWay.getChessPieceKind() != ChessPieceKind.KING) {
                                return false;
                            }
                        }
                    }
                }
            }
            // vertical move
            else {
                // moving up
                if (y2 > y1){
                    for (int i = y1; i < y2; i++){
                        if (!(board.isEmpty(i, x1))) {
                            return false;
                        }
                    }
                }
                // moving down
                else {
                    for (int i = y2; i < y1; i++){
                        if (!(board.isEmpty(i, x1))) {
                            return false;
                        }
                    }
                }
            }
        return true;
    }

    private static boolean checkPawnValidity(int x1, int y1, int x2, int y2, ChessBoard board, ChessMove move) {
        ChessPiece piece = move.getPiece();

        // Caso os peões tentem andar para trás
        if ((piece.getColor() == Color.WHITE && y2 < y1) || (piece.getColor() == Color.BLACK && y2 > y1)) {
            return false;
        }

        // Caso seja uma captura, apenas se pode mover diagonalmente por 1 quadrado
        if (board.isCapture(move)) {
            return (Math.abs(x2-x1) == 1 && Math.abs(y2-y1) == 1);
        }
        // Caso não seja, apenas se pode mover em frente, por 1 quadrado, ou 2 se estiver no início
        else {
            // Caso os peões estejam nas posições iniciais, poderão mover-se dois quadrados para a frente de numa jogada
            if ((piece.getColor() == Color.WHITE && y1 == 1) || (piece.getColor() == Color.BLACK && y1 == 6)) {
                return (Math.abs(x2-x1) == 0 && (Math.abs(y2-y1) == 2 || Math.abs(y2-y1) == 1));
            }

            // Caso contrário, apenas 1
            return (Math.abs(x2-x1) == 0 && Math.abs(y2-y1) == 1);
        }
    }

    private static boolean checkKingValidity(int x1, int y1, int x2, int y2, ChessBoard board){
        return (x2 == x1 || x2 == x1 + 1 || x2 == x1 - 1) && (y2 == y1 || y2 == y1 + 1 || y2 == y1 - 1);
    }
    
    private static boolean checkKnightValidity(int x1, int y1, int x2, int y2, ChessBoard board){
        return (Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 2) || (Math.abs(x2 - x1) == 2 && Math.abs(y2 - y1) == 1);
    }

    public static boolean isCheck(ChessBoard board, Color kingColor) {
        ChessPosition kingPos = null;
        HashMap<ChessPiece, ChessPosition> attackingPieces = new HashMap<ChessPiece, ChessPosition>();

        // Find all attacking pieces
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!board.isEmpty(y, x)) {
                    if (board.get(y, x).getColor() != kingColor) {
                        attackingPieces.put(board.get(y, x), new ChessPosition(y, x));
                    }
                }
            }
        }

        // Find king's position
        ChessPiece currentPiece = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!board.isEmpty(y, x)) {
                    currentPiece = board.get(y, x);
                    if (currentPiece.getChessPieceKind() == ChessPieceKind.KING
                            && currentPiece.getColor() == kingColor) {
                        kingPos = new ChessPosition(y, x);
                    }
                }
            }
        }

        // Check if any piece has a valid move to the king
        for (ChessPiece piece : attackingPieces.keySet()) {
            int x1 = attackingPieces.get(piece).getCol();
            int y1 = attackingPieces.get(piece).getRow();
            int x2 = kingPos.getCol();
            int y2 = kingPos.getRow();

            switch (piece.getChessPieceKind()) {
                case KING:
                    if (checkKingValidity(x1, y1, x2, y2, board)) {
                        return true;
                    }
                    break;
                case QUEEN:
                    if ((checkRookValidity(x1, y1, x2, y2, board) || checkBishopValidity(x1, y1, x2, y2, board))) {
                        return true;
                    }
                    break;
                case BISHOP:
                    if (checkBishopValidity(x1, y1, x2, y2, board)) {
                        return true;
                    }
                    break;
                case ROOK:
                    if (checkRookValidity(x1, y1, x2, y2, board)) {
                        return true;
                    }
                    break;
                case KNIGHT:
                    if (checkKnightValidity(x1, y1, x2, y2, board)) {
                        return true;
                    }
                    break;
                case PAWN:
                    // Verificar se apenas as diagonais frontais de um peão causam check
                    if ((piece.getColor() == Color.WHITE && y2 > y1) || (piece.getColor() == Color.BLACK && y2 < y1)) {
                        if (Math.abs(x2 - x1) == 1 && Math.abs(y2 - y1) == 1) return true;
                    }
            }
        }
        return false;
    }
}
