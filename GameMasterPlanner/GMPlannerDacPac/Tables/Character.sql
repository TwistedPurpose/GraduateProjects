CREATE TABLE [dbo].[Character]
(
	[Id] INT identity NOT NULL ,
	[HistoryId] INT NULL,
	[Name] NVARCHAR(50) NOT NULL, 
    CONSTRAINT [PK_Character] PRIMARY KEY ([Id]) 


)
