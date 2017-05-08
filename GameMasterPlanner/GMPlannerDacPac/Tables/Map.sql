CREATE TABLE [dbo].[Map]
(
	[Id] INT identity NOT NULL PRIMARY KEY,
	[ParentMapId] INT NULL,
	[Name] nvarchar(100) NOT NULL, 
    [Image] VARBINARY(MAX) NULL,
)
