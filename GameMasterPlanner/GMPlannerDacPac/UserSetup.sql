CREATE LOGIN GMPlanner WITH PASSWORD = '0d<nVn>?Ae#.H@LDzu|-\w%/FpG@9fqo';
go

CREATE USER GMPlanner
        FOR LOGIN GMPlanner
        WITH DEFAULT_SCHEMA = dbo
GO

ALTER ROLE [db_datareader] ADD MEMBER GMPlanner;
go

ALTER ROLE [db_datawriter] ADD MEMBER GMPlanner;