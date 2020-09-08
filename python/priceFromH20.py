import requests
import json
import time
import datetime
import Price

# Vivace, Vivace HUSD, Multistratgies, Multiequities,Multibonds,Allegro,Moderato
funds=["FR0011015478","FR0012497980","FR0010923383","FR0011008762","FR0010923375","FR0011015460","FR0010923367"]
for isin in funds:
	url="https://www.h2o-am.com/wp-content/themes/amarou/hs/get_json.php?isin="+isin
	r = requests.get(url)
	data = json.loads(r.text) 
	#print isin+" "+time.strftime('%d-%m-%Y',time.localtime(int(data[len(data)-1][0])/1000))+" "+str(data[len(data)-1][1])
	instrument = Price.INSTRUMENT()
	myPrice=Price.INSTR_PRICE(
	                value_d=Price.INSTR_PRICE.convertDateToTime(datetime.datetime.utcfromtimestamp(data[len(data)-1][0]/1000).replace(hour=0,minute=0, second=0, microsecond=0)),
	                value=data[len(data)-1][1],
	                evol=(data[len(data)-1][1]-data[len(data)-2][1])/data[len(data)-2][1],
	                provider_id=10, # H20 Provider
	                currency="EUR",
	                instr_id=instrument.getId(isin)
	                )
	myPrice.store()
	#Copy estimation for  Morningstar provider
	myPrice.provider_id=1
	myPrice.store()
	print instrument.getName(isin)+" "+time.strftime('%d-%m-%Y',time.localtime(long(myPrice.value_d/1000)))+ " "+str(myPrice.value)+" "+str(round(myPrice.evol,4)*100)+"%"
