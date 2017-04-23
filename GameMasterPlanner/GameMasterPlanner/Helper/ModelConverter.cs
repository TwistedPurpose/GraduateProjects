using DataAccess.EntityFramework;
using DataAccess.Models;
using GameMasterPlanner.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GameMasterPlanner.Helper
{
    public static class ModelConverter
    {
        public static CharacterViewModel ToCharacterViewModel(Character dbCharacter)
        {
            CharacterViewModel vm = null;
            if (dbCharacter != null)
            {
                vm = new CharacterViewModel()
                {
                    Id = dbCharacter.Id,
                    CampaignId = dbCharacter.CampaignId,
                    CharDescription = dbCharacter.Description,
                    Name = dbCharacter.Name,
                    Notes = dbCharacter.Notes,
                    SessionList = dbCharacter.Sessions.Select(x => new SessionViewModel()
                    {
                        // add more values later?
                        Id = x.Id,
                        CampaignId = x.CampaignId,
                        Title = x.Title,
                        SessionNumber = x.SessionNumber
                    }).ToList()
                };
            }

            return vm;
        }

        public static Character ToDbCharacterModel(CharacterViewModel vm)
        {
            Character dbCharacter = null;
            if (vm != null)
            {
                dbCharacter = new Character()
                {
                    Id = vm.Id,
                    CampaignId = vm.CampaignId,
                    Name = String.IsNullOrWhiteSpace(vm.Name) ? String.Empty : vm.Name.Trim(),
                    Description = String.IsNullOrWhiteSpace(vm.CharDescription) ? String.Empty : vm.CharDescription.Trim(),
                    Notes = String.IsNullOrWhiteSpace(vm.Notes) ? String.Empty : vm.Notes.Trim(),

                };
            }

            return dbCharacter;
        }

        public static Session ToDbSessionModel(SessionViewModel vm)
        {
            Session dbSession = null;

            return dbSession;
        }

        public static SessionViewModel ToSessionViewModel(Session dbSession)
        {
            SessionViewModel vm = null;

            if (dbSession != null)
            {
                vm = new SessionViewModel()
                {
                    Id = dbSession.Id,
                    BaseMapId = dbSession.BaseMapId,
                    CampaignId = dbSession.CampaignId,
                    Notes = dbSession.Notes,
                    SessionNumber = dbSession.SessionNumber,
                    Title = dbSession.Title
                };
            }

            return vm;
        }
    }
}