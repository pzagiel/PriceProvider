import peewee
import logging
import time
import datetime
#logger = logging.getLogger('peewee')
#logger.addHandler(logging.StreamHandler())
#logger.setLevel(logging.DEBUG)
database = peewee.SqliteDatabase("/Users/pzagiel/Development/PriceProvider/FinancialWorld.db",autocommit=True)

class INSTRUMENT(peewee.Model):
	id = peewee.CharField(primary_key=True) # primary key = unique id
	code=peewee.CharField()
	name=peewee.CharField()
	currency=peewee.CharField()
	class Meta:
		database = database
	def getId(self,code):
		query = INSTRUMENT.select(INSTRUMENT.id).where(INSTRUMENT.code==code)
		for row in query.namedtuples():
			return row.id
	def getName(self,code):
		query = INSTRUMENT.select(INSTRUMENT.name).where(INSTRUMENT.code==code)
		for row in query.namedtuples():
			return row.name	

class INSTR_PRICE(peewee.Model) :
	id = peewee.CharField(primary_key=True) # primary key = unique id
	value_d=peewee.IntegerField()
	value=peewee.DoubleField()
	evol=peewee.DoubleField()
	instr_id=peewee.IntegerField()
	currency=peewee.CharField()
	provider_id=peewee.IntegerField()
	
	#value_d = peewee.DateTimeField()
	#def __init__(self,priceDate, priceValue,changePerc):
    		#self.value_d = priceDate
    		#self.value = priceValue
    		#self.evol= changePerc
	class Meta:
		database = database

	def getDate(self): # return python date from unix time in millisecond
		# I don't know why utcfromtimestamp  function didn't return the right date and we need to add one day
		return datetime.datetime.utcfromtimestamp((self.value_d)/1000+86400) # add 86400 second to add one day	
	def store(self):
		try:
			INSTR_PRICE.replace({"instr_id":self.instr_id,"value":self.value,"value_d":self.value_d,"evol":self.evol,"currency":"EUR","provider_id":self.provider_id}).execute()
			#self.save()
		except peewee.IntegrityError:
			print "duplicating key need to try update"

	@staticmethod	
	def convertDateToTime(mydate):
		return time.mktime(mydate.timetuple())*1000

class POSITION(peewee.Model):
	id = peewee.CharField(primary_key=True) # primary key = unique id
	portfolio=peewee.CharField()
	quantity_n=peewee.DoubleField()
	cost_price_n=peewee.DoubleField()
	instr_id=peewee.IntegerField()
	date_d=peewee.IntegerField()
	class Meta:
		database = database
	def store(self):
		try:
			self.save()
		except peewee.IntegrityError:
			print "Error Inserting Position"
	@staticmethod
	def getId(code):
		query = INSTRUMENT.select(INSTRUMENT.id).where(INSTRUMENT.code==code)
		for row in query.namedtuples():
			return row.id

positions=[] 
positions.append(POSITION(portfolio="24822",quantity_n=5,instr_id=POSITION.getId("FR0011015478"),cost_price_n=63378.95,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=910,instr_id=POSITION.getId("LU0070992663"),cost_price_n=52.5,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=10300,instr_id=POSITION.getId("NL0011821202"),cost_price_n=9.7763,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=202,instr_id=POSITION.getId("FR0000051732"),cost_price_n=75.3,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=81,instr_id=POSITION.getId("FR0011981968"),cost_price_n=46.62,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=350,instr_id=POSITION.getId("FR0000125338"),cost_price_n=87.98,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=3000,instr_id=POSITION.getId("LU0828244219"),cost_price_n=11.36,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=520,instr_id=POSITION.getId("LU0075056555"),cost_price_n=33.63,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=19423.54,instr_id=POSITION.getId("EUR"),cost_price_n=1,date_d=0))
positions.append(POSITION(portfolio="24822",quantity_n=69779.05,instr_id=POSITION.getId("USD"),cost_price_n=1,date_d=0))
#for position in positions:
	#position.store()

