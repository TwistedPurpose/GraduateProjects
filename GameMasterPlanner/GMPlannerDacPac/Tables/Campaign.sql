CREATE TABLE [dbo].[Campaign]
(
	[Id] INT identity NOT NULL,
	[Name] NVARCHAR(100) NULL,  
    [HistoryId] INT NULL,
	CONSTRAINT [PK_Campaign] PRIMARY KEY ([ID]),
    CONSTRAINT [FK_Campaign_ToHistory] FOREIGN KEY ([HistoryId]) REFERENCES [History]([Id])
)
