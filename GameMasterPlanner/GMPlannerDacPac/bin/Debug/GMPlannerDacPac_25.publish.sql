﻿/*
Deployment script for gm_planner_db

This code was generated by a tool.
Changes to this file may cause incorrect behavior and will be lost if
the code is regenerated.
*/

GO
SET ANSI_NULLS, ANSI_PADDING, ANSI_WARNINGS, ARITHABORT, CONCAT_NULL_YIELDS_NULL, QUOTED_IDENTIFIER ON;

SET NUMERIC_ROUNDABORT OFF;


GO
:setvar DatabaseName "gm_planner_db"
:setvar DefaultFilePrefix "gm_planner_db"
:setvar DefaultDataPath "D:\Program Files\Microsoft SQL Server\MSSQL13.MSSQLSERVER\MSSQL\DATA\"
:setvar DefaultLogPath "D:\Program Files\Microsoft SQL Server\MSSQL13.MSSQLSERVER\MSSQL\DATA\"

GO
:on error exit
GO
/*
Detect SQLCMD mode and disable script execution if SQLCMD mode is not supported.
To re-enable the script after enabling SQLCMD mode, execute the following:
SET NOEXEC OFF; 
*/
:setvar __IsSqlCmdEnabled "True"
GO
IF N'$(__IsSqlCmdEnabled)' NOT LIKE N'True'
    BEGIN
        PRINT N'SQLCMD mode must be enabled to successfully execute this script.';
        SET NOEXEC ON;
    END


GO
USE [$(DatabaseName)];


GO
/*
Post-Deployment Script Template							
--------------------------------------------------------------------------------------
 This file contains SQL statements that will be appended to the build script.		
 Use SQLCMD syntax to include a file in the post-deployment script.			
 Example:      :r .\myfile.sql								
 Use SQLCMD syntax to reference a variable in the post-deployment script.		
 Example:      :setvar TableName MyTable							
               SELECT * FROM [$(TableName)]					
--------------------------------------------------------------------------------------
*/

insert into History values('A sweet history');
insert into History values('The hero of Seattle, the one they call... Eric!');
insert into History values('Scripting languages battle it out!');

insert into Campaign values('Dr. Larson and The Mystery of the Green Tomb',1);
insert into Campaign values('Dr. Larson and The Longest Knight',2);
insert into Campaign values('Dr. Perl and the Ruby Python',3)

insert into Session values('Trained to kill: A locomotive mystery!',1,null,1,'Some interesting notes here!');
insert into Session values('A Dark and Stormy Knight',2,null,1,'Wow!  Other notes!');
insert into Session values('Off the Ruby Rails',3,null,1,'Wow!  Other notes!');

insert into Character values(1,null,'Dr. Larson','The hero!','Notes and stuff');
insert into Character values(1,null,'Soren','The other guy!!','Notes and stuff');

insert into CharacterSessions values(1,1);
insert into CharacterSessions values(2,1);
GO

GO
PRINT N'Update complete.';


GO