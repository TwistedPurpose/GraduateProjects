CREATE TABLE [dbo].[Character]
(
	[Id] INT identity NOT NULL,
	[CampaignId] INT NOT NULL,
	[HistoryId] INT NULL,
	[Name] NVARCHAR(50) NOT NULL, 
    [Description] NCHAR(1000) NULL, 
    [Notes] NCHAR(1000) NULL, 
    CONSTRAINT [PK_Character] PRIMARY KEY ([Id]), 
    CONSTRAINT [FK_Character_Campaign] FOREIGN KEY (CampaignId) REFERENCES Campaign(Id)
)
