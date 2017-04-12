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

        this.showAddCharacterModal = ko.observable(false);

        this.addCharacterVM = new CharacterViewModel(null);

        this.editCharacter = (character) => {
            this.addCharacterVM.setupToEdit(character);
            this.showAddCharacterModal(true);
        }

        $('#addCharacterModal').on('hidden.bs.modal', function () {
            self.addCharacterVM.clear();

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

    createCharacter() {
        this.showAddCharacterModal(true);
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

    saveCharacter() {
        let self = this;
        self.showAddCharacterModal(false);
        this.CharacterList.removeAll();

        if (this.addCharacterVM.Id) {
            $.post(baseURL + 'api/Character', self.addCharacterVM.toJson(), function (returnedData) {
                $.getJSON(baseURL + 'api/Character/GetSessionCharacters?sessionId=' + self.CurrentSession().Id, function (data) {
                    data.forEach(function (characterData) {
                        self.CharacterList.push(new CharacterViewModel(characterData));
                    });
                });
            });
        } else {
            $.post(baseURL + 'api/Character', self.addCharacterVM.toJson(), function (returnedData) {
                self.addCharacterVM.Id = returnedData.Id;

                if (self.CurrentSession().Id) {
                    $.post(baseURL + 'api/Character/PostAssociateToSession?characterId=' + self.addCharacterVM.Id +
                        '&sessionId=' + self.CurrentSession().Id,
                        self.addCharacterVM.toJson(), function (returnedData) {

                            $.getJSON(baseURL + 'api/Character/GetSessionCharacters?sessionId=' + self.CurrentSession().Id, function (data) {
                                data.forEach(function (characterData) {
                                    self.CharacterList.push(new CharacterViewModel(characterData));
                                });
                            });
                        });
                }
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


    $.getJSON(baseURL + 'api/Character/GetSessionCharacters?sessionId=' + hubViewModel.CurrentSession().Id, function (data) {
        data.forEach(function (characterData) {
            hubViewModel.CharacterList.push(new CharacterViewModel(characterData));
        });
    });

    ko.applyBindings(hubViewModel);
});
