CREATE TABLE [dbo].[Session]
(
	[Id] INT identity NOT NULL PRIMARY KEY, 
	[CampaignId] NCHAR(10) NULL,
	[BaseMapId] INT NULL, 
	[SessionNumber] INT NULL,
    [Notes] NVARCHAR(MAX) NULL
    
)
