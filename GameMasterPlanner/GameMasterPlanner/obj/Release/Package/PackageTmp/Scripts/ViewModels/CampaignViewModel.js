class Campaign {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.Name = data.Name;
            this.History = data.History;

        } else {
            this.Id = null;
            this.Name = null;
            this.History = null;
        }

        this.HubURL = ko.computed(function () {
            return baseURL + 'Home/Hub?campaignId=' + this.Id;
        }, this);
    }

    toJson() {
        let self = this;

        let returnJson = {
            Id: self.Id,
            Name: self.Name,
            History: self.History
        };

        return returnJson;
    }
}