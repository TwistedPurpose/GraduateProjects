CREATE TABLE [dbo].[Items]
(
	[Id] INT identity NOT NULL PRIMARY KEY,
	[CampaignId] INT NOT NULL,
    [Name] NCHAR(50) NOT NULL, 
    [Description] NVARCHAR(2000) NULL, 
    [Abilities] NVARCHAR(2000) NULL, 
    CONSTRAINT [FK_Items_ToCampaign] FOREIGN KEY (CampaignId) REFERENCES Campaign(Id)
)
