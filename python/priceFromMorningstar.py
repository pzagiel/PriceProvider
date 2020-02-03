import requests
import json
import datetime
import Price

#MSId="0P0000M54C" # Ishare Dax
#ticker="EXS1.DE"
MSId="0P0000Q1W0" # Lyxor Information Technology
ticker="TNOW.MI"
fromDate="2019-12-31"
print datetime.datetime.utcfromtimestamp(1580425200000/1000)
print "python hist="+str(datetime.datetime.utcfromtimestamp(1580428800000/1000))
# https://www.epochconverter.COMPACTJSON
#1580425200000 11H



url="http://tools.morningstarpro.fr/api/rest.svc/timeseries_price/i6wq0tnd7a?currencyId=EUR&idtype=Morningstar&frequency=daily&startDate=" + fromDate + "&priceType=&outputType=COMPACTJSON&id=" + MSId + "]2]0]FOEUR$$ALL";
#print url
r = requests.get(url)
data = json.loads(r.text) 
instrument = Price.INSTRUMENT()
instrId=instrument.getId(ticker)
prevPrice=""
for i in data:
	# date store in milliseconds since 1970 like in Java
	date= datetime.datetime.utcfromtimestamp(i[0]/1000)
	price= i[1]
	print str(date)+":"+str(price)
	#print str(date)+":"+str(price)
	myPrice=Price.INSTR_PRICE(
            value_d=i[0],
            value=price,
            evol=0)
	myPrice.provider_id=1
	myPrice.currency="EUR"
	myPrice.instr_id=instrId
	if (prevPrice !=""):
		myPrice.evol=(myPrice.value-prevPrice.value)/prevPrice.value
	prevPrice=myPrice
	myPrice.store()

	