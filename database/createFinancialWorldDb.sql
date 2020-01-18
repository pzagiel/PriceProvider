 #DROP TABLE INSTRUMENT;
 #DROP TABLE INSTR_PRICE;
 #DROP TABLE PROVIDER;
CREATE TABLE INSTRUMENT (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 code varchar(30) NOT NULL,
 name varchar(50) NOT NULL,
 currency char(3),
 type varchar(30), 
 priceEvol double
);

CREATE UNIQUE INDEX idx_instrument on INSTRUMENT(code);

CREATE TABLE INSTR_PRICE (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 timestamp DATE DEFAULT (datetime('now','localtime')),
 instr_id BIGINT NOT NULL,
 provider_id BIGINT NOT NULL,
 currency char(3) NOT NULL,
 value_d INTEGER NOT NULL,
 value DOUBLE NOT NULL,
 evol DOUBLE NOT NULL,
 FOREIGN KEY(instr_id) REFERENCES INSTRUMENT(id)
 FOREIGN KEY(provider_id) REFERENCES PROVIDER(id)
);


CREATE UNIQUE INDEX idx_instr_price on INSTR_PRICE(instr_id,provider_id,currency,value_d);

CREATE TABLE PROVIDER (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 code varchar(30) NOT NULL,
 name varchar(50) NOT NULL
);

CREATE UNIQUE INDEX idx_provider on provider (code);

CREATE TABLE POSITION (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 portfolio varchar(30) NOT NULL,
 instr_id BIGINT NOT NULL,
 quantity_n DOUBLE NOT NULL,
 FOREIGN KEY(instr_id) REFERENCES INSTRUMENT(id)
);
