class initMap {
    constructor(){
        this.map = new google.maps.Map($('#map'), {
            center: { lat: -34.397, lng: 150.644 },
            zoom: 8
        });
    }

} 