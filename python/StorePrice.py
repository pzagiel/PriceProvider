import sys
from datetime import datetime
import Price
# Utilisation des arguments de ligne de commande
if len(sys.argv) != 5:
    print("Usage: python script.py <isin> <priceValue> <priceEvol>  <date_string>")
    sys.exit(1)
isin=sys.argv[1]
priceValue = float(sys.argv[2])
priceEvol = sys.argv[3]
priceDateString = sys.argv[4]

#isin="DE000C65XXC4"
#priceEvol=-0.0656
#priceValue=2.85

priceDate = datetime.strptime(priceDateString, "%d-%m-%Y").date()
instrument = Price.INSTRUMENT()	
myPrice=Price.INSTR_PRICE(
	                value_d=Price.INSTR_PRICE.convertDateToTime(priceDate),
	                value=priceValue,
	                evol=priceEvol,
	                provider_id=7, # Bloomber Provider temporarly waiting integration EUREX in Application
	                currency="EUR",
	                instr_id=instrument.getId(isin)
	                )
myPrice.store()	 # Insert or Update Price of Instrument