class HubViewModel {

    constructor(campaignId) {
        let self = this;

        this.CampaignId = campaignId;
        this.SessionList = ko.observableArray([]);

        this.CurrentSession = ko.observable();

        this.ItemList = ko.observableArray([]);
        this.CharacterList = ko.observableArray([]);
        this.LocationList = ko.observableArray([]);
        this.OrganizationList = ko.observableArray([]);

        let map = new GMaps({
            div: '#map',
            lat: 52.4801,
            lng: -1.8835,
            width: '100%',
            height: '500px',
            zoom: 7
        });

        map.setContextMenu({
            control: 'map',
            options: [{
                title: 'Add marker',
                name: 'add_marker',
                action: function (e) {
                    this.addMarker({
                        lat: e.latLng.lat(),
                        lng: e.latLng.lng(),
                        title: 'New marker'
                    });
                }
            }, {
                title: 'Center here',
                name: 'center_here',
                action: function (e) {
                    this.setCenter(e.latLng.lat(), e.latLng.lng());
                }
            }]
        });

        this.Map = map;

        this.showAddEditCharacterModal = ko.observable(false);
        this.existingCharacterModal = ko.observable(false);

        this.showItemModal = ko.observable(false);
        this.existingItemsModal = ko.observable(false);

        this.addEditCharacterVM = new CharacterViewModel(null);
        this.existingCharacterVM = new CharacterListViewModel(null);

        this.addEditItemVM = new ItemViewModel(null);
        this.existingItemsVM = new ItemListViewModel(null);

        this.editCharacter = (character) => {
            this.addEditCharacterVM.setupToEdit(character);
            this.showAddEditCharacterModal(true);
        };

        this.editItem = (item) => {
            this.addEditItemVM.setupToEdit(item);
            this.showItemModal(true);
        };

        $('#addCharacterModal').on('hidden.bs.modal', function () {
            self.addEditItemVM.clear();
        });

        $('#addCharacterModal').on('hidden.bs.modal', function () {
            self.addEditCharacterVM.clear();
        });

        self.CurrentSession.subscribe(function (newValue) {
            self.CharacterList.removeAll();
            self.ItemList.removeAll();

            if (self.CurrentSession().Id) {
                $.getJSON(baseURL + 'api/Character/GetSessionCharacters?sessionId=' + self.CurrentSession().Id, function (data) {
                    data.forEach(function (characterData) {
                        self.CharacterList.push(new CharacterViewModel(characterData));
                    });
                });

                $.getJSON(baseURL + 'api/Item/GetAllInSession?sessionId=' + self.CurrentSession().Id, function (itemsList) {
                    itemsList.forEach(function (item) {
                        self.ItemList.push(new ItemViewModel(item));
                    });
                });
            }
        });

    }

    saveSession() {
        let self = this;

        let session = self.CurrentSession();

        let data = [{
            Id: session.Id,
            CampaignId: session.CampaignId,
            SessionNumber: session.SessionNumber(),
            Notes: session.Notes(),
            Title: session.Title()
        }];

        $.ajax({
            url: baseURL + 'api/Session',
            type: 'POST',
            data: JSON.stringify(data),
            dataType: 'json',
            contentType: 'application/json'
        });
    }

    newSession() {
        let self = this;

        let newSession = new SessionViewModel();
        newSession.CampaignId = self.CampaignId;

        $.ajax({
            url: baseURL + 'api/Session',
            type: 'POST',
            data: JSON.stringify([newSession.toJson()]),
            dataType: 'json',
            contentType: 'application/json',
            success: function (sessionList) {
                if (sessionList && sessionList.length > 0) {
                    newSession = new SessionViewModel(sessionList[0]);
                    self.SessionList.push(newSession);
                    self.CurrentSession(newSession);
                }
            }
        });
    }

    // Helper for setting up character list
    setupCharacterListForAssociation() {
        self = this;

        self.existingCharacterVM.CharacterList.removeAll();

        $.getJSON(baseURL + 'api/Character/GetAll?campaignId=' + self.CampaignId, function (characterList) {
            characterList.forEach(function (character) {
                self.existingCharacterVM.CharacterList.push(new CharacterViewModel(character));
            });

            self.existingCharacterVM.SelectedCharacters.removeAll();

            self.CharacterList().forEach(function (characterInSession) {
                self.existingCharacterVM.SelectedCharacters.push(characterInSession.Id);
            });
        });
    }

    addExistingCharacters() {
        this.setupCharacterListForAssociation();
        this.existingCharacterModal(true);
    }

    //Associates characters from
    associateCharacters() {
        self = this;

        self.existingCharacterModal(false);

        let modelView = { sessionId: self.CurrentSession().Id, characterIds: self.existingCharacterVM.SelectedCharacters() };

        $.post(baseURL + 'api/SessionCharacter/PostAssociateCharactersWithSession', modelView);

        self.CharacterList.removeAll();

        self.existingCharacterVM.SelectedCharacters().forEach(function (characterId) {
            let index = arrayFirstIndexOf(self.existingCharacterVM.CharacterList(), function (character) {
                return character.Id === characterId;
            });

            if (index >= 0) {
                self.CharacterList.push(self.existingCharacterVM.CharacterList()[index]);
            }
        });
    }

    createCharacter() {
        this.showAddEditCharacterModal(true);
    }

    saveCharacter() {
        let self = this;
        self.showAddEditCharacterModal(false);

        self.addEditCharacterVM.CampaignId = self.CampaignId;

        // Save an edited character
        if (this.addEditCharacterVM.Id) {
            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (editedCharacter) {

                self.CharacterList.remove(function (character) { return character.Id === editedCharacter.Id; });

                self.CharacterList.push(new CharacterViewModel(editedCharacter));

            });
        } else {
            //Save a new character

            self.addEditCharacterVM.SessionId = self.CurrentSession().Id;

            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (returnedData) {
                //Get rid of as it is redundant?
                self.addEditCharacterVM.Id = returnedData.Id;

                self.CharacterList.push(new CharacterViewModel(returnedData));
            });
        }
    }

    showAddEditItemModal() {
        this.showItemModal(true);
    }

    saveItem() {
        let self = this;

        //Hide modal
        self.showItemModal(false);

        //Set the campaign id of the item to the current campaign
        self.addEditItemVM.CampaignId = self.CampaignId;

        if (self.addEditItemVM.Id) {
            //If Id is not null, then it is an edit
            $.post(baseURL + 'api/Item', self.addEditItemVM.toJson(), function (editedItem) {

                // Add sorting later via computed value
                self.ItemList.remove(function (item) { return item.Id === editedItem.Id; });

                self.ItemList.push(new ItemViewModel(editedItem));
            });
        } else {
            //If the campaign Id is null, then it is create
            self.addEditItemVM.SessionId = self.CurrentSession().Id;
            $.post(baseURL + 'api/Item', self.addEditItemVM.toJson(), function (addedItem) {
                self.ItemList.push(new ItemViewModel(addedItem));
            });
        }
    }

    // Sets up a list of all items in the campaign, and selects
    // Which items are already in this session then displays the modal
    // For users to change what items are in the session
    addExistingItems() {
        this.setupItemListForAssociation();
        this.existingItemsModal(true);
    }

    //Helper method for setting up UI for item list
    setupItemListForAssociation() {
        self = this;

        self.existingItemsVM.CompleteItemList.removeAll();

        $.getJSON(baseURL + 'api/Item/GetAll?campaignId=' + self.CampaignId, function (itemList) {
            itemList.forEach(function (item) {
                self.existingItemsVM.CompleteItemList.push(new ItemViewModel(item));
            });

            self.existingItemsVM.SelectedItems.removeAll();

            self.ItemList().forEach(function (itemInSession) {
                self.existingItemsVM.SelectedItems.push(itemInSession.Id);
            });
        });
    }

    //Associates selected items with the current session
    associateItems() {
        let self = this;

        self.existingItemsModal(false);

        let existingItemList = { SessionId: self.CurrentSession().Id, itemIds: self.existingItemsVM.SelectedItems() };

        $.post(baseURL + "api/SessionItem/", existingItemList);

        self.ItemList.removeAll();

        self.existingItemsVM.SelectedItems().forEach(function (itemId) {
            let index = arrayFirstIndexOf(self.existingItemsVM.CompleteItemList(), function (item) {
                return item.Id === itemId;
            });

            if (index >= 0) {
                self.ItemList.push(self.existingItemsVM.CompleteItemList()[index]);
            }
        });
    }
}

// The hubs view model to be bound to knockout
let hubViewModel = new HubViewModel(campaignId);

$.getJSON(baseURL + 'api/Session?id=' + campaignId, function (data) {
    // Fill sessions list
    data.forEach(function (sessionData) {
        hubViewModel.SessionList.push(new SessionViewModel(sessionData));
    });

    // If there are sessions, set the first one in the list to the
    // current session
    if (hubViewModel.SessionList() && hubViewModel.SessionList().length > 0) {
        hubViewModel.CurrentSession(hubViewModel.SessionList()[0]);
    }

    ko.applyBindings(hubViewModel);
});
