class MapViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.ParentMapId = data.ParentMapId;
            this.SessionId = data.SessionId;
            this.Name = ko.observable(data.Name);
            this.Image = ko.observable(data.Image);
            this.ImageType = ko.observable(data.ImageType);
        } else {
            this.Id;
            this.ParentMapId;
            this.SessionId;
            this.Name = ko.observable();
            this.Image = ko.observable();
            this.ImageType = ko.observable();
        }

        let self = this;

        this.ImageBlob = ko.computed(function () {
            if (self.Image() && self.ImageType()) {

                var binary = atob(self.Image());

                var array = [];
                for (var i = 0; i < binary.length; i++) {
                    array.push(binary.charCodeAt(i));
                }

                return new Blob([new Uint8Array(array)], { type: self.ImageType() });
            } else {
                return null;
            }

        });

        this.uploader = new FileUploader();
    }

    // Takes an uploaded map from the file uploader
    // And sets it to the Map VM's image
    setUploadedMap() {
        if (!this.Name()) {
            this.Name(this.uploader.fileName());
        }

        let fileDump = this.uploader.fileInput();

        // Do null checks
        let imageData = fileDump.split(',')[1];
        this.ImageType(fileDump.split(',')[0].split(';')[0].split(':')[1]);
        this.Image(imageData);
    }

    toJson() {
        let self = this;

        let json = {
            Id: self.Id,
            ParentMapId: self.ParentMapId,
            SessionId: self.SessionId,
            Name: self.Name(), //knockout elements
            Image: self.Image(),
            ImageType: self.ImageType()
        };

        return json;
    }

    clear() {
        this.Id = null;
        this.ParentMapId = null;
        this.SessionId = null;
        this.Name(null);
        this.Image(null);
        this.ImageType(null);
        this.ImageString = null;
    }

} 