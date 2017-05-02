CREATE TABLE [dbo].[ItemSession]
(
	[ItemId] INT NOT NULL, 
    [SessionId] INT NOT NULL,
	CONSTRAINT [FK_Items_Session_ItemId] FOREIGN KEY(ItemId) REFERENCES Items(Id),
    CONSTRAINT [FK_Items_Session_SessionId] FOREIGN KEY(SessionId) REFERENCES Session(Id), 
    CONSTRAINT [PK_ItemSessions] PRIMARY KEY ([ItemId], [SessionId])
)
