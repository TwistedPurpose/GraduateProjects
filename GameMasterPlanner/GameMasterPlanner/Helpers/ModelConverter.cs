using DataAccess.EntityFramework;
using GameMasterPlanner.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameMasterPlanner.Helpers
{
    public static class ModelConverter
    {
        public static Character CharacterToEntity(CharacterViewModel characterOld)
        {
            return new Character()
            {
                Id = characterOld.Id,
                HistoryId = characterOld.History.
            };
        }
    }
}