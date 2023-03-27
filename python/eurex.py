# -*- coding: utf-8 -*-
import requests
import datetime
import json
import subprocess
import sys

# Obtenir la date actuelle
today = datetime.date.today()

# on est lundi
if today.weekday() == 0:
    print "On est Lundi"
    today = today - datetime.timedelta(days=3)
# on est dimanche
elif today.weekday() == 6:
    print "On est Dimanche"
    today = today - datetime.timedelta(days=2)
else:
    print "On est ni Dimanche ni lundi"
    today = today - datetime.timedelta(days=1)
# On est lundi
if today.weekday() == 0 :
    dayBefore=today-datetime.timedelta(days=3)
else:
    dayBefore=today-datetime.timedelta(days=1)

print "Prev close price date:"+dayBefore.strftime('%d-%b-%Y').lower()
# Formater la date dans le format "YYYYMMDD"
formatted_date = dayBefore.strftime('%Y%m%d')

optionsDict = {
                'DE000C7FDWW2': " 'UBS       16.06.2023  strike 18.5'",
                'DE000C65XXC4': "'Banks 600 16.06.2023  strike 155 '"
              }

def getOptionClosePrice(identifier,busDate,optionType,strike):
    url = "https://www.eurex.com/api/v1/overallstatistics/"+identifier+"?filtertype=detail&productdate=20230616&busdate="+busDate+"&contracttype=M"

    # Utiliser curl pour récupérer les données JSON à partir de l'URL
    curl_process = subprocess.Popen(['curl','-s', url], stdout=subprocess.PIPE)
    json_data, err = curl_process.communicate()

    # Charger les données JSON
    data = json.loads(json_data)
    # Vérifier si les données existent
    if 'dataRowsCall' in data and optionType == "Call":
        options = data['dataRowsCall']
    elif 'dataRowsPut' in data and optionType == "Put":
        options = data['dataRowsPut']
    else:
        print("Il n'y a pas de close Price pour la date du "+ formatted_date)        
        sys.exit()
    #pas nécessaire de vérifier qu'il s'agit d'un put
    #put_option = next((opt for opt in put_options if opt['strike'] == 18.5 and opt['callOrPut'] == 'Put'), None)
    option = next((opt for opt in options if opt['strike'] == strike), None)
    if option:
        d_settle =option['dSettle']
        #print("La valeur de 'dSettle' pour l'option de vente (put) avec strike de 18.5 est de {}.".format(d_settle))
        return d_settle
    else:
        print("Aucune option de vente (put) avec strike de 18.5 n'a été trouvée.")

def storeOptionPrice(isin,id,busDate,optionType,strike):
    closePrice=getOptionClosePrice(id,today.strftime('%Y%m%d'),optionType,strike)
    dayBeforeClosePrice=getOptionClosePrice(id,dayBefore.strftime('%Y%m%d'),optionType,strike)
    evol=(closePrice-dayBeforeClosePrice)/dayBeforeClosePrice
    print "Option "+optionType+ " "+optionsDict[isin]+ " "+today.strftime('%d-%b-%Y').lower()+ " closePrice="+str(closePrice) + " evol="+str(round(evol*100,2))+"%"
    subprocess.call(["python", "/Users/pzagiel/Development/PriceProvider/python/StorePrice.py",isin,str(closePrice),str(evol),today.strftime('%d-%m-%Y')])

#UBS=55894 ,DE000C7FDWW2
storeOptionPrice("DE000C7FDWW2","55894",formatted_date,"Put",18.5)
#STOXX 600 BANKS=69760 , DE000C65XXC4
storeOptionPrice("DE000C65XXC4","69760",formatted_date,"Call",155)






# Nouvelle requete intéressante , statistique
# https://www.eurex.com/api/v1/overallstatistics/55894?busdate=20230324&filtertype=overview




""" dayBeforeClosePrice=getOptionClosePrice("55894",dayBefore.strftime('%Y%m%d'),2,18.5)
closePrice=getOptionClosePrice("55894",formatted_date,2,18.5)
evol=(closePrice-dayBeforeClosePrice)/dayBeforeClosePrice
#print "UBS PUT "+dayBefore.strftime('%Y%m%d')+ " closePrice="+str(dayBeforeClosePrice)
print "UBS PUT "+formatted_date+ " closePrice="+str(closePrice) + " evol="+str(round(evol*100,2))+"%"
subprocess.call(["python", "/Users/pzagiel/Development/PriceProvider/python/StorePrice.py","DE000C7FDWW2",str(closePrice),str(evol),today.strftime('%d-%m-%Y')])
dayBeforeClosePrice=getOptionClosePrice("69760",dayBefore.strftime('%Y%m%d'),1,155)
closePrice=getOptionClosePrice("69760",formatted_date,1,155)
evol=(closePrice-dayBeforeClosePrice)/dayBeforeClosePrice
print "BANKS 600 Call "+formatted_date+ " closePrice="+str(closePrice)+ " evol="+str(round(evol*100,2))+"%"
subprocess.call(["python", "/Users/pzagiel/Development/PriceProvider/python/StorePrice.py","DE000C65XXC4",str(closePrice),str(evol),today.strftime('%d-%m-%Y')])
# new request JSON """


#https://www.eurex.com/api/v1/overallstatistics/55894?filtertype=detail&productdate=20230616&busdate=20230323&contracttype=M

#Apparement la requ^te ci dessus suffit pour avoir tous les info de trade sur l'option
#Payload

#filtertype=detail&productdate=20230616&busdate=20230323&contracttype=M
#r = requests.get("https://live.euronext.com/intraday_chart/getChartData/FR0007054358-XPAR/intraday")
#r=requests.get("http://www.google.com")

#url="https://www.eurex.com/api/v1/overallstatistics/55894?filtertype=detail&productdate=20230616&busdate=20230323&contracttype=M"
#r=requests.get("https://www.eurex.com/api/v1/overallstatistics/55894?filtertype=detail&productdate=20230616&busdate=20230323&contracttype=M")
#r=requests.get("https://www.eurex.com")

# request JSON permet de récupérer le prix de clôture des option