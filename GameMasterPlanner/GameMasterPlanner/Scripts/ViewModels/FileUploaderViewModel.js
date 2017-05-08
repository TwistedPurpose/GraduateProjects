class FileUploaderViewModel {
    constructor() {
        this.filePath = ko.observable();
    }

    fileUpload(data, e) {
        let file = e.target.files[0];
        let reader = new FileReader();
    }
}