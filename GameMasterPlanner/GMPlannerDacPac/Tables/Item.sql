CREATE TABLE [dbo].[Item]
(
	[Id] INT identity NOT NULL,
	[CampaignId] INT NOT NULL,
    [Name] NCHAR(50) NOT NULL, 
    [Description] NVARCHAR(2000) NULL, 
    [Abilities] NVARCHAR(2000) NULL, 
	CONSTRAINT [PK_Item] PRIMARY KEY ([Id]), 
    CONSTRAINT [FK_Item_ToCampaign] FOREIGN KEY (CampaignId) REFERENCES Campaign(Id)
)
