awk -v RS="\n" -v FS="," '{
if(NR>1) print "INSERT INTO INSTRUMENT (code,name) VALUES (\47"$2"\47,\47"$1"\47);"
}'<../Excel/FinancialInstruments.csv | sqlite3 FinancialWorld.db
#Import provider
cat<< EOF | sqlite3 FinancialWorld.db
INSERT INTO PROVIDER (code,name) VALUES ('MS','Morningstar');
INSERT INTO PROVIDER (code,name) VALUES ('frankfurtBoerse','frankfurtBoerse');
INSERT INTO PROVIDER (code,name) VALUES ('GoogleNyse','Google Finance');
INSERT INTO PROVIDER (code,name) VALUES ('Euronext','Euronext');
INSERT INTO PROVIDER (code,name) VALUES ('Bloomberg','Bloomberg');
EOF


