CREATE DATABASE CustomerDetails;
use CustomerDetails;

CREATE TABLE customer(
                         cusId VARCHAR(10) NOT NULL,
                         cusName VARCHAR(30),
                         cusAddress VARCHAR(30),
                         cusSalary DECIMAL(10)
);
