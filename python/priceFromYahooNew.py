import requests
import json
import datetime
import Price
import sys
import time

instrList = {
    'EURUSD=X':'EURUSD',
    'AIR.PA':'AIR.PA',
    '^BFX':'BEL20',
    'BRNT.MI':'DE000A1N49P6',
    'CRUD.L':'GB00B15KXV33',
    'DSY.PA':'DSY.PAR',
    'RACE.MI':'RACE.MI',
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
    'INGA.AS': 'NL0011821202',
    'ATO.PAR': 'FR0000051732',
    'PHIA.AS': 'NL0000009538',
    'UMI.BRU': 'BE0974320526',
    'IFX.FRK': 'DE0006231004',
    'STM.PA': 'NL0000226223',
    'CAP.PA': 'FR0000125338',
    'WLN.PAR': 'FR0011981968',
    'ALGN': 'US0162551016',
    'AMZN': 'US0231351067',
    'BABA': 'US01609W1027',
    '9988.HK': '9988.HK',
    'MU': 'US5951121038',
    'JD': 'US47215P1066',
    'EL.PAR': 'FR0000121667',
    'XIOR.BR': 'XIOR.BRU',
    'LIN.DE': 'LIN.DE',
    'TSM':'TSM',
    '^NDX':'^NDX',
    'ENPH':'ENPH',
    'ADYEN.AS':'ADYEN.AMS',
    'SNOW':'SNOW',
    '^GSPC':'SPX',
    '^STOXX50E':'SX5E',
    'MRNA':'MRNA',
    'GBS.L':'GB00B00FHZ82',
    'INRG.MI':'IE00B1XNHC34',
    'AYEW.F':'IE00BJ5JNY98',
    'IUSA.DE':'IE0031442068'
}

identifier=sys.argv[1]
#1mo,1y,3mo,ytd
period="6mo"
headers = {
    'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'}

def getPrices(ticker,period):
    url="https://query1.finance.yahoo.com/v8/finance/chart/"+ticker+"?interval=1d&range="+period
    try:
        r = requests.get(url,headers=headers)     
    except requests.exceptions.SSLError as e:
        print "error getting price for: "+ticker
        print e

    data = json.loads(r.text) 
    dates=data["chart"]["result"][0]["timestamp"]
    priceData = data["chart"]["result"][0]["indicators"]["quote"][0]["close"]
    prices=[]
    for i in range(len(dates)):
        #print i
        currentDate=datetime.datetime.utcfromtimestamp(dates[i]).replace(hour=0,minute=0, second=0, microsecond=0)
        prevDate=datetime.datetime.utcfromtimestamp(dates[i-1]).replace(hour=0,minute=0, second=0, microsecond=0)
        # avoid twice the same price for the same date
        if currentDate != prevDate and i>0:
            # array of array of 3 elements  price,evol,date
            prices.append([priceData[i],(priceData[i]-priceData[i-1])/priceData[i-1],currentDate])
    return prices 

def getInstrId(ticker):
    instrument = Price.INSTRUMENT()
    if instrList.get(ticker) is not None:
    	instrId = instrument.getId(instrList.get(ticker))
    else:
    # if can resolve isin use ticker to find id of instrument
    	instrId = instrument.getId(ticker)
    return instrId

def buildPriceObjects(prices,ticker):
    pricesCollection=[]
    # I don't know why necessary to cast to int to avoid u72 for instance
    instrId=int(getInstrId(ticker))
    for i in range(len(prices)):
        myPrice=Price.INSTR_PRICE(
                value_d=Price.INSTR_PRICE.convertDateToTime(prices[i][2]),
                value=prices[i][0],
                evol=prices[i][1])
        myPrice.instr_id=instrId
        myPrice.provider_id=9
        myPrice.currency="EUR"
        pricesCollection.append(myPrice)
    return pricesCollection

def storePrice(price):
    price.store()
    print identifier + " " + time.strftime('%d-%m-%Y',time.localtime(int(price.value_d)/1000))+" "+str(price.value)+" "+str(round(price.evol,4)*100)+"%"

def storePrices(prices):
    for price in prices:
        storePrice(price)   

def storeLastPrice(priceCollection):
    lastPriceObject=priceCollection[len(priceCollection)-1]
    storePrice(lastPriceObject)
        
prices=getPrices(identifier,period)
priceCollection=buildPriceObjects(prices,identifier)
storeLastPrice(priceCollection)
#storePrices(priceCollection)