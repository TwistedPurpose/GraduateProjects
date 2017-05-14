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

        let self = this;

        this.ImageType = ko.computed(function(){
            if(self.ImageBLOB()){
                return self.ImageBLOB().split(',')[0].split(';')[0].split(':')[1];
            }
            return null;
        });

        this.ImageData = ko.computed(function(){
            if(self.ImageBLOB()){
                return self.ImageBLOB().split(',')[1];
            } else {
                return null;
            }
            
        });

        this.uploader = new FileUploader();
    }

    // Takes an uploaded map from the file uploader
    // And sets it to the Map VM's image
    setUploadedMap(){
        this.Name(this.uploader.fileName());
        this.ImageBLOB(this.uploader.fileInput());
    }

    toJson(){

    }

} 