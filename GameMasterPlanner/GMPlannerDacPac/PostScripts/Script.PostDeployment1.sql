/*
Post-Deployment Script Template							
--------------------------------------------------------------------------------------
 This file contains SQL statements that will be appended to the build script.		
 Use SQLCMD syntax to include a file in the post-deployment script.			
 Example:      :r .\myfile.sql								
 Use SQLCMD syntax to reference a variable in the post-deployment script.		
 Example:      :setvar TableName MyTable							
               SELECT * FROM [$(TableName)]					
--------------------------------------------------------------------------------------
*/

insert into History values('A sweet history');
insert into History values('The hero of Seattle, the one they call... Eric!');

insert into Campaign values('Dr. Larson and The Mystery of the Green Tomb',1);
insert into Campaign values('Dr. Larson and The Longest Knight',2);

