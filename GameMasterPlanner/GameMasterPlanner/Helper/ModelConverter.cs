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
                    CharDescription = String.IsNullOrWhiteSpace(dbCharacter.Description) ? String.Empty : dbCharacter.Description.Trim(),
                    Name = String.IsNullOrWhiteSpace(dbCharacter.Name) ? String.Empty : dbCharacter.Name.Trim(),
                    Notes = String.IsNullOrWhiteSpace(dbCharacter.Notes) ? String.Empty : dbCharacter.Notes.Trim(),
                    SessionList = dbCharacter.Sessions.Select(x => new SessionViewModel()
                    {
                        // add more values later?
                        Id = x.Id,
                        CampaignId = x.CampaignId,
                        Title = String.IsNullOrWhiteSpace(x.Title) ? String.Empty : x.Title.Trim(),
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
                       // Add session conversion too?
                };
            }

            return dbCharacter;
        }

        public static Session ToDbSessionModel(SessionViewModel vm)
        {
            Session dbSession = null;
            
            if(vm != null)
            {
                dbSession = new Session()
                {
                    Id = vm.Id,
                    BaseMapId = vm.BaseMapId,
                    CampaignId = vm.CampaignId,
                    Notes = vm.Notes,
                    Title = vm.Title,
                    SessionNumber = vm.SessionNumber
                };
            }

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
                    Notes = String.IsNullOrWhiteSpace(dbSession.Notes) ? String.Empty : dbSession.Notes.Trim(),
                    SessionNumber = dbSession.SessionNumber,
                    Title = String.IsNullOrWhiteSpace(dbSession.Title) ? String.Empty : dbSession.Title.Trim()
                };
            }

            return vm;
        }

        public static ItemViewModel ToItemViewModel(Item dbItem)
        {
            ItemViewModel vm = null;
            
            if(dbItem != null)
            {
                vm = new ItemViewModel()
                {
                    Id = dbItem.Id,
                    CampaignId = dbItem.CampaignId,
                    Name = dbItem.Name.Trim(),
                    ItemDescription = String.IsNullOrWhiteSpace(dbItem.Description) ? String.Empty : dbItem.Description.Trim(),
                    Abilities = String.IsNullOrWhiteSpace(dbItem.Abilities) ? String.Empty : dbItem.Abilities.Trim(),
                    SessionList = dbItem.Sessions.Select(x => new SessionViewModel()
                    {
                        // add more values later?
                        Id = x.Id,
                        CampaignId = x.CampaignId,
                        Title = String.IsNullOrWhiteSpace(x.Title) ? String.Empty : x.Title.Trim(),
                        SessionNumber = x.SessionNumber
                    }).ToList()
                };
            }

            return vm;
        }

        public static Item ToDbItemModel(ItemViewModel itemVM)
        {
            Item dbItem = null;

            if(itemVM != null)
            {
                dbItem = new Item()
                {
                    Id = itemVM.Id,
                    Name = itemVM.Name,
                    CampaignId = itemVM.CampaignId,
                    Description = String.IsNullOrWhiteSpace(itemVM.ItemDescription) ? String.Empty : itemVM.ItemDescription.Trim(),
                    Abilities = String.IsNullOrWhiteSpace(itemVM.Abilities) ? String.Empty : itemVM.Abilities.Trim()
                };
            }

            return dbItem;
        }
    }
}