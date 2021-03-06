package domain;

import javax.persistence.*;

import java.time.Duration;
import java.util.Date;
import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@Entity
public class ChessMove {
    @Id @GeneratedValue(strategy=AUTO)
    private int id;

    private Duration duration;

    @ManyToOne
    private ChessPlayer author;

    // Única maneira aparente de resolver sobreposição de colunas
    @AttributeOverrides({
            @AttributeOverride(name="row",column=@Column(name="originRow")),
            @AttributeOverride(name="col",column=@Column(name="originCol"))
            })
    @Embedded
    @Column(nullable = false)
    private ChessPosition from;

    @AttributeOverrides({
            @AttributeOverride(name="row",column=@Column(name="destRow")),
            @AttributeOverride(name="col",column=@Column(name="destCol"))
    })
    @Embedded
    @Column(nullable = false)
    private ChessPosition to;

    @AttributeOverrides({
            @AttributeOverride(name="color",column=@Column(name="capturedType")),
            @AttributeOverride(name="type",column=@Column(name="capturedColor"))
    })
    @Embedded
    private ChessPiece capture = null;

    private ChessPieceKind promotion = null;

    @Embedded
    private ChessPiece piece;

    private boolean isCheck;

    private boolean isCheckmate;

    private CastlingDirection castlingDirection;

    @Temporal(TemporalType.TIMESTAMP)
    private Date playedTimestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date seenTimestamp;

    private int idx;

    public ChessMove() {
    }

    // Construtor mais completo
    public ChessMove(ChessPlayer player, ChessPiece piece, ChessPosition from, ChessPosition to) {
        this.author = player;
        this.from = from;
        this.to = to;
        this.piece = piece;
    }

    // Construtor mais simples
    public ChessMove(ChessPlayer player, ChessPosition from, ChessPosition to) {
        this.author = player;
        this.from = from;
        this.to = to;
    }

    // Construtor para validações temporárias
    public ChessMove(ChessPosition from, ChessPosition to, ChessPiece piece) {
        this.piece = piece;
        this.from = from;
        this.to = to;
    }

    /* Setters */

    public void setOrigin(ChessPosition from) {
        this.from = from;
    }

    public void setDestination(ChessPosition to) {
        this.to = to;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
    }

    public void setCapture(ChessPiece capture) {
        this.capture = capture;
    }

    public void setAuthor(ChessPlayer player) {this.author = player;}

    public void setPromotion(ChessPieceKind promotion) {
        this.promotion = promotion;
    }

    public void setCheck() {
        this.isCheck = true;
    }

    public void setCheckmate() {
        this.isCheckmate = true;
    }

    public void setCastling(CastlingDirection direction) {this.castlingDirection = direction;}

    public void setPlayedTimestamp(Date timestamp) {
        this.playedTimestamp = timestamp;
    }

    public void setIndex(int index) {
        this.idx = index;
    }

    public void setSeenTimestamp(Date seenTimestamp) {
        this.seenTimestamp = seenTimestamp;
    }

    /* Getters */

    public int getId() {
        return id;
    }

    public ChessPosition getOrigin() {
        return from;
    }

    public ChessPosition getDestination() {
        return to;
    }

    public Duration getDuration() {
        return duration;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public ChessPiece getCapture() {
        return capture;
    }

    public ChessPlayer getAuthor() {return author;}

    public ChessPieceKind getPromotion() {
        return promotion;
    }

    public CastlingDirection getCastling() {return castlingDirection;}

    public Date getPlayedTimestamp() {
        return playedTimestamp;
    }

    public Date getSeenTimestamp() {
        return seenTimestamp;
    }

    public boolean isCheck() {return isCheck;}

    public boolean isCheckmate() {return isCheckmate;}

    public boolean isCapture() {return getCapture() != null;}

    public boolean isPromotion() {return getPromotion() != null;}

    public boolean isCastling() {return getCastling() != null;}

    public boolean hasBeenSeen() {return getSeenTimestamp() != null;}

    public boolean equals(ChessMove other) {
        if (this == other) return true;
        return id == other.id
                && duration == other.duration
                && Objects.equals(author.getEmail(), other.author.getEmail())
                && Objects.equals(from, other.from)
                && Objects.equals(to, other.to)
                && Objects.equals(capture, other.capture)
                && Objects.equals(piece, other.piece);
    }


    // TODO: toString Temporário (igualar a notação algébrica)
    // Adicionar notação caso check ou checkmate
    public String toString() {

        if (getCastling() == CastlingDirection.KING_SIDE) {
            return "O-O";
        }
        if (getCastling() == CastlingDirection.QUEEN_SIDE) {
            return "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        if (piece != null) {
            if (piece.getChessPieceKind() != ChessPieceKind.PAWN) {
                sb.append(piece);

            } else {
                if (isCapture()) {
                    sb.append((char) ('a' + getOrigin().getCol()));
                }
            }
        }

        if (isCapture()) {
            sb.append("x");
        }

        sb.append(to);

        if (isCheckmate()) {
            sb.append("#");
        }
        else if (isCheck()) {
            sb.append("+");
        }

        return sb.toString();
    }
}
