class ItemViewModel {
    constructor(item) {
        if (item) {
            this.Id = item.Id;
            this.CampaignId = item.CampaignId;
            this.SessionId = item.SessionId;
            this.Name = ko.observable(item.Name);
            this.ItemDescription = ko.observable(item.ItemDescription);
            this.Abilities = ko.observable(item.Abilities);
            this.SessionList = ko.observableArray(item.SessionList);
        } else {
            this.Id;
            this.CampaignId;
            this.SessionId;
            this.Name = ko.observable();
            this.ItemDescription = ko.observable();
            this.Abilities = ko.observable();
            this.SessionList = ko.observableArray([]);
        }
    }

    setupToEdit(item) {
        this.Id = item.Id;
        this.CampaignId = item.CampaignId;
        this.SessionId = item.SessionId;
        this.Name(item.Name());
        this.ItemDescription(item.ItemDescription());
        this.Abilities(item.Abilities());
        this.SessionList(item.SessionList());
    }

    toJson() {
        let self = this;

        let item = {
            Id: self.Id,
            CampaignId: self.CampaignId,
            SessionId: self.SessionId,
            Name: self.Name(),
            ItemDescription: self.ItemDescription(),
            Abilities: self.Abilities(),
            SessionList: self.SessionList()
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
        //Add SessionList?
    }
}

class ItemListViewModel {
    constructor(data) {
        if (data) {
            this.CompleteItemList = ko.observableArray(data.ItemList);
            this.SelectedItems = ko.observableArray(data.SelectedItems);
        } else {
            this.CompleteItemList = ko.observableArray([]);
            this.SelectedItems = ko.observableArray([]);
        }
    }
}