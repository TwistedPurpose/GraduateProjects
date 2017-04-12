class CharacterViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.HistoryId = data.HistoryId;
            this.Name = ko.observable(data.Name);
            this.CharDescription = ko.observable(data.Description);
            this.Notes = ko.observable(data.Notes);
        } else {
            this.Id;
            this.HistoryId;
            this.Name = ko.observable();
            this.CharDescription = ko.observable();
            this.Notes = ko.observable();
        }

    }

    toJson() {
        let self = this;

        let character = {
            Id: self.Id,
            HistoryId: self.HistoryId,
            Name: self.Name(),
            CharDescription: self.CharDescription(),
            Notes: self.Notes()
        }

        return character;
    }

    loadCharacter(characterId) {
        $.getJSON(baseUrl + "api/Character?characterId=" + characterId, function (data) { });

    }

    associateToSession(SessionId) {
        $.post(baseURL + 'api/Character/PostAssociateToSession?characterId=' + this.Id + '&sessionId=' + SessionId,
            this.toJson(), function (returnedData) { });
    }
}

