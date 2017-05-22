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

insert into Campaign values('Dr. Larson and The Mystery of the Green Tomb', 'Too spooky!');
insert into Campaign values('Dr. Larson and The Longest Knight','I heard he is so tall he can touch Azure clouds!');
insert into Campaign values('Dr. Perl and the Ruby Python','Scripting will never be the same');

insert into Session values('Trained to kill: A locomotive mystery!',1,null,1,'Some interesting notes here!');
insert into Session values('A Dark and Stormy Knight',2,null,1,'Wow!  Other notes!');
insert into Session values('Off the Ruby Rails',3,null,1,'Wow!  Other notes!');

insert into Character values(1,null,'Dr. Larson','The hero!','Notes and stuff');
insert into Character values(1,null,'Soren','The other guy!!','Notes and stuff');

insert into CharacterSessions values(1,1);
insert into CharacterSessions values(2,1);
