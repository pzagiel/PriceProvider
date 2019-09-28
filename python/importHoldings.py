import Price
import pandas as pd
import logging
import sys
logger = logging.getLogger('peewee')
logger.addHandler(logging.StreamHandler())
logger.setLevel(logging.DEBUG)

data = pd.read_csv(sys.argv[1])
positions=[] 
for index, row in data.iterrows():
	positions.append(Price.POSITION(portfolio=row['portfolio'],quantity_n=row['quantity'],instr_id=Price.POSITION.getId(row['isin']),cost_price_n=row['costprice'],date_d=0))
    #print(row['portfolio'], row['isin'],row['costprice'],row['quantity'])
#print(data)
for position in positions:
	position.store()