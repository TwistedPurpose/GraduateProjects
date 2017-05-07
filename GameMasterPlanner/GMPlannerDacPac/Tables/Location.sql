CREATE TABLE [dbo].[Location]
(
	[Id] INT identity NOT NULL, 
    [ParentMapId] INT NOT NULL,
	[ChildMapId] INT NULL,
	[Name] Nvarchar(50) NOT NULL,
	[Description] Nvarchar(2000) NULL,
	CONSTRAINT [PK_Location] Primary Key ([Id]),
	CONSTRAINT [FK_Location_ToParentMap] FOREIGN KEY ([ParentMapId]) REFERENCES [Map]([Id]),
	CONSTRAINT [FK_Location_ToChildMap] FOREIGN KEY ([ChildMapId]) REFERENCES [Map]([Id])
)
