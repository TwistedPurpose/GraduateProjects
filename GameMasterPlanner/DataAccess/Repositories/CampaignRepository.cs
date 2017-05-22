using DataAccess.EntityFramework;
using System;
using System.Collections.Generic;
using System.Data.Entity;
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

        public Campaign CreateCampaign(Campaign campaign)
        {
            db.Campaigns.Add(campaign);
            db.SaveChanges();
            return campaign;
        }

        public void UpdateCampaign(Campaign campaign)
        {
            db.Campaigns.Attach(campaign);
            var campaignEntry = db.Entry(campaign);

            campaignEntry.State = EntityState.Modified;

            db.SaveChanges();
        }

    }
}
