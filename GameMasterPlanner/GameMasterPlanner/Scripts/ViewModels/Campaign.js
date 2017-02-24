class Campaign {
    constructor(id, name, history) {
        this.name = name;
        this.history = history;
        this.id = id;
    }
}

class CampaignList {
    constructor(data) {
        let list = [];
        data.forEach(function (campaign) {
            list.push(new Campaign(campaign.id, campaign.Name, campaign.History));
        });
        this.campaignList = ko.observableArray(list);

        this.newCampaginName = ko.observable();
        this.newCampaginHistory = ko.observable();
    }

    addCampagin() {
        let data = new Campaign(this.newCampaginName(), this.newCampaginHistory());
        
        let list = [];
        $.post(window.location.href + 'api/Campaign', data, function (returnedData) {
            returnedData.forEach(function (campaign) {
                list.push(new Campaign(campaign.Name, campaign.History));
            });

        });
        this.campaignList(list);
    }
}

$.getJSON(window.location.href + 'api/Campaign', function (data) {
    var campaginList = new CampaignList(data);
    ko.applyBindings(campaginList);
});