# Query to get SP500 Prices
#https://query1.finance.yahoo.com/v8/finance/chart/%5EGSPC?symbol=^GSPC&period1=1314568800&
#period2=1500415200&interval=1d&includePrePost=true&events=div|split|earn&
#lang=en-US&region=US&crumb=oC6qFyVRUTg&corsDomain=finance.yahoo.com


# https://observablehq.com/@stroked/yahoofinance interesting


# Apple Quote
# last price
#https://query1.finance.yahoo.com/v8/finance/chart/AAPL?interval=1d 
# Best Query BABA daily price since begin of year
#https://query1.finance.yahoo.com/v8/finance/chart/BABA?interval=1d&range=ytd

# GIT Project
#https://github.com/Gunjan933/stock-market-scraper
# other Example of rich request

#https://query1.finance.yahoo.com/v8/finance/chart/MSFT?symbol=MSFT&period1=0&period2=9999999999&interval=1d&includePrePost=true&events=div%2Csplit
##https://query1.finance.yahoo.com/v8/finance/chart/%5EGSPC?symbol=^GSPC&period1=1314568800&period2=1500415200&interval=1d&includePrePost=true&events=div|split|earn&lang=en-US&region=US&crumb=oC6qFyVRUTg&corsDomain=finance.yahoo.com

#https://query1.finance.yahoo.com/v8/finance/chart/^SP500TR?symbol=^SP500TR&period1=1579185900&period2=1579620900&interval=5m&includePrePost=true&events=div|split|earn&lang=en-US&region=US&crumb=oC6qFyVRUTg&corsDomain=finance.yahoo.com

# SPP500
#https://query1.finance.yahoo.com/v8/finance/chart/^GSPC?interval=1d&range=ytd

import requests
import json
import datetime
import Price
import sys
import time

watchList = ["PANW","BRNT.MI","CRUD.L","MDB","ALGN","JD","INGA.AS","SBUX",  
                "PHIA.AS","STM.PA","CAP.PA","AMZN","BABA","AHLA.DE","9988.HK","MU","NFLX","^BFX",
                "AIR.PA","XIOR.BR","LIN.DE","TSM","^NDX","ENPH","ADYEN.AS","SNOW",
                "MAXR","SQ","^GSPC","SE","^STOXX50E","EXS1.DE","ARKK","NET","MRNA","WIX","DIM.PA",
                "EURUSD=X","LOTB.BR","ICLN","TEMN.SW","SIE.DE","^SOX","^HSI","000001.SS","NIO",
                "BYDDF","PRX.AS","ARCT","CRSP","PLTR"]

instrList = {
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
    'MRNA':'MRNA'

}

# Code below to get all instruments in database for Patrick Joly
#finInstr = Price.INSTRUMENT()
#watchList=finInstr.getAllInstruments()


if (len(sys.argv)==2):
    watchList=[sys.argv[1]]

headers = {
    'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36'}



for ticker in watchList:
    #time.sleep(5)
    #print w
    #ticker=sys.argv[1]
    #ticker=w
    # valide range
    #max,ytd,5d,1d
    url="https://query1.finance.yahoo.com/v8/finance/chart/"+ticker+"?interval=1d&range=1mo"
    try:
        r = requests.get(url,headers=headers)     
    except requests.exceptions.SSLError as e:
        print "error getting price for: "+ticker
        print e
        continue
        #print r.text
        #print url
        #print r.content  
    data = json.loads(r.text) 
    #print data
    dates=data["chart"]["result"][0]["timestamp"]
    prices=[]
    for i in dates:
    	myPrice=Price.INSTR_PRICE(
                value_d=datetime.datetime.utcfromtimestamp(i).replace(hour=0,minute=0, second=0, microsecond=0),
                value=0,
                evol=0)
    	prices.append(myPrice)

    priceData = data["chart"]["result"][0]["indicators"]["quote"][0]["close"]
    #print priceData
    count=0
    for i in priceData:
        #print "prices"+str(i)
        if(i is not None):
            prices[count].value=round(float(i),4)
        else:
            prices[count].value=i
            #print round(float(i),4)
            #prices[count].value=round(i,4)
        count=count+1

    # remove null values
    #for p in prices:
        #print "value:"+str(p.value)
    # filter data by removing None (empty data)
    test = [p for p in prices if p.value is not None]
    test.sort(key=lambda x: x.value_d)
    #test.sort(reverse=True) 

    #for t in test:
        # compute evol
        #print "test"+str(t.value_d)+"="+str(t.value)


    #for i in prices:
    #		print str(i.value_d)+":"+str(i.value)

    #Last Price
    #print str(prices[count-1].value_d)+" : "+str(prices[count-1].value)
    #print ticker
    #for i in test:
            #print str(i.value_d) +":"+str(i.value)

    priceToStore=test[len(test)-1]
    priceForEvol=test[len(test)-2]
    #print priceToStore.value_d
    priceToStore.evol=(priceToStore.value-priceForEvol.value)/priceForEvol.value
    priceToStore.value_d=Price.INSTR_PRICE.convertDateToTime(priceToStore.value_d)
    priceToStore.provider_id=9
    priceToStore.currency="EUR"

    instrument = Price.INSTRUMENT()
    # instr_id=instrument.getId(instrList.get(ticker))
    if instrList.get(ticker) is not None:
    	priceToStore.instr_id = instrument.getId(instrList.get(ticker))
    else:
    # if can resolve isin use ticker to find id of instrument
    	priceToStore.instr_id = instrument.getId(ticker)
    #print priceToStore.provider_id
    #print int(priceToStore.value_d)
    #print int(priceToStore.value_d)
    # yahoo give epoch date in milliseconds
    print ticker+ ":"+ time.strftime('%d-%m-%Y',time.localtime(int(priceToStore.value_d)/1000))+" "+str(priceToStore.value)+" "+str(round(priceToStore.evol,4)*100)+"%"
    priceToStore.store()
    #time.sleep(5)
 




