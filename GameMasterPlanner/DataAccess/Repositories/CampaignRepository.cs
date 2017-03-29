using DataAccess.EntityFramework;
using DataAccess.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace DataAccess.Repositories
{
    public class CampaignRepository
    {
        public List<CampaignViewModel> GetCampaignList()
        {

            using (var db = new GameMasterPlannerDBEntities())
            {

                var list = from campaigns in db.Campaigns
                           select new CampaignViewModel()
                           {
                               Id = campaigns.Id,
                               Name = campaigns.Name,
                               History = campaigns.History.Description
                           };


                return list.ToList();
            }
        }

        public void CreateCampaign(CampaignViewModel campaign)
        {
            using (var db = new GameMasterPlannerDBEntities())
            {
                Campaign campaignDb = new Campaign()
                {
                    Name = campaign.Name
                };

                if (!String.IsNullOrWhiteSpace(campaign.History))
                {

                    var campaignHistory = new History()
                    {
                        Description = campaign.History
                    };
                    campaignDb.History = campaignHistory;
                    db.Histories.Add(campaignHistory);
                    db.SaveChanges();
                }
                db.Campaigns.Add(campaignDb);
                db.SaveChanges();
            }
        }
    }
}
