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
	class Meta:
		database = database
	def getId(self,code):
		query = INSTRUMENT.select(INSTRUMENT.id).where(INSTRUMENT.code==code)
		for row in query.namedtuples():
			return row.id

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
		return datetime.datetime.utcfromtimestamp((self.value_d)/1000+86400) # add 86400 second to add one day	
	def store(self):
		try:
			INSTR_PRICE.replace({"instr_id":self.instr_id,"value":self.value,"value_d":self.value_d,"evol":self.evol,"currency":"EUR","provider_id":8}).execute()
			#self.save()
		except peewee.IntegrityError:
			print "duplicating key need to try update"

	@staticmethod	
	def convertDateToTime(mydate):
		return time.mktime(mydate.timetuple())*1000



