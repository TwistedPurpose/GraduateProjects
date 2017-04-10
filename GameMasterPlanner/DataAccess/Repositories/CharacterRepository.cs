using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
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
            return new List<Character>();
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

        public Character AddNewCharacter(Character character)
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
    }
}
