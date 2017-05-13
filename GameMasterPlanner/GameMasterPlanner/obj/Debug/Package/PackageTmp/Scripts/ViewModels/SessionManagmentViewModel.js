class SessionManagementViewModel {
    constructor() {
        let self = this;

        //List of campaigns to select and the sessions in it
        this.CampaignList = ko.observableArray([]);
        this.SessionList = ko.observableArray([]);

        this.SortedSessionList = ko.computed(function () {
            self.SessionList.sort(function (l, r) { return l.SessionNumber() > r.SessionNumber() ? 1 : -1; });

            return self.SessionList();
        });

        //Current selected campaign in drop down
        this.CurrentCampaign = ko.observable();

        //When current campaign is changed, empty out session list and get all sessions for that campaign
        this.CurrentCampaign.subscribe(function (newValue) {
            $.getJSON(baseURL + 'api/Session/Get?id=' + self.CurrentCampaign().Id, function (sessionList) {
                self.SessionList.removeAll();

                sessionList.forEach(function (session) {
                    self.SessionList.push(new SessionViewModel(session));
                });
            });
        });
    }

    save() {
        let self = this;

        let sessionVMList = [];

        self.SessionList().forEach(function (session) {
            sessionVMList.push(session.toJson());
        });

        $.ajax({
            url: baseURL + 'api/Session',
            type: 'POST',
            data: JSON.stringify(sessionVMList),
            dataType: 'json',
            contentType: 'application/json'
        });
    }
}

let vm = new SessionManagementViewModel();

$.getJSON(baseURL + 'api/Campaign', function (campaigns) {
    campaigns.forEach(function (campaign) {
        vm.CampaignList.push(new Campaign(campaign));
    });

    if (vm.CampaignList && vm.CampaignList().length > 0) {
        vm.CurrentCampaign(vm.CampaignList()[0]);
    }
});

ko.applyBindings(vm);