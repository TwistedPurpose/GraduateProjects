using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Linq;

namespace DataAccess.Repositories
{

    public class CampaignRepository : BaseRepository
    {
        public List<Campaign> GetCampaignList()
        {
            var list = from campaigns in db.Campaigns
                       select campaigns;

            return list.ToList();
        }

        public void CreateCampaign(Campaign campaign)
        {
            db.Campaigns.Add(campaign);
            db.SaveChanges();
        }

        public void CreateHistory(History history)
        {
            db.Histories.Add(history);
            db.SaveChanges();
        }

    }
}
