import requests
import json
import time
import sys
import datetime
import Price
# Vivace,Vivace SP, Vivace HUSD,Vivace SP, Multistratgies, Multiequities,Multibonds,Allegro,Moderato 
funds=["FR0011015478","FR0013535499","FR0012497980","FR0013535465","FR0010923383","FR0011008762","FR0010923375","FR0011015460","FR0010923367"]

for isin in funds:
	# https seems not to work because of certificate verification failed so I change to http
	url="http://www.h2o-am.com/wp-content/themes/amarou/hs/get_json.php?isin="+isin
	r = requests.get(url)
	data = json.loads(r.text) 
	instrument = Price.INSTRUMENT()	
	lastPriceIndex=len(data)-1 # last element

	if (len(sys.argv)==2):
		lastPriceIndex=lastPriceIndex+int(sys.argv[1]) # argument 1 is the offset from last price
	# H20 is returning date in Java style, meaning milliseconds from 1970 and not seconds, so we divide by 1000 to get seconds	
	priceDate=datetime.datetime.utcfromtimestamp(data[lastPriceIndex][0]/1000)
	lastPrice=data[lastPriceIndex][1]
	prevPrice=data[lastPriceIndex-1][1]
	priceEvol=(lastPrice-prevPrice)/prevPrice
	myPrice=Price.INSTR_PRICE(
	                value_d=Price.INSTR_PRICE.convertDateToTime(priceDate),
	                value=lastPrice,
	                evol=priceEvol,
	                provider_id=10, # H20 Provider
	                currency="EUR",
	                instr_id=instrument.getId(isin)
	                )
	myPrice.store() 	
	print instrument.getName(isin)+" "+time.strftime('%d-%m-%Y',time.localtime(long(myPrice.value_d/1000)))+ " "+str(myPrice.value)+" "+str(round(myPrice.evol,4)*100)+"%"
