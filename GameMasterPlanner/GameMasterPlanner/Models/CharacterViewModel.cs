using DataAccess.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameMasterPlanner.Models
{
    public class CharacterViewModel
    {
        public int Id { get; set; }
        public int CampaignId { get; set; }
        // for associating new sessions
        public int SessionId { get; set; }
        public string Name { get; set; }
        public string CharDescription { get; set; }
        public string Notes { get; set; }
        public List<SessionViewModel> SessionList { get; set; }
    }
}