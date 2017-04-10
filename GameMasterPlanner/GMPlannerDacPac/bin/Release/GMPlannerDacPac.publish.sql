﻿/*
Deployment script for GMPlannerDB

This code was generated by a tool.
Changes to this file may cause incorrect behavior and will be lost if
the code is regenerated.
*/

GO
SET ANSI_NULLS, ANSI_PADDING, ANSI_WARNINGS, ARITHABORT, CONCAT_NULL_YIELDS_NULL, QUOTED_IDENTIFIER ON;

SET NUMERIC_ROUNDABORT OFF;


GO
:setvar DatabaseName "GMPlannerDB"
:setvar DefaultFilePrefix "GMPlannerDB"
:setvar DefaultDataPath ""
:setvar DefaultLogPath ""

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
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET ANSI_NULLS ON,
                ANSI_PADDING ON,
                ANSI_WARNINGS ON,
                ARITHABORT ON,
                CONCAT_NULL_YIELDS_NULL ON,
                NUMERIC_ROUNDABORT OFF,
                QUOTED_IDENTIFIER ON,
                ANSI_NULL_DEFAULT ON,
                CURSOR_CLOSE_ON_COMMIT OFF,
                AUTO_CREATE_STATISTICS ON,
                AUTO_SHRINK OFF,
                AUTO_UPDATE_STATISTICS ON,
                RECURSIVE_TRIGGERS OFF 
            WITH ROLLBACK IMMEDIATE;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET ALLOW_SNAPSHOT_ISOLATION OFF;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET AUTO_UPDATE_STATISTICS_ASYNC OFF,
                DATE_CORRELATION_OPTIMIZATION OFF 
            WITH ROLLBACK IMMEDIATE;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET AUTO_CREATE_STATISTICS ON(INCREMENTAL = OFF) 
            WITH ROLLBACK IMMEDIATE;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET QUERY_STORE (QUERY_CAPTURE_MODE = AUTO, OPERATION_MODE = READ_WRITE, FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_PLANS_PER_QUERY = 200, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), MAX_STORAGE_SIZE_MB = 100) 
            WITH ROLLBACK IMMEDIATE;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
        ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
        ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
        ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
        ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
        ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
        ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
        ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
    END


GO
IF EXISTS (SELECT 1
           FROM   [sys].[databases]
           WHERE  [name] = N'$(DatabaseName)')
    BEGIN
        ALTER DATABASE [$(DatabaseName)]
            SET TEMPORAL_HISTORY_RETENTION ON 
            WITH ROLLBACK IMMEDIATE;
    END


GO
PRINT N'Creating [dbo].[CampaignSessions]...';


GO
CREATE TABLE [dbo].[CampaignSessions] (
    [CampaignId] INT NOT NULL,
    [SessionId]  INT NOT NULL,
    PRIMARY KEY CLUSTERED ([CampaignId] ASC, [SessionId] ASC)
);


GO
PRINT N'Creating [dbo].[CharacterSessions]...';


GO
CREATE TABLE [dbo].[CharacterSessions] (
    [CharacterId] INT NOT NULL,
    [SessionId]   INT NOT NULL,
    CONSTRAINT [PK_CharacterSessions] PRIMARY KEY CLUSTERED ([CharacterId] ASC, [SessionId] ASC)
);


GO
PRINT N'Creating [dbo].[Items]...';


GO
CREATE TABLE [dbo].[Items] (
    [Id]          INT           IDENTITY (1, 1) NOT NULL,
    [Name]        NCHAR (50)    NULL,
    [Description] NVARCHAR (50) NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[Character]...';


GO
CREATE TABLE [dbo].[Character] (
    [Id]          INT           IDENTITY (1, 1) NOT NULL,
    [HistoryId]   INT           NULL,
    [Name]        NVARCHAR (50) NOT NULL,
    [Description] NCHAR (1000)  NULL,
    [Notes]       NCHAR (1000)  NULL,
    CONSTRAINT [PK_Character] PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[Locations]...';


GO
CREATE TABLE [dbo].[Locations] (
    [Id]          INT             IDENTITY (1, 1) NOT NULL,
    [ParentMapId] INT             NOT NULL,
    [ChildMapId]  INT             NULL,
    [Name]        NVARCHAR (50)   NOT NULL,
    [Description] NVARCHAR (2000) NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[Map]...';


GO
CREATE TABLE [dbo].[Map] (
    [Id]          INT           IDENTITY (1, 1) NOT NULL,
    [ParentMapId] INT           NULL,
    [Name]        NVARCHAR (50) NOT NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[Session]...';


GO
CREATE TABLE [dbo].[Session] (
    [Id]            INT            IDENTITY (1, 1) NOT NULL,
    [Title]         NVARCHAR (40)  NULL,
    [CampaignId]    INT            NULL,
    [BaseMapId]     INT            NULL,
    [SessionNumber] INT            NOT NULL,
    [Notes]         NVARCHAR (MAX) NULL,
    CONSTRAINT [PK_Session] PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[History]...';


GO
CREATE TABLE [dbo].[History] (
    [Id]          INT            IDENTITY (1, 1) NOT NULL,
    [Description] NVARCHAR (500) NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[Campaign]...';


GO
CREATE TABLE [dbo].[Campaign] (
    [Id]        INT            IDENTITY (1, 1) NOT NULL,
    [Name]      NVARCHAR (100) NULL,
    [HistoryId] INT            NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[FK_Character_Session_CharacterId]...';


GO
ALTER TABLE [dbo].[CharacterSessions]
    ADD CONSTRAINT [FK_Character_Session_CharacterId] FOREIGN KEY ([CharacterId]) REFERENCES [dbo].[Character] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Character_Session_SessionId]...';


GO
ALTER TABLE [dbo].[CharacterSessions]
    ADD CONSTRAINT [FK_Character_Session_SessionId] FOREIGN KEY ([SessionId]) REFERENCES [dbo].[Session] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Session_ToMap]...';


GO
ALTER TABLE [dbo].[Session]
    ADD CONSTRAINT [FK_Session_ToMap] FOREIGN KEY ([BaseMapId]) REFERENCES [dbo].[Map] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Session_ToCampaign]...';


GO
ALTER TABLE [dbo].[Session]
    ADD CONSTRAINT [FK_Session_ToCampaign] FOREIGN KEY ([CampaignId]) REFERENCES [dbo].[Campaign] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Campaign_ToHistory]...';


GO
ALTER TABLE [dbo].[Campaign]
    ADD CONSTRAINT [FK_Campaign_ToHistory] FOREIGN KEY ([HistoryId]) REFERENCES [dbo].[History] ([Id]);


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

insert into Campaign values('Dr. Larson and The Mystery of the Green Tomb',1);
insert into Campaign values('Dr. Larson and The Longest Knight',2);

insert into Session values('Trained to kill: A locomotive mystery!',1,null,1,'Some interesting notes here!');
insert into Session values('A Dark and Stormy Knight',2,null,1,'Wow!  Other notes!');

insert into Character values(null,'Dr. Larson','The hero!','Notes and stuff');
insert into Character values(null,'Soren','The other guy!!','Notes and stuff');

insert into CharacterSessions values(1,1);
insert into CharacterSessions values(2,1);
GO

GO
DECLARE @VarDecimalSupported AS BIT;

SELECT @VarDecimalSupported = 0;

IF ((ServerProperty(N'EngineEdition') = 3)
    AND (((@@microsoftversion / power(2, 24) = 9)
          AND (@@microsoftversion & 0xffff >= 3024))
         OR ((@@microsoftversion / power(2, 24) = 10)
             AND (@@microsoftversion & 0xffff >= 1600))))
    SELECT @VarDecimalSupported = 1;

IF (@VarDecimalSupported > 0)
    BEGIN
        EXECUTE sp_db_vardecimal_storage_format N'$(DatabaseName)', 'ON';
    END


GO
PRINT N'Update complete.';


GO
