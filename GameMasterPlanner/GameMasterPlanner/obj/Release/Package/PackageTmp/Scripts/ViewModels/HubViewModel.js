class HubViewModel {

    constructor(campaignId) {
        let self = this;

        this.CampaignId = campaignId;
        this.SessionList = ko.observableArray([]);

        this.CurrentSession = ko.observable();

        this.CharacterList = ko.observableArray([]);
        this.CompleteCharacterList = ko.observableArray([]); 
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
        this.showAddExistingCharacterModal = ko.observable(false);

        this.addEditCharacterVM = new CharacterViewModel(null);
        this.addExistingCharacterVM = new CharacterListViewModel(null);

        this.editCharacter = (character) => {
            this.addEditCharacterVM.setupToEdit(character);
            this.showAddEditCharacterModal(true);
        };

        $('#addCharacterModal').on('hidden.bs.modal', function () {
            self.addEditCharacterVM.clear();
        });

        self.CurrentSession.subscribe(function (newValue) {
            self.CharacterList.removeAll();

            if (self.CurrentSession().Id) {
                $.getJSON(baseURL + 'api/Character/GetSessionCharacters?sessionId=' + self.CurrentSession().Id, function (data) {
                    data.forEach(function (characterData) {
                        self.CharacterList.push(new CharacterViewModel(characterData));
                    });
                });
            }

        });

        $.getJSON(baseURL + 'api/Character/GetAll?campaignId=' + self.CampaignId, function (characterList) {
            characterList.forEach(function (character) {
                self.CompleteCharacterList.push(new CharacterViewModel(character));
            });

            self.addExistingCharacterVM.CharacterList(self.CompleteCharacterList());
        });
    }

    saveSession() {
        $.post(baseURL + 'api/Session', this.CurrentSession().toJson());
    }

    newSession() {
        let self = this;

        let newSession = new SessionViewModel();
        newSession.CampaignId = self.CampaignId;

        let json = newSession.toJson();

        $.post(baseURL + 'api/Session', json, function (returnedData) {
            newSession = new SessionViewModel(returnedData);
            self.SessionList.push(newSession);
            self.CurrentSession(newSession);
        });
    }

    setupCharacterListForAssociation() {
        self = this;

        self.addExistingCharacterVM.CharacterList(self.CompleteCharacterList());

        self.addExistingCharacterVM.SelectedCharacters.removeAll();

        self.CharacterList().forEach(function (characterInSession) {
            self.addExistingCharacterVM.SelectedCharacters.push(characterInSession.Id);
        });
    }

    addExistingCharacters() {
        this.setupCharacterListForAssociation();
        this.showAddExistingCharacterModal(true);
    }

    //
    associateCharacters() {
        self = this;

        self.showAddExistingCharacterModal(false);

        let modelView = { sessionId: self.CurrentSession().Id, characterIds: self.addExistingCharacterVM.SelectedCharacters() };

        $.post(baseURL + 'api/SessionCharacter/PostAssociateCharactersWithSession', modelView);

        self.CharacterList.removeAll();

        self.addExistingCharacterVM.SelectedCharacters().forEach(function (characterId) {
            let index = arrayFirstIndexOf(self.CompleteCharacterList(), function (character) {
                return character.Id === characterId;
            });

            if (index >= 0) {
                self.CharacterList.push(self.CompleteCharacterList()[index]);
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
            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (returnedData) {

                let indexOfCharacter = arrayFirstIndexOf(self.CharacterList(), function (character) {
                    return character.Id === returnedData.Id;
                });

                self.CharacterList()[indexOfCharacter] = new CharacterViewModel(returnedData);
                self.CharacterList.valueHasMutated();
            });
        } else {
            //Save a new character

            self.addEditCharacterVM.SessionId = self.CurrentSession().Id;

            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (returnedData) {
                self.addEditCharacterVM.Id = returnedData.Id;

                self.CharacterList.push(new CharacterViewModel(returnedData));
            });
        }
    }
}

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
