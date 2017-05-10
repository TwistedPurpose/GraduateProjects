class MapViewModel {
    constructor(data) {
        if (data) {
            this.Id = data.Id;
            this.ParentMapId = data.ParentMapId;
            this.Name = ko.observable(data.Name);
            this.ImageBLOB = ko.observable(data.ImageBLOB);
        } else {
            this.Id;
            this.ParentMapId;
            this.Name = ko.observable();
            this.ImageBLOB = ko.observable();
        }

        this.uploader = new FileUploader();
    }

    // Takes an uploaded map from the file uploader
    // And sets it to the Map VM's image
    setUploadedMap(){
        this.Name(this.uploader.fileName());
        this.ImageBLOB(this.uploader.fileInput());

    }

} 