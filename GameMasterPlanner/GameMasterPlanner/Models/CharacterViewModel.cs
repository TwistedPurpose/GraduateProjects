using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameMasterPlanner.Models
{
    public class CharacterViewModel
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string History { get; set; }
        public string Description { get; set; }
        public string Notes { get; set; }
    
    }
}