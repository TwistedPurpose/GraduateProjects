class Session {
    constructor(data) {
        this.Id = data.Id;
        this.Notes = data.Notes;
        this.Title = data.Title;
    }


}

class SessionList {
    constructor(data) {
        let list = [];
        data.forEach(function (session) {
            list.push(new Session());
        });

        this.sessionList = ko.observableArray(list);
    }
}