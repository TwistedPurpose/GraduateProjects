using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using DataAccess.Models;

namespace GameMasterPlanner.Models
{
    public class ItemViewModel
    {
        public int Id { get; set; }
        public int CampaignId { get; set; }
        public int SessionId { get; set; }
        public string Name { get; set; }
        public string ItemDescription { get; set; }
        public string Abilities { get; set; }
        public List<SessionViewModel> SessionList { get; internal set; }
    }
}