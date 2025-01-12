import chess.pgn
from collections import defaultdict

def filter_games_by_elo(pgn_file, min_elo=1800):
    """
    Filters games from a PGN file where both players have a rating >= min_elo.
    Yields valid games one at a time.
    """
    with open(pgn_file, "r") as file:
        while True:
            game = chess.pgn.read_game(file)
            if game is None:
                break
            
            # Extract ratings from headers
            white_elo = game.headers.get("WhiteElo")
            black_elo = game.headers.get("BlackElo")
            
            # Check if both players meet the rating requirement
            if white_elo and black_elo:
                try:
                    white_elo = int(white_elo)
                    black_elo = int(black_elo)
                    if white_elo >= min_elo and black_elo >= min_elo:
                        yield game
                except ValueError:
                    # Skip games with invalid ELO ratings
                    continue

def build_opening_book_with_depth(pgn_file, min_elo=2100, max_depth=6):
    """
    Builds an opening book from a PGN file, filtering games based on Elo and limiting depth.
    Returns a dictionary where keys are FEN strings and values are tuples (move, frequency).
    """
    from collections import defaultdict
    opening_book = defaultdict(lambda: defaultdict(int))

    # Filter and parse games
    filtered_games = filter_games_by_elo(pgn_file, min_elo)

    for index, game in enumerate(filtered_games):

        print(index)

        board = game.board()
        depth = 0  # Track depth

        for move in game.mainline_moves():
            if depth >= max_depth:  # Stop processing if max depth is reached
                break

            fen = board.fen()  # Get the current position's FEN
            uci_move = move.uci()  # Convert move to UCI format (e.g., e2e4)
            opening_book[fen][uci_move] += 1  # Increment frequency for the move
            board.push(move)  # Apply the move to the board

            depth += 1  # Increment depth for each move

    # Convert to a simpler format: {fen: [(move1, freq1), (move2, freq2), ...]}
    simplified_book = {
        fen: [(move, freq) for move, freq in moves.items()]
        for fen, moves in opening_book.items()
    }

    return simplified_book


def save_opening_book(opening_book, output_file):
    """
    Saves the opening book as a JSON file.
    """
    import json
    with open(output_file, "w") as file:
        json.dump(opening_book, file, indent=4)

# Example usage
if __name__ == "__main__":
    pgn_file = "lichess_db_standard_rated_2013-12.pgn"  # Replace with your PGN file path
    output_file = "opening_book.json"
    
    print("Building the opening book...")
    opening_book = build_opening_book_with_depth(pgn_file, min_elo=1800)
    
    print(f"Saving the opening book to {output_file}...")
    save_opening_book(opening_book, output_file)
    print("Done!")
