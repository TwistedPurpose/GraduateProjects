class CharacterLibraryViewModel {
    constructor() {
        let self = this;

        this.CampaignList = ko.observableArray([]);
        this.CharacterList = ko.observableArray([]);

        this.CurrentCampaign = ko.observable();

        this.CurrentCampaign.subscribe(function (newValue) {
            $.getJSON(baseURL + 'api/Character/GetAll?campaignId=' + self.CurrentCampaign().Id, function (characterList) {
                self.CharacterList.removeAll();
                characterList.forEach(function (character) {
                    self.CharacterList.push(new CharacterViewModel(character));
                });
            });
        });

        this.showAddEditCharacterModal = ko.observable(false);

        this.addEditCharacterVM = new CharacterViewModel(null);

        this.editCharacter = (character) => {
            this.addEditCharacterVM.setupToEdit(character);
            this.showAddEditCharacterModal(true);
        }
    }

    createCharacter() {
        this.showAddEditCharacterModal(true);
    }
}

var vm = new CharacterLibraryViewModel();

$.getJSON(baseURL + 'api/Campaign', function (campaigns) {
    campaigns.forEach(function (campaign) {
        vm.CampaignList.push(new Campaign(campaign));
    });

    if (vm.CampaignList && vm.CampaignList().length > 0) {
        vm.CurrentCampaign(vm.CampaignList()[0]);
    }
});

ko.applyBindings(vm);