CREATE TABLE [dbo].[Map]
(
	[Id] INT identity NOT NULL PRIMARY KEY,
	[ParentMapId] INT NULL,
	[Name] nvarchar(50) NOT NULL,
)
