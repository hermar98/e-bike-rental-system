DROP TABLE IF EXISTS station_data;
DROP TABLE IF EXISTS bike_data;
DROP TABLE IF EXISTS trip;
DROP TABLE IF EXISTS done_repair;
DROP TABLE IF EXISTS repair;
DROP TABLE IF EXISTS bike;
DROP TABLE IF EXISTS bike_type;
DROP TABLE IF EXISTS docking_station;
DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS admin_users;

CREATE TABLE admin_users (
  user_id       INTEGER     NOT NULL AUTO_INCREMENT,
  email        VARCHAR(100) NOT NULL,
  firstname VARCHAR(30) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  user_password VARCHAR(100) NOT NULL,
  salt         VARCHAR(100) NOT NULL,
  CONSTRAINT userid_pk PRIMARY KEY(user_id)
);

-- Creating tables
CREATE TABLE bike_type (
  type_id INT NOT NULL AUTO_INCREMENT,
  type_name VARCHAR(20) NOT NULL,
  rental_price DOUBLE NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  CONSTRAINT bike_type_pk PRIMARY KEY(type_id));

CREATE TABLE bike (
  bike_id INT NOT NULL,
  purchase_date DATE NOT NULL,
  price DOUBLE NOT NULL,
  make VARCHAR(30) NOT NULL,
  type_id INT NOT NULL,
  station_id INT,
  active BIT NOT NULL DEFAULT 1,
  CONSTRAINT bike_pk PRIMARY KEY(bike_id));

CREATE TABLE bike_data (
  bike_id INT NOT NULL,
  date_time DATETIME NOT NULL,
  coords_lat DOUBLE(10,6) NOT NULL,
  coords_lng DOUBLE(10,6) NOT NULL,
  charging_lvl DOUBLE(5,2) NOT NULL,
  trip_id INT,
  CONSTRAINT bike_data_pk PRIMARY KEY(bike_id, date_time));

CREATE TABLE repair (
  repair_id INT NOT NULL AUTO_INCREMENT,
  date_sent DATE NOT NULL,
  request_desc VARCHAR(30) NOT NULL,
  bike_id INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  CONSTRAINT repair_pk PRIMARY KEY(repair_id));

CREATE TABLE done_repair (
  repair_id INT NOT NULL,
  price DOUBLE NOT NULL,
  date_recieved DATE NOT NULL,
  repair_desc VARCHAR(30) NOT NULL,
  CONSTRAINT done_repair_pk PRIMARY KEY(repair_id));

CREATE TABLE docking_station (
  station_id INT NOT NULL AUTO_INCREMENT,
  station_name VARCHAR(20) NOT NULL,
  coords_lat DOUBLE(10,6) NOT NULL,
  coords_lng DOUBLE(10,6) NOT NULL,
  total_docks INT NOT NULL,
  active BIT NOT NULL DEFAULT 1,
  CONSTRAINT docking_station_pk PRIMARY KEY(station_id));

CREATE TABLE station_data (
  station_id INT NOT NULL,
  date_time DATETIME NOT NULL,
  power_usage DOUBLE(10,2) NOT NULL,
  CONSTRAINT station_data_pk PRIMARY KEY(station_id, date_time));

CREATE TABLE customer (
  customer_id INT NOT NULL,
  firstname VARCHAR(15),
  surname VARCHAR(15),
  CONSTRAINT customer_pk PRIMARY KEY(customer_id));

CREATE TABLE trip(
  trip_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  bike_id INT NOT NULL,
  time_start DATETIME NOT NULL,
  time_end DATETIME,
  start_station_id INT NOT NULL,
  end_station_id INT,
  trip_distance DOUBLE,
  CONSTRAINT trip_pk PRIMARY KEY(trip_id));

-- Adding foreign keys
ALTER TABLE bike ADD CONSTRAINT bike_fk1 FOREIGN KEY(type_id) REFERENCES bike_type(type_id);
ALTER TABLE bike ADD CONSTRAINT bike_fk2 FOREIGN KEY(station_id) REFERENCES docking_station(station_id);

ALTER TABLE bike_data ADD CONSTRAINT bike_data_fk FOREIGN KEY(bike_id) REFERENCES bike(bike_id);
ALTER TABLE bike_data ADD CONSTRAINT bike_data_fk2 FOREIGN KEY(trip_id) REFERENCES trip(trip_id);

ALTER TABLE repair ADD CONSTRAINT repair_fk FOREIGN KEY(bike_id) REFERENCES bike(bike_id);

ALTER TABLE station_data ADD CONSTRAINT station_data_fk FOREIGN KEY(station_id) REFERENCES docking_station(station_id);

ALTER TABLE trip ADD CONSTRAINT trip_fk1 FOREIGN KEY(customer_id) REFERENCES customer(customer_id);
ALTER TABLE trip ADD CONSTRAINT trip_fk2 FOREIGN KEY(bike_id) REFERENCES bike(bike_id);
ALTER TABLE trip ADD CONSTRAINT trip_fk3 FOREIGN KEY(start_station_id) REFERENCES docking_station(station_id);
ALTER TABLE trip ADD CONSTRAINT trip_fk4 FOREIGN KEY(end_station_id) REFERENCES docking_station(station_id);

-- Inserting data to the tables
-- Stations
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Lade station", 63.445900, 10.441570, 20);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Moholt station", 63.410468, 10.435377, 30);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Strindheim station", 63.427548, 10.458589, 50);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Singsaker station", 63.422290, 10.400251, 35);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Berg station", 63.414210, 10.417674, 25);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Teknobyen station", 63.416500, 10.398627, 55);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Lerkendal station", 63.412822, 10.407011, 40);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Øya station", 63.425031, 10.383407, 25);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Solsiden station", 63.434739, 10.411598, 65);
INSERT INTO docking_station (station_id, station_name, coords_lat, coords_lng, total_docks) VALUES(DEFAULT, "Ila station", 63.431244, 10.363101, 55);

-- Station data
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(1, "2018-03-22 12:50:00", 500);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(1, "2018-03-22 12:51:00", 520);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(1, "2018-03-22 12:52:00", 490);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(2, "2018-03-22 12:50:00", 700);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(2, "2018-03-22 12:51:00", 750);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(2, "2018-03-22 12:52:00", 810);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(3, "2018-03-22 12:50:00", 210);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(3, "2018-03-22 12:51:00", 195);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(3, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(4, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(5, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(6, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(7, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(8, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(9, "2018-03-22 12:52:00", 240);
INSERT INTO station_data (station_id, date_time, power_usage) VALUES(10, "2018-03-22 12:52:00", 240);

-- Customers
INSERT INTO customer (customer_id, firstname, surname) VALUES(111, "Herman", "Martinsen");
INSERT INTO customer (customer_id, firstname, surname) VALUES(222, "Trond", "Rondestvedt");
INSERT INTO customer (customer_id, firstname, surname) VALUES(333, "Jorgen", "Aasvestad");
INSERT INTO customer (customer_id, firstname, surname) VALUES(444, "Aria", "Bui");
INSERT INTO customer (customer_id, firstname, surname) VALUES(555, "Jingyi", "Li");
INSERT INTO customer (customer_id, firstname, surname) VALUES(666, "Shanshan", "Qu");

-- Admins
INSERT INTO admin_users VALUES (DEFAULT, "test@test.no", "Trond Jacob", "Rondis", "/2kOn3LNqLjfdxMMn67d1KwH6aodvjVlBTpphyy8MM0=","zyxUEbv3n6Chgi2uZ+rp2E42zwNs91DRb2Xg6gcGn0M=");
INSERT INTO admin_users VALUES (DEFAULT, "trond_jacob@hotmail.com", "Trond Jacob", "Rondestvedt", "VXIcZbhMjjzm1Mn+7yYmb4i8h9Dwis7QS13nf7wZEOk=","V2mQnKW+CCBtR5piCnH55sX/9bg/YqMibuKpey8OqJI=");
INSERT INTO admin_users VALUES (DEFAULT, "martinsenhr@gmail.com", "Herman Ryen", "Martinsen", "Evvl1WR8wO9G2m9l8cjhN4LKC2/EuEtW7d0CqQx4duk=","Byt9PT0wBSN0MBoEJv56g08On3gdI2j4Kiznf8vKrOc=");
INSERT INTO admin_users VALUES (DEFAULT, "jorgen96@hotmail.com", "Jørgen", "Aasvestad", "F1Svqro2skMh8xdfqmE1y9e8FMOZp2zXqgpMzJCLSq8=","vLqFM7z7DBdOZgCVpNxelMWv7EzXMjJp+ibbxbMzixY=");
INSERT INTO admin_users VALUES (DEFAULT, "joeylee0405@gmail.com", "Jingyi", "Lee", "v7in9XPUiqiHPtfC+eylLW6dDlxFzn6aqbuWHrQeUX8=","KVqU5xqnYyMDN/2nHg0yZ39H1Em+4psevcgbk3yaRZY=");
INSERT INTO admin_users VALUES (DEFAULT, "admin@team15.no", "admin", "user", "inKXLWAnlyYAXRaguPwSxT0FSyrBRMponZCQRVR0x3A=","2zVnrsFWhjoOJ5npsCUzktPtXscD18f2awp9atV4C+g=");

-- Types
INSERT INTO bike_type (type_id, type_name, rental_price) VALUES(DEFAULT, "Terrain bike", 200);
INSERT INTO bike_type (type_id, type_name, rental_price) VALUES(DEFAULT, "Street bike", 150);
INSERT INTO bike_type (type_id, type_name, rental_price) VALUES(DEFAULT, "Kids bike", 90);
INSERT INTO bike_type (type_id, type_name, rental_price) VALUES(DEFAULT, "Fat bike", 300);

-- Bikes
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1000, "2018-01-22", 2000, "Ferarri", 1, 1 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1001, "2018-01-22", 1000, "Renault", 1, 2 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1002, "2018-01-22", 1000, "Renault", 1, 3 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1003, "2018-01-22", 1000, "Renault", 1, 4 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1004, "2018-02-22", 1000, "Renault", 1, 5 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1005, "2018-02-22", 1000, "Renault", 1, 6 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1006, "2018-02-22", 2000, "Ferarri", 1, 7 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(1007, "2018-02-22", 1000, "Renault", 1, 8 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(2000, "2018-03-22", 1500, "Volvo"  , 2, 9 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(2001, "2018-03-22", 3000, "Datsun" , 2, 10);
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(2002, "2018-03-22", 1500, "Volvo"  , 2, 1 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(2003, "2018-03-22", 3000, "Datsun" , 2, 2 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(3000, "2018-04-22", 2000, "VW"     , 3, 3 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(3001, "2018-04-22", 2000, "VW"     , 3, 4 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(4000, "2018-04-22",  800, "Fiat"   , 4, 5 );
INSERT INTO bike (bike_id, purchase_date, price, make, type_id, station_id) VALUES(4001, "2018-04-22",  800, "Fiat"   , 4, 6 );



-- Bike Data
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1000, "2018-03-22 13:31:00", 63.410468, 10.435377, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1001, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1002, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1003, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1004, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1005, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1006, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(1007, "2018-03-22 13:30:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(2000, "2018-03-22 13:32:00", 63.427548, 10.458589, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(2001, "2018-03-22 13:30:00", 63.410468, 10.435377, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(2002, "2018-03-22 13:31:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(2003, "2018-03-22 13:32:00", 63.427548, 10.458589, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(3000, "2018-03-22 13:30:00", 63.427548, 10.458589, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(3001, "2018-03-22 13:31:00", 63.410468, 10.435377, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(4000, "2018-03-22 13:32:00", 63.445900, 10.441570, 100);
INSERT INTO bike_data (bike_id, date_time, coords_lat, coords_lng, charging_lvl) VALUES(4001, "2018-03-22 13:32:00", 63.445900, 10.441570, 100);

INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2017-12-03", "Desc", 1000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(1, 300, "2018-12-09", "Repair OK");
-- 2018 Januar
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-01-03", "Desc", 1000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(2, 150, "2018-01-09", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-01-11", "Desc", 4000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(3, 175, "2018-01-13", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-01-22", "Desc", 2000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(4, 60, "2018-01-18", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-01-22", "Desc", 2001);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(5, 300, "2018-01-27", "Repair OK");
-- 2018 Februar
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-02-03", "Desc", 1000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(6, 150, "2018-02-09", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-02-11", "Desc", 4000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(7, 175, "2018-02-13", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-02-22", "Desc", 2000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(8, 60, "2018-02-18", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-02-22", "Desc", 2001);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(9, 300, "2018-02-27", "Repair OK");
-- 2018 Mars
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-03-03", "Desc", 1000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(10, 150, "2018-03-09", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-03-11", "Desc", 4000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(11, 175, "2018-03-13", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-03-22", "Desc", 2000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(12, 60, "2018-03-18", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-03-22", "Desc", 2001);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(13, 300, "2018-03-27", "Repair OK");
-- 2018 April
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-03", "Desc", 1000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(14, 150, "2018-04-09", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-11", "Desc", 4000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(15, 175, "2018-04-13", "Repair OK");
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-15", "Desc", 2000);
INSERT INTO done_repair (repair_id, price, date_recieved, repair_desc) VALUES(16, 100, "2018-04-18", "Repair OK");

INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-18", "Desc", 2000);
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-18", "Desc", 2001);
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-19", "Desc", 2002);
INSERT INTO repair (repair_id, date_sent, request_desc, bike_id) VALUES(DEFAULT, "2018-04-19", "Desc", 1005);



-- 2018 Januar
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,1000,"2018-01-05 12:00:00","2018-01-05 16:20:00",1,2,2500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,1001,"2018-01-10 12:00:00","2018-01-10 16:00:00",2,3,2700);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,1002,"2018-01-15 12:00:00","2018-01-15 16:00:00",3,4,3400);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,1003,"2018-01-20 12:00:00","2018-01-20 16:00:00",5,6,4000);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,1004,"2018-01-25 12:00:00","2018-01-25 16:00:00",6,7,5500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,1005,"2018-01-28 12:00:00","2018-01-28 16:00:00",8,9,6000);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,3000,"2018-01-05 12:00:00","2018-01-05 16:00:00",6,7,6324);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,3001,"2018-01-10 12:00:00","2018-01-10 16:00:00",7,8,3455);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,4000,"2018-01-15 12:00:00","2018-01-15 16:00:00",9,10,6234);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,4001,"2018-01-20 12:00:00","2018-01-20 16:00:00",10,1,2657);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,3001,"2018-01-25 12:00:00","2018-01-25 16:00:00",8,2,7654);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,4001,"2018-01-28 12:00:00","2018-01-28 16:00:00",1,3,9875);
-- 2018 Februar
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,1006,"2018-02-05 12:00:00","2018-02-05 16:00:00",10,1,3560);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,1007,"2018-02-10 12:00:00","2018-02-10 16:00:00",1,2,4500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,2000,"2018-02-15 12:00:00","2018-02-15 16:00:00",2,3,6500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,2001,"2018-02-20 12:00:00","2018-02-20 16:00:00",3,4,2300);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,2002,"2018-02-25 12:00:00","2018-02-25 16:00:00",4,5,5500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,2003,"2018-02-28 12:00:00","2018-02-28 16:00:00",5,6,6000);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,1000,"2018-02-05 12:00:00","2018-02-05 16:20:00",1,2,2500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,1001,"2018-02-10 12:00:00","2018-02-10 16:00:00",2,3,2700);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,1002,"2018-02-15 12:00:00","2018-02-15 16:00:00",3,4,3400);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,1003,"2018-02-20 12:00:00","2018-02-20 16:00:00",5,6,4000);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,1004,"2018-02-25 12:00:00","2018-02-25 16:00:00",6,7,5500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,1005,"2018-02-28 12:00:00","2018-02-28 16:00:00",8,9,6000);
-- 2018 Mars
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,3000,"2018-03-05 12:00:00","2018-03-05 16:00:00",6,7,6324);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,3001,"2018-03-10 12:00:00","2018-03-10 16:00:00",7,8,3455);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,4000,"2018-03-15 12:00:00","2018-03-15 16:00:00",9,10,6234);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,4001,"2018-03-20 12:00:00","2018-03-20 16:00:00",10,1,2657);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,3001,"2018-03-25 12:00:00","2018-03-25 16:00:00",8,2,7654);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,4001,"2018-03-28 12:00:00","2018-03-28 16:00:00",1,3,9875);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,1000,"2018-03-05 12:00:00","2018-03-05 16:20:00",1,2,2500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,1001,"2018-03-10 12:00:00","2018-03-10 16:00:00",2,3,2700);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,1002,"2018-03-15 12:00:00","2018-03-15 16:00:00",3,4,3400);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,1003,"2018-03-20 12:00:00","2018-03-20 16:00:00",5,6,4000);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,1004,"2018-03-25 12:00:00","2018-03-25 16:00:00",6,7,5500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,1005,"2018-03-28 12:00:00","2018-03-28 16:00:00",8,9,6000);
-- 2018 April
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (111,1000,"2018-04-05 12:00:00","2018-04-05 16:00:00",2,4,3560);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (222,1001,"2018-04-10 12:00:00","2018-04-10 16:00:00",3,5,4500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (333,2002,"2018-04-15 12:00:00","2018-04-15 16:00:00",5,6,6500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (444,2003,"2018-04-20 12:00:00","2018-04-20 16:00:00",6,7,2300);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (555,2001,"2018-04-25 12:00:00","2018-04-25 16:00:00",4,8,5500);
INSERT INTO trip (customer_id, bike_id, time_start, time_end,start_station_id,end_station_id,trip_distance) VALUES (666,3000,"2018-04-28 12:00:00","2018-04-28 16:00:00",7,9,6000);

CREATE OR REPLACE VIEW active_bikes AS SELECT * FROM bike NATURAL JOIN bike_type WHERE active = TRUE;

CREATE OR REPLACE VIEW unfinished_repairs AS (SELECT r.* FROM repair r JOIN active_bikes b ON r.bike_id = b.bike_id LEFT JOIN done_repair dr ON r.repair_id = dr.repair_id WHERE dr.repair_id IS NULL);

CREATE OR REPLACE VIEW available_bikes AS SELECT b.* FROM active_bikes b NATURAL JOIN bike_type bt LEFT JOIN unfinished_repairs ur ON ur.bike_id = b.bike_id WHERE ur.bike_id IS NULL AND b.station_id IS NOT NULL;

CREATE OR REPLACE VIEW recent_bike_data AS SELECT bd.* FROM bike_data bd JOIN (SELECT bike_id, MAX(date_time) AS max_date FROM bike_data GROUP BY bike_id) gbd ON bd.bike_id = gbd.bike_id AND bd.date_time = gbd.max_date JOIN bike ON bd.bike_id = bike.bike_id WHERE active = true;

CREATE OR REPLACE VIEW travelling_bikes AS SELECT rbd.* FROM recent_bike_data rbd JOIN active_bikes ab ON ab.bike_id = rbd.bike_id LEFT JOIN unfinished_repairs ur ON ur.bike_id = rbd.bike_id WHERE ab.station_id IS NULL AND ur.bike_id IS NULL;

CREATE OR REPLACE VIEW repair_costs AS (SELECT YEAR(date_sent) AS year, MONTH(date_sent) AS month, SUM(price) AS costs FROM repair NATURAL JOIN done_repair GROUP BY year, month);

CREATE OR REPLACE VIEW bike_income AS (SELECT YEAR(time_start) AS year, MONTH(time_start) AS month, SUM((bike_type.rental_price/60)*TIMESTAMPDIFF(MINUTE,time_start, time_end)) AS income FROM bike_type NATURAL JOIN bike NATURAL JOIN trip GROUP BY year, month);

CREATE OR REPLACE VIEW bike_costs AS (SELECT YEAR(purchase_date) AS year, MONTH(purchase_date) AS month, SUM(price) as costs FROM bike GROUP BY year, month);
