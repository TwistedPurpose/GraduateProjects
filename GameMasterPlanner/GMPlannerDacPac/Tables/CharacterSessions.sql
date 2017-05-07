CREATE TABLE [dbo].[CharacterSessions]
(
	[CharacterId] INT NOT NULL , 
    [SessionId] INT NOT NULL, 
	CONSTRAINT [FK_Character_Session_CharacterId] FOREIGN KEY(CharacterId) REFERENCES [Character](Id),
    CONSTRAINT [FK_Character_Session_SessionId] FOREIGN KEY(SessionId) REFERENCES [Session](Id), 
    CONSTRAINT [PK_CharacterSessions] PRIMARY KEY ([CharacterId], [SessionId])
)
