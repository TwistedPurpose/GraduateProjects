CREATE TABLE [dbo].[Campaign]
(
	[Id] INT identity NOT NULL,
	[Name] NVARCHAR(100) NULL,  
    [History] NVARCHAR(3000) NULL,
	CONSTRAINT [PK_Campaign] PRIMARY KEY ([Id])
)
