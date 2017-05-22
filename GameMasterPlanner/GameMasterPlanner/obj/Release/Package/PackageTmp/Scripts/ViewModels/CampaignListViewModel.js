class CampaignList {
    constructor(data) {
        let list = [];
        data.forEach(function (campaign) {
            list.push(new Campaign(campaign));
        });

        this.showAddEditCampaignModal = ko.observable(false);
        this.addEditCampaignVM = ko.observable(new Campaign(null));

        this.campaignList = ko.observableArray(list);
    }

    createCampaign() {
        this.showAddEditCampaignModal(true);
    }

    saveCampaign() {
        let self = this;

        self.showAddEditCampaignModal(false);

        $.post(baseURL + 'api/Campaign', self.addEditCampaignVM().toJson(), function (returnedData) {
            self.campaignList.removeAll();
            returnedData.forEach(function (campaign) {
                self.campaignList.push(new Campaign(campaign));
            });
        });
    }
}

$.getJSON(baseURL + 'api/Campaign', function (data) {
    var campaginList = new CampaignList(data);
    ko.applyBindings(campaginList);
});