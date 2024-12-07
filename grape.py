def ulong_to_long(ulongs):
    """Convert unsigned 64-bit integers to signed 64-bit integers."""
    INT64_MAX = 2**63 - 1
    UINT64_MAX = 2**64
    return [num if num <= INT64_MAX else num - UINT64_MAX for num in ulongs]

def generate_bishop_masks():
    """Generate movement bitboard masks for a bishop on all squares of an 8x8 board, excluding edge bits."""
    def bishop_moves(square):
        """Generate bishop moves for a single square, excluding edge bits."""
        moves = 0
        rank, file = divmod(square, 8)
        
        # Diagonal and anti-diagonal directions
        directions = [(-1, -1), (-1, 1), (1, -1), (1, 1)]
        
        for dr, df in directions:
            r, f = rank, file
            while 0 < r + dr < 7 and 0 < f + df < 7:  # Exclude edge squares
                r += dr
                f += df
                moves |= 1 << (r * 8 + f)
        return moves

    # Generate masks for all 64 squares
    masks = [bishop_moves(square) for square in range(64)]
    return ulong_to_long(masks)

# Generate and print the bishop masks
bishop_masks = generate_bishop_masks()
print("Bishop Bitboard Masks (Signed Long Format, Edge Bits Removed):")

bishop_masks = [long.__str__()+"L" for long in bishop_masks]

print(bishop_masks)
