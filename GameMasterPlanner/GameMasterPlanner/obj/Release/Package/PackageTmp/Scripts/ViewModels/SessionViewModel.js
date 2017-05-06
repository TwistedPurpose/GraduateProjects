class SessionViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.CampaignId = data.CampaignId;
            this.SessionNumber = ko.observable(data.SessionNumber);
            this.Notes = ko.observable(data.Notes);
            this.Title = ko.observable(data.Title);
        } else {
            this.Id;
            this.CampaignId;
            this.SessionNumber = ko.observable();
            this.Notes = ko.observable();
            this.Title = ko.observable();
        }
    }

    toJson() {
        let self = this;

        let session = {
            Id: self.Id,
            CampaignId: self.CampaignId,
            SessionNumber: self.SessionNumber(),
            Notes: self.Notes(),
            Title: self.Title()
        };

        return session;
    }
}