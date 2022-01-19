package domain;

import javax.persistence.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
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
    @OrderBy("idx ASC")
    private List<ChessMove> moves;

    @Transient
    private ChessBoard board;

    private Color winner;
    private Outcome outcome;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date firstOpenedTimestamp;

    private int timeControl = 15; // In minutes

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

    public void setFirstOpenedTimestamp(Date timestamp) {
        this.firstOpenedTimestamp = timestamp;
    }

    public void setTimeControl(int timeControl) {
        this.timeControl = timeControl;
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

    public Date getFirstOpenedTimestamp() {
        return firstOpenedTimestamp;
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

    public int getTimeControl() {
        return timeControl;
    }

    public boolean isOver() {
        return getOutcome() != null;
    }

    public boolean hasBeenOpened() {return getFirstOpenedTimestamp() != null;}

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
            // Check time
            if (getTotalTimeLeft(getTurn()) == Duration.ZERO) {
                setWinner(getTurn().opposite);
                setOutcome(Outcome.TIMEOUT);
            }

            // Auto-set move details if needed
            if (move.getPiece() == null && !getBoard().isEmpty(move.getOrigin().getRow(), move.getOrigin().getCol())) {
                move.setPiece(getBoard().get(move.getOrigin().getRow(), move.getOrigin().getCol()));
            } else {
                throw new IllegalMoveException("Invalid Move.");
            }

            // If specified piece is different from piece on board
            if (!move.getPiece().equals(getBoard().get(move.getOrigin().getRow(), move.getOrigin().getCol()))) {
                throw new IllegalMoveException("The specified piece is not valid!");
            }

            // Se for o jogador correto a jogar
            if ((getTurn() == Color.WHITE && move.getAuthor() == white) ||
                    (getTurn() == Color.BLACK && move.getAuthor() == black)) {

                // Se o jogador estiver a jogar com as peças de cor correta
                if (move.getPiece().getColor() == getTurn()){
                    getBoard().update(move, this);

                    // Set game-ending details
                    // Check if move resulted in checkmate, stalemate, 50 move rule ending, insufficient material or repetition
                    if (Validation.isCheckmate(getBoard(), getTurn().opposite, this)) {
                        move.setCheckmate();
                        setWinner(getTurn());
                        setOutcome(Outcome.CHECKMATE);
                    } else if (Validation.isStalemate(getBoard(), getTurn().opposite, this)) {
                        setOutcome(Outcome.STALEMATE);
                    } else if (Validation.isMoveLimitEnding(getMoves())) {
                        setOutcome(Outcome.MOVE_LIMIT);
                    } else if (Validation.isInsufficientMaterialEnding(getBoard())) {
                        setOutcome(Outcome.INSUFFICIENT_MATERIAL);
                    } else if (Validation.isRepetitionEnding(getMoves())) {
                        setOutcome(Outcome.MOVE_LIMIT);
                    }

                    // Calculate duration (time between previous move being opened and new move being played)
                    move.setIndex(moves.size());
                    move.setPlayedTimestamp(new Date());

                    // If it's the first move, the duration of the move is calculated using firstOpenedTimestamp and move.playedTimestamp
                    if (getMoves().isEmpty()) {
                        move.setDuration(Duration.between(
                                getFirstOpenedTimestamp().toInstant(),
                                move.getPlayedTimestamp().toInstant()
                        ));
                    } else {
                        move.setDuration(Duration.between(
                                getLastMove().getSeenTimestamp().toInstant(),
                                move.getPlayedTimestamp().toInstant()
                        ));
                    }

                    // Add to move list
                    moves.add(move);

                } else {
                    throw new IllegalMoveException("Player playing wrong colored piece!");
                }
            } else {
                throw new IllegalMoveException("Player playing in wrong turn!");
            }
        } else {
            throw new IllegalMoveException("This game is already over");
        }

    }

    public Duration getTotalTimeUsed(Color c) {
        Duration total = Duration.ZERO;

        for (ChessMove move : getMoves()) {
            if (move.getPiece().getColor() == c) {
                total = total.plus(move.getDuration());
            }
        }

        return total;
    }

    public Duration getTotalTimeLeft(Color c) {

        Duration time;

        if (!getMoves().isEmpty()) {
            // If it's this player move, and he's already seen the previous move (i.e the time is counting irl, returns the
            // same when the move is played) (real-time update)
            if (getLastMove().hasBeenSeen() && getTurn() == c) {
                time = Duration.ofMinutes(getTimeControl()).minus(
                        getTotalTimeUsed(c)).plus(
                        Duration.between(
                                new Date().toInstant(), getLastMove().getSeenTimestamp().toInstant())
                );
            } else { // If current player hasn't seen the previous move
                time = Duration.ofMinutes(getTimeControl()).minus(getTotalTimeUsed(c));
            }

            // Return Duration.ZERO when out of time
            if (time.compareTo(Duration.ZERO) < 0) {
                time = Duration.ZERO;
            }
        } else {
            if (hasBeenOpened() && c==getTurn()) {
                time = Duration.ofMinutes(getTimeControl()).minus(
                        Duration.between(
                                getFirstOpenedTimestamp().toInstant(), new Date().toInstant())
                );
            } else {
                time = Duration.ofMinutes(getTimeControl());
            }

        }

        return time;
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
