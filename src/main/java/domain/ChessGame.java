package domain;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.AUTO;

@Entity
@NamedQueries({
        @NamedQuery(name="findByPlayerName",
                query="SELECT cg FROM ChessGame cg, ChessPlayer cp WHERE cp.name = ?1 AND (cg.white = cp OR cg.black = cp)"),
        @NamedQuery(name="findByPlayerEmail",
                query="SELECT cg FROM ChessGame cg, ChessPlayer cp WHERE cp.email = ?1 AND (cg.white = cp OR cg.black = cp)"),
        @NamedQuery(name="listChessGames",
                query="SELECT cg FROM ChessGame cg"),
        @NamedQuery(name="deleteAllGames",
                query="DELETE FROM ChessGame")
})
public class ChessGame {
    @Id @GeneratedValue(strategy=AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ChessPlayer white;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ChessPlayer black;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    private List<ChessMove> moves;

    @Transient
    private ChessBoard board;

    private Color winner;
    private Outcome outcome;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMoveTimestamp;

    public ChessGame() {
    }

    public ChessGame(ChessPlayer white, ChessPlayer black, Date startDate) {
        setWhite(white);
        setBlack(black);

        setTimestamp(startDate);

        board = new ChessBoard();
        moves = new ArrayList<ChessMove>();
    }

    // TODO: Implement methods, document

    /* Setters */

    public void setWhite(ChessPlayer white) {
        this.white = white;
    }

    public void setBlack(ChessPlayer black) {
        this.black = black;
    }

    public void setWinner(Color winner) {
        this.winner = winner;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setLastMoveTimestamp(Date timestamp) {
        this.lastMoveTimestamp = timestamp;
    }

    /* Getters */

    public int getId() {
        return id;
    }

    public ChessPlayer getWhite() {
        return white;
    }

    public ChessPlayer getBlack() {
        return black;
    }

    public List<ChessMove> getMoves() {
        return moves;
    }

    public ChessBoard getBoard() {
        if (board == null) {
            board = new ChessBoard();
            board.rebuildBoard(getMoves());
        }

        return board;
    }

    public Date getLastMoveTimestamp() {
        return lastMoveTimestamp;
    }

    public Color getWinner() {
        return winner;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isOver() {
        return getOutcome() != null;
    }

    /* Other methods */

    public Color getTurn(){
        if (getMoves().isEmpty()) {
            return Color.WHITE;
        } else {
            ChessMove lastMove = getMoves().get(getMoves().size()-1);
            ChessPiece movePiece = lastMove.getPiece();
            Color pieceColor = movePiece.getColor();
            return pieceColor.opposite;
        }
    }

    public void addMove(ChessMove move) throws IllegalMoveException {

        // If game isn't over yet!

        if (!isOver()) {
            // Auto-set move details if needed
            if (move.getPiece() == null) {
                move.setPiece(getBoard().get(move.getOrigin().getRow(), move.getOrigin().getCol()));
            }
            // If specified piece is different from piece on board
            else if (!move.getPiece().equals(getBoard().get(move.getOrigin().getRow(), move.getOrigin().getCol()))) {
                throw new IllegalMoveException("A peça especificada é inválida!");
            }

            // Se for o jogador correto a jogar
            if ((getTurn() == Color.WHITE && move.getAuthor() == white) ||
                    (getTurn() == Color.BLACK && move.getAuthor() == black)) {

                // Se o jogador estiver a jogar com as peças de cor correta
                if (move.getPiece().getColor() == getTurn()){
                    getBoard().update(move);

                    // Set game-ending details
                    // Check if move resulted in checkmate
                    if (Validation.isCheckmate(getBoard(), getTurn().opposite)) {
                        move.setCheckmate();
                        setWinner(getTurn());
                        setOutcome(Outcome.CHECKMATE);
                    } else if (Validation.isStalemate(getBoard(), getTurn().opposite)) {
                        setOutcome(Outcome.STALEMATE);
                    }

                    // Add to move list
                    System.out.println(move);
                    moves.add(move);

                } else {
                    throw new IllegalMoveException("Jogador a jogar peça de cor errada!");
                }
            } else {
                throw new IllegalMoveException("Jogador a jogar na vez errada!");
            }
        } else {
            throw new IllegalMoveException("O jogo já terminou!");
        }

    }

    public String totalTime(Color c) {
        long totalSeconds = 0;

        for (ChessMove move : getMoves()) {
            if (move.getPiece().getColor() == c) {
                totalSeconds += move.getDuration();
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(new Date(totalSeconds * 1000));
    }

    public boolean getCastlingRights(Color c) {

        // Verificar se o rei dessa cor alguma vez se mexeu
        for (ChessMove move : getMoves()) {
            if (move.getPiece().getChessPieceKind() == ChessPieceKind.KING && move.getPiece().getColor() == c) {
                return false;
            }
        }

        // TODO Verificar também se o rei, ou o quadrado por onde irá passar, está em check

        return true;
    }

    public ChessMove getLastMove() {
        if (getMoves().isEmpty()) {
            return null;
        } else {
            return getMoves().get(getMoves().size()-1);
        }
    }

    // TODO: Improve output readability
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(getId()).append(", ");
        sb.append(getWhite().getName()).append(" vs. ").append(getBlack().getName());
        sb.append(" (Started: ").append(getTimestamp());

        if (getWinner() != null) {
            sb.append(". Winner: ").append(getWinner());
            sb.append(" by ").append("outcome");
        } else {
            sb.append(". Status: Ongoing");
        }

        sb.append(")");

        return sb.toString();
    }
}
