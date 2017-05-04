class ItemViewModel {
    constructor(item) {
        if (item) {
            this.Id = item.Id;
            this.CampaignId = item.CampaignId;
            this.SessionId = item.SessionId;
            this.Name = ko.observable(item.Name);
            this.ItemDescription = ko.observable(item.ItemDescription);
            this.Abilities = ko.observable(item.Abilities);
        } else {
            this.Id;
            this.CampaignId;
            this.SessionId;
            this.Name = ko.observable();
            this.ItemDescription = ko.observable();
            this.Abilities = ko.observable();
        }
    }

    setupToEdit(item) {
        this.Id = item.Id;
        this.CampaignId = item.CampaignId;
        this.SessionId = item.SessionId;
        this.Name(item.Name());
        this.ItemDescription(item.ItemDescription());
        this.Abilities(item.Abilities);
    }

    toJson() {
        let self = this;

        let item = {
            Id: self.Id,
            CampaignId: self.CampaignId,
            SessionId: self.SessionId,
            Name: self.Name(),
            ItemDescription: self.ItemDescription(),
            Abilities: self.Abilities()
        };

        return item;
    }

    clear() {
        this.Id = 0;
        this.CampaignId = 0;
        this.SessionId = 0;
        this.Name(null);
        this.ItemDescription(null);
        this.Abilities(null);
    }
}