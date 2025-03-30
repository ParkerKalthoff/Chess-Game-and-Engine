import chess
import chess.pgn
import chess.engine

STOCKFISH_PATH = r"generateEqualPositions\Engine\stockfish.exe"
PGN_FILE = r"generateEqualPositions\LiChessData\lichess_rated_2013-09.pgn"

OUTPUT_FILE = "roughly_equal_positions.txt"

EVAL_MIN, EVAL_MAX = -0.6, 0.6

engine = chess.engine.SimpleEngine.popen_uci(STOCKFISH_PATH)

def evaluate_position(board):
    result = engine.analyse(board, chess.engine.Limit(depth=15))
    score = result["score"].relative.score(mate_score=10000)
    if score is None:
        return None
    return score / 100

filtered_positions = []
with open(PGN_FILE) as pgn:
    while True:

        if(len(filtered_positions) >= 3250):
            break
        else:
            print(len(filtered_positions))

        game = chess.pgn.read_game(pgn)
        if game is None:
            break
        board = game.board()
        for i, move in enumerate(game.mainline_moves(), start=1):
            board.push(move)
            if i > 6 and i < 14:
                eval_score = evaluate_position(board)
                if eval_score is not None and EVAL_MIN <= eval_score <= EVAL_MAX:
                    filtered_positions.append(board.fen())
                    continue

with open(OUTPUT_FILE, "w") as f:
    f.write("\n".join(filtered_positions))

print('Done!')