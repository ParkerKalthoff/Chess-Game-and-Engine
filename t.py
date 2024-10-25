def F(n : int):
  if n <=1: 
    fib = n
  else:
    print(n)
    fib = F(n - 1) + F(n - 2)
  print(fib)
  return fib

F(4)