CREATE TABLE [dbo].[Locations]
(
	[Id] INT identity NOT NULL PRIMARY KEY, 
    [ParentMapId] INT NOT NULL,
	[ChildMapId] INT NULL,
	[Name] Nvarchar(50) NOT NULL,
	[Description] Nvarchar(2000) NULL
)
