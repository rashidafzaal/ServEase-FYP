/*
SQLyog Ultimate v10.00 Beta1
MySQL - 5.6.21 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `request` (
	`R_ID` int ,
	`U_ID` int ,
	`P_ID` int ,
	`R_STATUS` int ,
	`DATE_TIME` datetime ,
	`RATING` int 
); 
insert into `request` (`R_ID`, `U_ID`, `P_ID`, `R_STATUS`, `DATE_TIME`, `RATING`) values('1','1','3','1',NULL,NULL);
