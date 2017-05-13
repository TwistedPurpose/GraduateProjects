class CharacterLibraryViewModel {
    constructor() {
        let self = this;

        //List of campaigns to select and the characters in it
        this.CampaignList = ko.observableArray([]);
        this.CharacterList = ko.observableArray([]);

        //Current selected campaign in drop down
        this.CurrentCampaign = ko.observable();

        //When current campaign is changed, empty out character list and get all characters for that campaign
        this.CurrentCampaign.subscribe(function (newValue) {
            $.getJSON(baseURL + 'api/Character/GetAll?campaignId=' + self.CurrentCampaign().Id, function (characterList) {
                self.CharacterList.removeAll();

                characterList.forEach(function (character) {
                    self.CharacterList.push(new CharacterViewModel(character));
                });
            });
        });

        //Binary observable for showing and hiding character modal
        //If true, show modal, else hide.
        this.showAddEditCharacterModal = ko.observable(false);

        //View model for add/edit character modal
        this.addEditCharacterVM = new CharacterViewModel(null);

        //Fills VM of the modal, probably a better way to do this
        //Shows modal
        this.editCharacter = (character) => {
            this.addEditCharacterVM.setupToEdit(character);
            this.showAddEditCharacterModal(true);
        }

        //When modal is closed for whatever reason, clear the VM
        //To ensure clean use on next opening
        //There is probably a better way to do this, but IDK
        $('#addCharacterModal').on('hidden.bs.modal', function () {
            self.addEditCharacterVM.clear();
        });
    }

    createCharacter() {
        this.showAddEditCharacterModal(true);
    }

    saveCharacter() {
        let self = this;
        
        if (this.addEditCharacterVM.Id) {
            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (returnedData) {
                let indexOfCharacter = arrayFirstIndexOf(self.CharacterList(), function (character) {
                    return character.Id === returnedData.Id;
                });

                self.CharacterList()[indexOfCharacter] = new CharacterViewModel(returnedData);
                self.CharacterList.valueHasMutated();
            });
        } else {
            self.addEditCharacterVM.CampaignId = self.CurrentCampaign().Id;

            $.post(baseURL + 'api/Character', self.addEditCharacterVM.toJson(), function (returnedData) {
                self.CharacterList.push(new CharacterViewModel(returnedData));
            });
        }

        self.showAddEditCharacterModal(false);
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