CREATE TABLE [dbo].[Map]
(
	[Id] INT identity NOT NULL PRIMARY KEY,
	[ParentMapId] INT NULL,
	[Name] nvarchar(200) NOT NULL, 
    [Image] VARBINARY(MAX) NULL, 
    [ImageType] NVARCHAR(100) NULL,
)
