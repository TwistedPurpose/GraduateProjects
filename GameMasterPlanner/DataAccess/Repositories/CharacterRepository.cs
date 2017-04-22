using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class CharacterRepository : BaseRepository
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="id">Campaign Id</param>
        /// <returns></returns>
        public List<Character> GetAllCharacters(int campaignId)
        {
            var list = from characters in db.Characters
                       where characters.CampaignId == campaignId
                       select characters;

            return list.ToList();
        }

        public List<Character> GetCharactersInSession(int sessionId)
        {
            var list = from session in db.Sessions
                       where session.Id == sessionId
                       select session.Characters;

            return list.FirstOrDefault().ToList();
        }

        public Character GetCharacter(int characterId)
        {
            return (from character in db.Characters
                    where character.Id == characterId
                    select character).FirstOrDefault();
        }

        public Character CreateCharacter(Character character)
        {
            db.Characters.Add(character);
            db.SaveChanges();
            return character;
        }

        public void AddCharacterToSession(int characterId, int sessionId)
        {
            Session s = (from session in db.Sessions
                          where session.Id == sessionId
                         select session).FirstOrDefault();

            Character c = (from character in db.Characters
                           where character.Id == characterId
                           select character).FirstOrDefault();

            s.Characters.Add(c);

            db.SaveChanges();
        }

        public void UpdateCharacter(Character dbCharacter)
        {
            db.Characters.Attach(dbCharacter);
            var entry = db.Entry(dbCharacter);

            entry.State = EntityState.Modified;

            //entry.Property(e => e.CampaignId).IsModified = false;

            db.SaveChanges();
        }
    }
}
