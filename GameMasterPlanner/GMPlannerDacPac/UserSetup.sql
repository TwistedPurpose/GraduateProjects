--CREATE LOGIN [IIS APPPOOL\DefaultAppPool] WITH PASSWORD = '0d<nVn>?Ae#.H@LDzu|-\w%/FpG@9fqo';
--go

--CREATE USER [IIS APPPOOL\DefaultAppPool]
--        FOR LOGIN [IIS APPPOOL\DefaultAppPool]
--        WITH DEFAULT_SCHEMA = dbo
--GO

--ALTER ROLE [db_datareader] ADD MEMBER [IIS APPPOOL\DefaultAppPool];
--go

--ALTER ROLE [db_datawriter] ADD MEMBER [IIS APPPOOL\DefaultAppPool];