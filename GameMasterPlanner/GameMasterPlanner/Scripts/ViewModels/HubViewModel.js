class HubViewModel {

    constructor(campaignId) {
        let self = this;

        this.CampaignId = campaignId
        this.SessionList = ko.observableArray([]);

        //Check here if there is no sessions
        this.CurrentSession = ko.observable();

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
        this.showAddExistingCharacterModal = ko.observable(false);

        this.addEditCharacterVM = new CharacterViewModel(null);
        this.addExistingCharacterVM = new CharacterListViewModel(null);

        this.editCharacter = (character) => {
            this.addEditCharacterVM.setupToEdit(character);
            this.showAddEditCharacterModal(true);
        }

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

    createCharacter() {
        this.showAddEditCharacterModal(true);
    }

    saveCharacter() {
        let self = this;
        self.showAddEditCharacterModal(false);
        //self.CharacterList.removeAll();

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
    data.forEach(function (sessionData) {
        hubViewModel.SessionList.push(new SessionViewModel(sessionData));
    });

    if (hubViewModel.SessionList() && hubViewModel.SessionList().length > 0) {
        hubViewModel.CurrentSession(hubViewModel.SessionList()[0]);
    }

    ko.applyBindings(hubViewModel);
});
