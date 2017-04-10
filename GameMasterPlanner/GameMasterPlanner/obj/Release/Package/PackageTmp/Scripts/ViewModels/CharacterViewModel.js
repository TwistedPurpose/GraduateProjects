class CharacterViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.HistoryId = data.HistoryId;
            this.Name = ko.observable(data.Name);
            this.CharDescription = ko.observable(data.Description);
            this.Notes = ko.observable(data.Notes);
        } else {
            this.Id = -1;
            this.HistoryId = -1;
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

    save() {
        let self = this;

        $.post(baseURL + 'api/Character', self.toJson(), function (returnedData) {
            self.Id = returnedData.Id;
            self.HistoryId = returnedData.HistoryId;
          
            if (self.SessionId) {
                self.associateToSesssion();
            }
            self.clear();
        });
    }

    loadCharacter(characterId) {
        $.getJSON(baseUrl + "api/Character?characterId=" + characterId, function (data) { });

    }

    associateToSesssion() {
        $.post(baseURL + 'api/Character/PostAssociateToSession?characterId=' + this.Id + '&sessionId=' + this.SessionId,
            this.toJson(), function (returnedData) { });
    }

    setSessionId(sessionId) {
        this.SessionId = sessionId;
    }

    clear() {
        this.Id = -1;
        this.HistoryId = -1;
        this.SessionId = null;
        this.Name(null);
        this.CharDescription(null);
        this.Notes(null);
    }
}

