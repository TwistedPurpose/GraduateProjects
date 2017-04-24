class CharacterViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.CampaignId = data.CampaignId;
            this.SessionId = data.SessionId;
            this.Name = ko.observable(data.Name);
            this.CharDescription = ko.observable(data.CharDescription);
            this.Notes = ko.observable(data.Notes);
            this.SessionList = ko.observableArray(data.SessionList);
        } else {
            this.Id;
            this.CampaignId;
            this.SessionId;
            this.Name = ko.observable();
            this.CharDescription = ko.observable();
            this.Notes = ko.observable();
            this.SessionList = ko.observableArray([]);
        }

    }

    setupToEdit(character) {
        this.Id = character.Id;
        this.CampaignId = character.CampaignId;
        this.SessionId = character.SessionId;
        this.HistoryId = character.HistoryId;
        this.Name(character.Name());
        this.CharDescription(character.CharDescription());
        this.Notes(character.Notes());
    }

    toJson() {
        let self = this;

        let character = {
            Id: self.Id,
            CampaignId: self.CampaignId,
            HistoryId: self.HistoryId,
            Name: self.Name(),
            CharDescription: self.CharDescription(),
            Notes: self.Notes()
        }

        return character;
    }

    clear() {
        this.Id = null;
        this.HistoryId = null;
        this.Name(null);
        this.CharDescription(null);
        this.Notes(null);
    }
}

class CharacterListViewModel {
    constructor(data) {
        if (data) {
            this.CharacterList = ko.observableArray(data.CharacterList);
            this.SelectedCharacters = ko.observableArray(data.SelectedCharacters);
        } else {
            this.CharacterList = ko.observableArray([]);
            this.SelectedCharacters = ko.observableArray([]);
        }

    }
}