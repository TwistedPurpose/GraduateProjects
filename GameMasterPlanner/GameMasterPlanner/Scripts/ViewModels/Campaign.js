class Campaign {
    constructor(id, name, history) {
        this.name = name;
        this.history = history;
        this.id = id;
        this.hubURL = ko.computed(function () {
            return baseURL + 'Hub/Index/' + id;
        }, this);
            
    }
}

class CampaignList {
    constructor(data) {
        let list = [];
        data.forEach(function (campaign) {
            list.push(new Campaign(campaign.Id, campaign.Name, campaign.History));
        });
        this.campaignList = ko.observableArray(list);

        this.newCampaginName = ko.observable();
        this.newCampaginHistory = ko.observable();
    }

    addCampagin() {
        let data = new Campaign(-1, this.newCampaginName(), this.newCampaginHistory());
        
        this.campaignList.removeAll();
        var list = this.campaignList;
        
        $.post(baseURL + 'api/Campaign', data, function (returnedData) {
            returnedData.forEach(function (campaign) {
                list.push(new Campaign(campaign.Id, campaign.Name, campaign.History));
            });
        });

        this.campaignList = list;
    }
}

$.getJSON(baseURL + 'api/Campaign', function (data) {
    var campaginList = new CampaignList(data);
    ko.applyBindings(campaginList);
});