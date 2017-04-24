class CampaignList {
    constructor(data) {
        let list = [];
        data.forEach(function (campaign) {
            list.push(new Campaign(campaign));
        });
        this.campaignList = ko.observableArray(list);

        this.newCampaginName = ko.observable();
        this.newCampaginHistory = ko.observable();
    }

    addCampagin() {
        let self = this;

        let data = { Name: self.newCampaginName(), History: self.newCampaginHistory()};
        
        this.campaignList.removeAll();
        var list = this.campaignList;
        
        $.post(baseURL + 'api/Campaign', data, function (returnedData) {
            returnedData.forEach(function (campaign) {
                list.push(new Campaign(campaign));
            });
        });

        this.campaignList = list;
    }
}

$.getJSON(baseURL + 'api/Campaign', function (data) {
    var campaginList = new CampaignList(data);
    ko.applyBindings(campaginList);
});