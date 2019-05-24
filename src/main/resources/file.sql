CREATE TABLE coin(
   name VARCHAR(5) NOT NULL PRIMARY KEY
  ,min  NUMERIC(9,5) NOT NULL
  ,max  NUMERIC(10,5) NOT NULL
);
INSERT INTO coin(name,min,max) VALUES ('BTC',3000,10000);
INSERT INTO coin(name,min,max) VALUES ('XRP',0.35,0.4);
INSERT INTO coin(name,min,max) VALUES ('ETH',200,300);
INSERT INTO coin(name,min,max) VALUES ('TRX',0.022,0.04);
INSERT INTO coin(name,min,max) VALUES ('ADA',0.06,0.1);
INSERT INTO coin(name,min,max) VALUES ('DENT',0.0008,0.0015);
INSERT INTO coin(name,min,max) VALUES ('MIOTA',0.29,0.5);
INSERT INTO coin(name,min,max) VALUES ('NPXS',0.00055,0.00085);
INSERT INTO coin(name,min,max) VALUES ('NANO',1.3,2);
INSERT INTO coin(name,min,max) VALUES ('XLM',0.09,0.15);
INSERT INTO coin(name,min,max) VALUES ('XMR',65,100);
INSERT INTO coin(name,min,max) VALUES ('BTT',0.001,0.0014);
INSERT INTO coin(name,min,max) VALUES ('XEM',0.06,0.09);
INSERT INTO coin(name,min,max) VALUES ('HOT',0.0017,0.0025);
INSERT INTO coin(name,min,max) VALUES ('BAT',0.25,0.4);
