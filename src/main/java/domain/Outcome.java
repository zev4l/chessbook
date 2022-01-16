package domain;

public enum Outcome {
    CHECKMATE,
    RESIGNATION,
    TIMEOUT,
    // Draw
    STALEMATE,
    INSUFFICIENT_MATERIAL,
    MOVE_LIMIT,
    REPETITION,
    AGREEMENT
}
