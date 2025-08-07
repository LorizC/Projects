create database bankmanagementsystem;
show databases;
use bankmanagementsystem;
show tables;
SELECT * from signup;
SELECT * from login;
SELECT * from TransactionHistory;
CREATE TABLE signup(
formno VARCHAR(20) PRIMARY KEY,
name VARCHAR(50),
bdate VARCHAR(20),
cardnumber VARCHAR(20) UNIQUE,
pinnumber VARCHAR(10),
security_question VARCHAR(255),
security_answer VARCHAR(255)
);
CREATE TABLE login(
formNumber VARCHAR(20),
cardnumber VARCHAR(25) PRIMARY KEY,
pinnumber VARCHAR(10),
FOREIGN KEY (formNumber) REFERENCES signup(formno)
);
CREATE TABLE TransactionHistory (
id INT AUTO_INCREMENT PRIMARY KEY,
cardnumber VARCHAR(25) NOT NULL,
date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
amount INT NOT NULL,
type VARCHAR(20) NOT NULL,
balance INT NOT NULL DEFAULT 0,
FOREIGN KEY (cardnumber) REFERENCES login(cardnumber)
);
SHOW CREATE TABLE TransactionHistory;