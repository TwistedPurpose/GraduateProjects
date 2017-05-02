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

        };

        //Implement me!
    }

    clear() {
        //Implement me!
    }
}