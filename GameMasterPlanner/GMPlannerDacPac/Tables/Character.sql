CREATE TABLE [dbo].[Character]
(
	[Id] INT identity NOT NULL ,
	[HistoryId] INT NULL,
	[Name] NVARCHAR(50) NOT NULL, 
    [Description] NCHAR(1000) NULL, 
    [Notes] NCHAR(1000) NULL, 
    CONSTRAINT [PK_Character] PRIMARY KEY ([Id]) 


)
