class AddCharacterViewModel {
    constructor() {
        this.Name = ko.observable();
        this.History = ko.observable();
        this.Description = ko.observable();
    }

    save() {
        let self = this;

        let newCharacter = new Character({
            Name: self.Name(),
            History: self.History(),
            Descrption: self.Description()
        });

        $.post(baseURL + 'api/Character', data, function (returnedData) {
            returnedData.forEach(function (data) {
                list.push(new Character(data));
            });
        });
    }
}

