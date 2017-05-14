class FileUploader {
    constructor() {
        this.fileInput = ko.observable();
        this.fileName = ko.observable();
        this.reader = new FileReader();
    }
}