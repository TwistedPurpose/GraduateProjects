class CampaignList {
    constructor(data) {
        let self = this;
        let list = [];
        data.forEach(function (campaign) {
            list.push(new Campaign(campaign));
        });

        this.showAddEditCampaignModal = ko.observable(false);
        this.addEditCampaignVM = ko.observable(new Campaign(null));

        this.campaignList = ko.observableArray(list);

        this.editCampaign = (campaign) => {
            this.addEditCampaignVM(campaign);
            this.showAddEditCampaignModal(true);
        }

        $('#addEditCampaignModal').on('hidden.bs.modal', function () {
            self.addEditCampaignVM(new Campaign(null));
        });
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

        self.addEditCampaignVM(new Campaign(null));
    }
}

$.getJSON(baseURL + 'api/Campaign', function (data) {
    var campaginList = new CampaignList(data);
    ko.applyBindings(campaginList);
});