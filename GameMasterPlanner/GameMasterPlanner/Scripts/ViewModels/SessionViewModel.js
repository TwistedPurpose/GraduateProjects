class SessionViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.Notes = ko.observable(data.Notes);
            this.Title = ko.observable(data.Title);
        } else {
            this.Id;
            this.Notes = ko.observable();
            this.Title = ko.observable();
        }
    }

    toJson() {
        let self = this;

        let session = {
            Id: self.Id,
            Notes: self.Notes(),
            Title: self.Title()
        }

        return session;
    }
}