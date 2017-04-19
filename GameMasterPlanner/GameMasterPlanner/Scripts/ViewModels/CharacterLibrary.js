class CharacterLibraryViewModel {
    constructor() {
        let self = this;

        this.CampaignList = ko.observableArray([]);
        this.CharacterList = ko.observableArray([]);

        this.CurrentCampaign = ko.observable();

        this.CurrentCampaign.subscribe(function (newValue) {
            $.getJSON(baseURL + 'api/Character/GetAll?=' + self.CurrentCampaign().Id, function (characterList) {
                self.CharacterList.removeAll();

                characterList.forEach(function (character) {
                    self.CharacterList.push(new CharacterViewModel(character));
                });
            });
        });

        $.getJSON(baseURL + 'api/Campaign', function (campaigns) {
            campaigns.forEach(function (campaign) {
                self.CampaignList().push(new Campaign(campaign));
            });
        });
    }
}



var vm = new CharacterLibraryViewModel();
