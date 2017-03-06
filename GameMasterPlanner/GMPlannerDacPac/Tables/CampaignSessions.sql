CREATE TABLE [dbo].[CampaignSessions]
(
	[CampaignId] INT NOT NULL , 
    [SessionId] INT NOT NULL,
	PRIMARY KEY ([CampaignId], [SessionId])
)
