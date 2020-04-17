CREATE TABLE IF NOT EXISTS wrboot_dbinfo
(ID SERIAL NOT NULL  ,
 dbtype VARCHAR(50),
 dbname VARCHAR(100),
 dbdriverclass VARCHAR(100),
 dburl VARCHAR(100),
 dbuser VARCHAR(50),
 dbpassword VARCHAR(50),
 remark VARCHAR(200),
 PRIMARY KEY(id)
);