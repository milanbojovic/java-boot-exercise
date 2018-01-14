CREATE TABLE SENSOR (
  ID IDENTITY PRIMARY KEY,
  SENSOR_PUBLIC_ID VARCHAR(100) NOT NULL
);

CREATE TABLE MEASUREMENT (
  ID IDENTITY PRIMARY KEY,
  SENSOR_ID BIGINT NOT NULL,
  VALUE DECIMAL NOT NULL,
  TIMESTAMP TIMESTAMP NOT NULL,
  CONSTRAINT FK_MEASUREMENT_SENSOR_ID FOREIGN KEY(SENSOR_ID) REFERENCES SENSOR(ID)
);

CREATE TABLE MEDIAN (
  ID IDENTITY PRIMARY KEY,
  SENSOR_ID BIGINT NOT NULL,
  VALUE DECIMAL NOT NULL,
  TIMESTAMP TIMESTAMP NOT NULL,
CONSTRAINT FK_MEDIAN_SENSOR_ID FOREIGN KEY(SENSOR_ID) REFERENCES SENSOR(ID)
);