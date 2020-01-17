import sys
import requests
import json
import Price
import time
import peewee
import traceback
from datetime import datetime

# URL="https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=UMI.BR&apikey=EZ49SSU14R2EGHNS"

instrList = {
    'NVDA':'NVDA',
    'MDB':'MDB',
    'CSCO':'CSCO',
    'TINC.BRU':'TINC.BRU',
    'SBUX':'SBUX',
    'TEVA':'TEVA',
    'XIOR.BRU':'XIOR.BRU',
    'ASIT.BRU':'ASIT.BRU',
    'ACWI':'ACWI',
    'BAR.BRU':'BAR.BRU',
    'AMD':'AMD',
    'ADBE':'US00724F1012',
    'CROX': 'CROX',
    'PANW':'US6974351057',
    'AAPL': 'US0378331005',
    'INGA.AMS': 'NL0011821202',
    'ATO.PAR': 'FR0000051732',
    'PHIA.AMS': 'NL0000009538',
    'UMI.BRU': 'BE0974320526',
    'IFX.FRK': 'DE0006231004',
    'STM.PAR': 'NL0000226223',
    'CAP.PAR': 'FR0000125338',
    'WLN.PAR': 'FR0011981968',
    'ALGN': 'US0162551016',
    'AMZN': 'US0231351067',
    'BABA': 'US01609W1027',
    '9988.HK': '9988.HK',
    'MU': 'US5951121038',
    'JD': 'US47215P1066',
    'EL.PAR': 'FR0000121667'
}


def convertToDate(strDate):
    mydateTime = datetime.strptime(strDate, '%Y-%m-%d')
    return mydateTime

def getHistPrice(ticker):
    param = {
        "function": "TIME_SERIES_DAILY",
        "symbol": ticker,
        "outputsize": "full",
        "apikey": "EZ49SSU14R2EGHNS",
    }
    r = requests.get("https://www.alphavantage.co/query", param)
    #print r.text
    data = json.loads(r.text) 
    priceData = data["Time Series (Daily)"]
    lastPrice=float(0.0)
    for priceDateString in sorted (priceData.keys()):
        priceDate = convertToDate(priceDateString)
        if priceDate.year > 2019 :
            myPrice = Price.INSTR_PRICE(
            value_d=Price.INSTR_PRICE.convertDateToTime(priceDate),
            value=priceData[priceDateString]["4. close"],
            evol=0)
            myPrice.provider_id = 8  # Alpha
            myPrice.currency = "EUR"
            if lastPrice != 0.0:
                myPrice.evol=((float(myPrice.value)-lastPrice)/float(lastPrice))
            instrument = Price.INSTRUMENT()
            myPrice.instr_id = instrument.getId(ticker)
            myPrice.store();
            print ticker +":"+priceDateString+ " "+str(lastPrice)+ " "+str(myPrice.evol*100)+"%"
        lastPrice=float(priceData[priceDateString]["4. close"])  

def getLastPrice(ticker):
    param = {
        "function": "GLOBAL_QUOTE",
        "symbol": ticker,
        "outputsize": "compact",
        "apikey": "EZ49SSU14R2EGHNS",
    }
    r = requests.get("https://www.alphavantage.co/query", param)
    # print r.text
    data = json.loads(r.text)
    try:
        if len(data["Global Quote"]) == 0:
            print "no data for Symbol:" + ticker
            sys.exit(0)
    except KeyError as e:
        print "Invalid answer from price Provider"
        #traceback.print_exc()
        print r.text
        sys.exit(0)
        
  
    # print data["Time Series (Daily)"][1]["4. close"]
    # for x in data["Time Series (Daily)"]:
    # print(x+":"+data["Time Series (Daily)"][x]["4. close"])

    stringDate = data["Global Quote"]["07. latest trading day"]
    priceDate = convertToDate(stringDate)
    evol = data["Global Quote"]["10. change percent"]
    evol = float(evol[:-1])
    myPrice = Price.INSTR_PRICE(
        value_d=Price.INSTR_PRICE.convertDateToTime(priceDate),
        value=data["Global Quote"]["05. price"],
        evol=evol / 100.0)
    myPrice.provider_id = 8  # Alpha
    myPrice.currency = "EUR"
    instrument = Price.INSTRUMENT()
    # instr_id=instrument.getId(instrList.get(ticker))
    if instrList.get(ticker) is not None:
        myPrice.instr_id = instrument.getId(instrList.get(ticker))
    else:
        # if can resolve isin use ticker to find id of instrument
        myPrice.instr_id = instrument.getId(ticker)
    # myPrice.value=1
    return myPrice


if len(sys.argv) < 2:
    # print "specify Ticker"
    # print "example: python priceFromAlpha.py AAPL"

    for symbol in sorted (instrList.keys()):
        # need to ensure no more than 5 request per minute
        time.sleep(15)
        lastPrice = getLastPrice(symbol)
        isin = instrList.get(symbol)
        print symbol + "-" + isin + " " + lastPrice.value + " at:" + lastPrice.getDate().strftime(
            '%Y-%m-%d') + " evol:" + str(lastPrice.evol * 100)
        lastPrice.store();
    sys.exit(0)

if len(sys.argv) == 3:
    if sys.argv[2] == "YTD" :
        print "get Historical price since begin of year"
        getHistPrice(sys.argv[1])
    else :
        print "Date Frame not supported only YTD"

# one argument that is symbol of stock
ticker = sys.argv[1]

lastPrice = getLastPrice(ticker)
isin = instrList.get(ticker)
lastPrice.store();
print "store price for " + ticker + " " + lastPrice.value + " at:" + lastPrice.getDate().strftime('%Y-%m-%d')+" evol:" + str(lastPrice.evol * 100)+"%"
# try:
# except IntegrityError:  ## Occurs if primary_key already exists
# lastPrice.update()
# print ticker+"-"+isin+"date:"+lastPrice.value+" changePerc "+lastPrice.evol
