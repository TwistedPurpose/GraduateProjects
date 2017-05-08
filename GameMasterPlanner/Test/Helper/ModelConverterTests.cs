using Microsoft.VisualStudio.TestTools.UnitTesting;
using GameMasterPlanner.Helper;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DataAccess.EntityFramework;
using Test.TestData;
using GameMasterPlanner.Models;

namespace GameMasterPlanner.Helper.Tests
{
    [TestClass()]
    public class ModelConverterTests
    {
        [TestMethod()]
        public void ToCharacterViewModelTest()
        {
            //Arrange
            Character dbCharacter = new Character() {
                Id = 1,
                CampaignId = 1,
                HistoryId = 1,
                Name = "Cassidy",
                Description = "A woman with a heart of gold, but ironically has no heart.  She has crazy red hair and is mean with a Guisarm.",
                Notes = "She has a mechanical heart because an evil fairy stole her real one!  She's on a quest to find a new heart. She's also known as Big T!",
                Sessions = SessionsTestData.getDbSessions()
            };

            //Act
            CharacterViewModel characterVM = ModelConverter.ToCharacterViewModel(dbCharacter);

            //Assert
            Assert.IsNotNull(characterVM);
            Assert.AreEqual(dbCharacter.Id, characterVM.Id);
            Assert.AreEqual(dbCharacter.CampaignId, characterVM.CampaignId);
            Assert.AreEqual(dbCharacter.Name, characterVM.Name);
            Assert.AreEqual(dbCharacter.Description, characterVM.CharDescription);
            Assert.AreEqual(dbCharacter.Notes, characterVM.Notes);
            Assert.AreEqual(dbCharacter.Sessions.Count, characterVM.SessionList.Count);
        }

        [TestMethod()]
        public void ToDbCharacterModelTest()
        {
            Assert.Fail();
        }

        [TestMethod()]
        public void ToDbSessionModelTest()
        {
            Assert.Fail();
        }

        [TestMethod()]
        public void ToSessionViewModelTest()
        {
            Assert.Fail();
        }

        [TestMethod()]
        public void ToItemViewModelTest()
        {
            Assert.Fail();
        }

        [TestMethod()]
        public void ToDbItemModelTest()
        {
            Assert.Fail();
        }


    }
}