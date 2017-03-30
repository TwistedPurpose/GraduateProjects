class Hub {
    constructor(campaignId) {
        let sessionList = [];
        $.getJSON(baseURL + 'api/Session?id=' + campaignId, function (data) {
            sessionList.push(new Session(data));
        });

        this.SessionList = ko.observableArray(sessionList);

        //Check here if there is no sessions
        this.CurrentSession = new ko.observable(sessionList[0]);

        this.CharacterList;
        this.LocationList;
        this.OrganizationList;

        let map = new GMaps({
            div: '#map',
            lat: 52.4801,
            lng: -1.8835,
            width: '100%',
            height: '500px',
            zoom: 7
        });

        map.setContextMenu({
            control: 'map',
            options: [{
                title: 'Add marker',
                name: 'add_marker',
                action: function (e) {
                    this.addMarker({
                        lat: e.latLng.lat(),
                        lng: e.latLng.lng(),
                        title: 'New marker'
                    });
                }
            }, {
                title: 'Center here',
                name: 'center_here',
                action: function (e) {
                    this.setCenter(e.latLng.lat(), e.latLng.lng());
                }
            }]
        });

        this.Map = map;
    }
}

let hubViewModel = new Hub(campaignId);
ko.applyBindings(hubViewModel);