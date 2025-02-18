import pandas as pd
import matplotlib.pyplot as plt
from fredapi import Fred

# Replace with your FRED API Key
FRED_API_KEY = "3152e8b354b419579b966e13846e2553"

# Initialize FRED API
fred = Fred(api_key=FRED_API_KEY)

# Fetch 10-Year Treasury Yield (as proxy for r) - Series ID: GS10
r = fred.get_series('GS10')

# Fetch Real GDP Growth Rate (as proxy for g) - Series ID: A191RL1Q225SBEA
g = fred.get_series('A191RL1Q225SBEA')

# Convert interest rate to decimal form
r = r / 100  

# Convert GDP growth rate from quarterly to annualized
g = g.rolling(4).sum()

# Align data by common date index
df = pd.DataFrame({'Interest Rate (r)': r, 'GDP Growth Rate (g)': g}).dropna()

# Plot r vs g
plt.figure(figsize=(10, 5))
plt.plot(df.index, df['Interest Rate (r)'], label="Interest Rate (r)", color='red')
plt.plot(df.index, df['GDP Growth Rate (g)'], label="GDP Growth Rate (g)", color='green')
plt.axhline(y=0, color='black', linewidth=0.5)
plt.legend()
plt.title("US Debt Sustainability: Interest Rate (r) vs GDP Growth Rate (g)")
plt.xlabel("Year")
plt.ylabel("Rate (%)")
plt.grid(True)

# Highlight r > g vs g > r
plt.fill_between(df.index, df['Interest Rate (r)'], df['GDP Growth Rate (g)'], 
                 where=(df['Interest Rate (r)'] > df['GDP Growth Rate (g)']), 
                 interpolate=True, color='red', alpha=0.3, label='r > g (Debt Grows)')

plt.fill_between(df.index, df['Interest Rate (r)'], df['GDP Growth Rate (g)'], 
                 where=(df['Interest Rate (r)'] <= df['GDP Growth Rate (g)']), 
                 interpolate=True, color='green', alpha=0.3, label='g > r (Debt Shrinks)')

plt.legend()
plt.show()
