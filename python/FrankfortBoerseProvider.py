import requests
isin="XFRA:DE0006231004" # Infineon
param = {
        "symbol":isin,
        "resolution": 30,
        "from": "1561564096",
        "to": "1561996156",
    }
#https://api.boerse-frankfurt.de/tradingview/history?symbol=XFRA:DE0006231004&resolution=10&from=1561564096&to=1561996156
r = requests.get("https://api.boerse-frankfurt.de/tradingview/history", param)
print r
