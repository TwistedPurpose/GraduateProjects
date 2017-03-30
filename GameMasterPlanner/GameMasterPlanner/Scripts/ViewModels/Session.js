class Session {
    constructor() {

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