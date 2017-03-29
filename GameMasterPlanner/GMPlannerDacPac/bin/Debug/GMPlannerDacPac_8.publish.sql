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
                QUOTED_IDENTIFIER ON,
                ANSI_NULL_DEFAULT ON 
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
PRINT N'Rename refactoring operation with key b306fb9b-5912-4292-9222-df10bbb06115 is skipped, element [dbo].[CharacterSessions].[Id] (SqlSimpleColumn) will not be renamed to CharacterId';


GO
PRINT N'Rename refactoring operation with key bf83a970-9dc2-4c7b-8616-6453e70e2ee9 is skipped, element [dbo].[CampaignSessions].[Id] (SqlSimpleColumn) will not be renamed to CampaignId';


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
PRINT N'Creating [dbo].[CampaignSessions]...';


GO
CREATE TABLE [dbo].[CampaignSessions] (
    [CampaignId] INT NOT NULL,
    [SessionId]  INT NOT NULL,
    PRIMARY KEY CLUSTERED ([CampaignId] ASC, [SessionId] ASC)
);


GO
PRINT N'Creating [dbo].[Character]...';


GO
CREATE TABLE [dbo].[Character] (
    [Id]        INT           IDENTITY (1, 1) NOT NULL,
    [HistoryId] INT           NULL,
    [Name]      NVARCHAR (50) NOT NULL,
    CONSTRAINT [PK_Character] PRIMARY KEY CLUSTERED ([Id] ASC)
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
PRINT N'Creating [dbo].[History]...';


GO
CREATE TABLE [dbo].[History] (
    [Id]          INT            IDENTITY (1, 1) NOT NULL,
    [Description] NVARCHAR (500) NULL,
    PRIMARY KEY CLUSTERED ([Id] ASC)
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
    [CampaignId]    INT            NULL,
    [BaseMapId]     INT            NULL,
    [SessionNumber] INT            NULL,
    [Notes]         NVARCHAR (MAX) NULL,
    CONSTRAINT [PK_Session] PRIMARY KEY CLUSTERED ([Id] ASC)
);


GO
PRINT N'Creating [dbo].[FK_Campaign_ToHistory]...';


GO
ALTER TABLE [dbo].[Campaign] WITH NOCHECK
    ADD CONSTRAINT [FK_Campaign_ToHistory] FOREIGN KEY ([HistoryId]) REFERENCES [dbo].[History] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Character_Session_CharacterId]...';


GO
ALTER TABLE [dbo].[CharacterSessions] WITH NOCHECK
    ADD CONSTRAINT [FK_Character_Session_CharacterId] FOREIGN KEY ([CharacterId]) REFERENCES [dbo].[Character] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Character_Session_SessionId]...';


GO
ALTER TABLE [dbo].[CharacterSessions] WITH NOCHECK
    ADD CONSTRAINT [FK_Character_Session_SessionId] FOREIGN KEY ([SessionId]) REFERENCES [dbo].[Session] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Session_ToMap]...';


GO
ALTER TABLE [dbo].[Session] WITH NOCHECK
    ADD CONSTRAINT [FK_Session_ToMap] FOREIGN KEY ([BaseMapId]) REFERENCES [dbo].[Map] ([Id]);


GO
PRINT N'Creating [dbo].[FK_Session_ToCampaign]...';


GO
ALTER TABLE [dbo].[Session] WITH NOCHECK
    ADD CONSTRAINT [FK_Session_ToCampaign] FOREIGN KEY ([CampaignId]) REFERENCES [dbo].[Campaign] ([Id]);


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

GO

GO
