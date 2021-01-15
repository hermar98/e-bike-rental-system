-- DROP EXISTING CONFLICTING TABLES
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

-- CREATING TABLES

CREATE TABLE admin_users (
  user_id       INTEGER     NOT NULL AUTO_INCREMENT,
  email        VARCHAR(100) NOT NULL,
  firstname VARCHAR(30) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  user_password VARCHAR(100) NOT NULL,
  salt         VARCHAR(100) NOT NULL,
  CONSTRAINT userid_pk PRIMARY KEY(user_id)
);

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
  charging_lvl DOUBLE NOT NULL,
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
  power_usage DOUBLE NOT NULL,
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

-- ADDING FOREIGN KEYS
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

-- CREATE VIEWS

CREATE OR REPLACE VIEW active_bikes AS SELECT * FROM bike NATURAL JOIN bike_type WHERE active = TRUE;

CREATE OR REPLACE VIEW unfinished_repairs AS (SELECT r.* FROM repair r JOIN active_bikes b ON r.bike_id = b.bike_id LEFT JOIN done_repair dr ON r.repair_id = dr.repair_id WHERE dr.repair_id IS NULL);

CREATE OR REPLACE VIEW available_bikes AS SELECT b.* FROM active_bikes b NATURAL JOIN bike_type bt LEFT JOIN unfinished_repairs ur ON ur.bike_id = b.bike_id WHERE ur.bike_id IS NULL AND b.station_id IS NOT NULL;

CREATE OR REPLACE VIEW recent_bike_data AS SELECT bd.* FROM bike_data bd JOIN (SELECT bike_id, MAX(date_time) AS max_date FROM bike_data GROUP BY bike_id) gbd ON bd.bike_id = gbd.bike_id AND bd.date_time = gbd.max_date JOIN bike ON bd.bike_id = bike.bike_id WHERE active = true;

CREATE OR REPLACE VIEW travelling_bikes AS SELECT rbd.* FROM recent_bike_data rbd JOIN active_bikes ab ON ab.bike_id = rbd.bike_id LEFT JOIN unfinished_repairs ur ON ur.bike_id = rbd.bike_id WHERE ab.station_id IS NULL AND ur.bike_id IS NULL;

CREATE OR REPLACE VIEW repair_costs AS (SELECT YEAR(date_sent) AS year, MONTH(date_sent) AS month, SUM(price) AS costs FROM repair NATURAL JOIN done_repair GROUP BY year, month);

CREATE OR REPLACE VIEW bike_income AS (SELECT YEAR(time_start) AS year, MONTH(time_start) AS month, SUM((bike_type.rental_price/60)*TIMESTAMPDIFF(MINUTE,time_start, time_end)) AS income FROM bike_type NATURAL JOIN bike NATURAL JOIN trip GROUP BY year, month);

CREATE OR REPLACE VIEW bike_costs AS (SELECT YEAR(purchase_date) AS year, MONTH(purchase_date) AS month, SUM(price) as costs FROM bike GROUP BY year, month);

INSERT INTO admin_users VALUES (DEFAULT, "admin@team15.no", "admin", "user", "inKXLWAnlyYAXRaguPwSxT0FSyrBRMponZCQRVR0x3A=","2zVnrsFWhjoOJ5npsCUzktPtXscD18f2awp9atV4C+g=");