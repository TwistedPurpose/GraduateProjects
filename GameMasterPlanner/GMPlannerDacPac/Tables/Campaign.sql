CREATE TABLE [dbo].[Campaign]
(
	[Id] INT identity NOT NULL PRIMARY KEY,
	[Name] NVARCHAR(100) NULL,  
    [HistoryId] INT NULL,
    CONSTRAINT [FK_Campaign_ToHistory] FOREIGN KEY ([HistoryId]) REFERENCES [History]([Id])
)
