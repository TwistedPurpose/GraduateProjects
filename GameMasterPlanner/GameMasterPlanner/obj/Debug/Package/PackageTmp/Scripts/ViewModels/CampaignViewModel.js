class Campaign {
    constructor(data) {
        this.Name = data.Name;
        this.History = data.History;
        this.Id = data.Id;
        this.HubURL = ko.computed(function () {
            return baseURL + 'Home/Hub?campaignId=' + this.Id;
        }, this);
    }
}