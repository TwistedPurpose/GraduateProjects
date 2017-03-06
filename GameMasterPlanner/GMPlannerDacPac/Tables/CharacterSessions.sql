CREATE TABLE [dbo].[CharacterSessions]
(
	[CharacterId] INT NOT NULL , 
    [SessionId] INT NOT NULL, 
    PRIMARY KEY ([CharacterId], [SessionId])
)
