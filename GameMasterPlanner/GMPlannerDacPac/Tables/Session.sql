CREATE TABLE [dbo].[Session]
(
	[Id] INT identity NOT NULL,
	[Title] NVARCHAR(200) NULL, 
	[CampaignId] Int NOT NULL,
	[BaseMapId] INT NULL, 
	[SessionNumber] INT NOT NULL,
    [Notes] NVARCHAR(MAX) NULL, 
    CONSTRAINT [FK_Session_ToMap] FOREIGN KEY ([BaseMapId]) REFERENCES [Map]([Id]), 
    CONSTRAINT [FK_Session_ToCampaign] FOREIGN KEY ([CampaignId]) REFERENCES [Campaign]([Id]),
	--CONSTRAINT [UQ_Session_SessionNumber] UNIQUE ([CampaignId],[SessionNumber]),
    CONSTRAINT [PK_Session] PRIMARY KEY ([Id])
    
)
