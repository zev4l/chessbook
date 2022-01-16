package domain;

import java.util.ArrayList;
import java.util.HashMap;

public class Validation {

    public static boolean checkValidity(ChessMove move, ChessBoard board, ChessGame game){
        Boolean valid = null;

        ChessPosition origPos = move.getOrigin();
        ChessPosition destPos = move.getDestination();
        int x1 = origPos.getCol();
        int y1 = origPos.getRow();
        int x2 = destPos.getCol();
        int y2 = destPos.getRow();

        ChessPiece pieceToMove = move.getPiece();
        Color      currentTurn = pieceToMove.getColor();
        ChessPiece pieceInDestination = board.get(y2, x2);

        // Check if it's a valid castling move
        if (move.isCastling()) {
            return checkCastlingValidity(move, board, game);
        }

        // Check if destination piece belongs to enemy or not
        if (pieceInDestination != null) {
            if(pieceInDestination.getColor() == pieceToMove.getColor()) return false;
        }

        if ((x2 == x1 || (x2 < 8 && x2 >= 0)) && (y2 == y1 || (y2 < 8 && y2 >= 0)) && (!(x2 == x1 && y2 == y1))) {
            switch (pieceToMove.getChessPieceKind()){
                case KING:
                    valid = checkKingValidity(x1, y1, x2, y2, board);
                    break;
                case QUEEN:
                    valid = (checkRookValidity(x1, y1, x2, y2, board) || checkBishopValidity(x1, y1, x2, y2, board));
                    break;
                case BISHOP:
                    valid = checkBishopValidity(x1, y1, x2, y2, board);
                    break;
                case ROOK:
                    valid = checkRookValidity(x1, y1, x2, y2, board);
                    break;
                case KNIGHT:
                    valid = checkKnightValidity(x1, y1, x2, y2, board);
                    break;
                case PAWN:
                    valid = checkPawnValidity(x1, y1, x2, y2, board, move);
                    break;
            }

            if (valid) {
                // If it's a valid move, return false if it'd allow an enemy check
                return !wouldBeCheck(board, move);
            }
        }
        return false;
    }

    private static boolean checkCastlingValidity(ChessMove move, ChessBoard board, ChessGame game) {
        Color currentTurn = move.getPiece().getColor();
        ChessPiece specifiedRook = new ChessPiece(ChessPieceKind.ROOK, currentTurn);
        ChessPiece specifiedKing = new ChessPiece(ChessPieceKind.KING, currentTurn);
        int rookX;
        int rookY;
        int kingX = 4; // King's default column

        // DISCLAIMER: The ChessGame object is passed down all the way since ChessGame.addMove() because
        // game moves need to be accessed in order to determine if the king or the specified rook have
        // already moved. It's a bad approach, but it's the best we have under time pressure. It is repugnant
        // and annoying that we have to pass this ChessGame object all the way down and only use it once. Sorry :/

        // If king is in check, castling isn't allowed
        if (isCheck(board, currentTurn)) {
            return false;
        }

        // Detailing the specified rook
        if (move.getCastling() == CastlingDirection.KING_SIDE) {
            rookX = 7;
        } else {
            rookX = 0;
        }

        if (currentTurn == Color.WHITE) {
            rookY = 0;
        } else {
            rookY = 7;
        }

        // If the king or the specified rook have previously moved, castling isn't allowed
        for (ChessMove eachMove : game.getMoves()) {
            if ((eachMove.getPiece().equals(specifiedKing)) ||
                    (eachMove.getPiece().equals(specifiedRook) &&
                            move.getOrigin().getCol() == rookX &&
                            move.getOrigin().getRow() == rookY)) {
                return false;
            }
        }

        // Checking if there are any pieces between the king and the specified rook AND if any of the squares where the king would pass are under attack
        if (move.getCastling() == CastlingDirection.QUEEN_SIDE) {
            for (int i = 1; i < kingX; i++) {
                if (!board.isEmpty(rookY, i)) return false;
            }
            // Checking if c and d files are under attack
            if (isUnderAttack(2, rookY, board, currentTurn.opposite) ||
                isUnderAttack(3, rookY, board, currentTurn.opposite)) {
                return false;
            }

        } else if (move.getCastling() == CastlingDirection.KING_SIDE) {
            for (int i = 6; i > kingX; i--) {
                if (!board.isEmpty(rookY, i)) return false;
            }
            // Checking if f and g files are under attack
            if (isUnderAttack(5, rookY, board, currentTurn.opposite) ||
                isUnderAttack(6, rookY, board, currentTurn.opposite)) {
                return false;
            }
        }

        return true;
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
            if (x2 != x1 && y2 == y1) {
                // moving right
                if (x2 > x1) {
                    for (int i = x1 + 1; i < x2; i++) {
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
                    for (int i = x1 - 1; i > x2; i--){
                        if (!(board.isEmpty(y1, i))) {
                            ChessPiece pieceOnTheWay = board.get(y1, i);
                            if (pieceOnTheWay.getChessPieceKind() != ChessPieceKind.KING) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            }
            // vertical move
            else if (x2 == x1 && y2 != y1) {
                // moving up
                if (y2 > y1){
                    for (int i = y1 + 1; i < y2; i++){
                        if (!(board.isEmpty(i, x1))) {
                            return false;
                        }
                    }
                }
                // moving down
                else {
                    for (int i = y1 - 1; i > y2; i--){
                        if (!(board.isEmpty(i, x1))) {
                            return false;
                        }
                    }
                }
                return true;
            }
        return false;
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

        // Find king's position
        ChessPiece currentPiece = null;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!board.isEmpty(y, x)) {
                    currentPiece = board.get(y, x);
                    if (currentPiece.getChessPieceKind() == ChessPieceKind.KING
                            && currentPiece.getColor() == kingColor) {
                        kingPos = new ChessPosition(y, x);
                        break;
                    }
                }
            }
        }

        return isUnderAttack(kingPos.getCol(), kingPos.getRow(), board, kingColor.opposite);

    }

    public static boolean isUnderAttack(int x2, int y2, ChessBoard board, Color attackingColor) {
        HashMap<ChessPiece, ChessPosition> attackingPieces = board.getAllTeamPieces(attackingColor);

        // Check if any piece has a valid move to the king
        for (ChessPiece piece : attackingPieces.keySet()) {
            int x1 = attackingPieces.get(piece).getCol();
            int y1 = attackingPieces.get(piece).getRow();

            switch (piece.getChessPieceKind()) {
                case KING:
                    if (checkKingValidity(x1, y1, x2, y2, board)) {
                        return true;
                    }
                    break;
                case QUEEN:
                    boolean validRookMove = checkRookValidity(x1, y1, x2, y2, board);
                    boolean validBishopMove = checkBishopValidity(x1, y1, x2, y2, board);

                    // A queen move is either a valid rook move or a valid bishop move
                    if (validRookMove || validBishopMove) {
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

    public static boolean wouldBeCheck(ChessBoard board, ChessMove move) {

        /////////////// Simulate move
        board.executeMove(move);
        ///////////////

        // Test if it would allow check
        boolean wouldBeCheck = Validation.isCheck(board, move.getPiece().getColor());

        // Undo move before returning
        board.undoMove(move);

        return wouldBeCheck;
    }

    // Determinar quantidade de moves legais disponível, poderá mais tarde ser overhauled para mostrar exatamente quais

    public static int getLegalMovesAmount(ChessBoard board, Color teamColor, ChessGame game) {
        HashMap<ChessPiece, ChessPosition> teamPieces = board.getAllTeamPieces(teamColor);

        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();

        // TODO: Add castling to available moves

        for (ChessPiece piece : teamPieces.keySet()) {
            int x1 = teamPieces.get(piece).getCol();
            int y1 = teamPieces.get(piece).getRow();


            for (int y2 = 0; y2 < 8; y2++) {
                for (int x2 = 0; x2 < 8; x2++) {


                    ChessMove tempMove = new ChessMove(teamPieces.get(piece), new ChessPosition(y2, x2), piece);

                    if (checkValidity(tempMove, board, game)) {
                        moveList.add(tempMove);
                    }

                }
            }
        }

        return moveList.size();
    }

    public static boolean isCheckmate(ChessBoard board, Color teamColor, ChessGame game) {
        return getLegalMovesAmount(board, teamColor, game) == 0 && isCheck(board, teamColor);
    }

    public static boolean isStalemate(ChessBoard board, Color teamColor, ChessGame game) {
        return getLegalMovesAmount(board, teamColor, game) == 0 && !isCheck(board, teamColor);
    }
}