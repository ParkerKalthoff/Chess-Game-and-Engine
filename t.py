from math import exp, factorial

# Given average rate (lambda) for the Poisson distribution
lambda_val = 6

# Part (a): P(X <= 3) = P(X = 0) + P(X = 1) + P(X = 2) + P(X = 3)
P_fewer_than_4 = sum((exp(-lambda_val) * lambda_val ** k) / factorial(k) for k in range(4))

# Part (b): P(6 <= X <= 8) = P(X = 6) + P(X = 7) + P(X = 8)
P_6_to_8 = sum((exp(-lambda_val) * lambda_val ** k) / factorial(k) for k in range(6, 9))

# Round results to 4 decimal places
print(round(P_fewer_than_4, 4))
print(round(P_6_to_8, 4))
