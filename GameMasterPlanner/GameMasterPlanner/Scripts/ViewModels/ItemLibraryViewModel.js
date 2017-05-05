class ItemLibraryViewModel {
    constructor() {
        let self = this;

        //List of campaigns to select and the Items in it
        this.CampaignList = ko.observableArray([]);
        this.ItemList = ko.observableArray([]);

        //Current selected campaign in drop down
        this.CurrentCampaign = ko.observable();

        //When current campaign is changed, empty out Item list and get all Items for that campaign
        this.CurrentCampaign.subscribe(function (newValue) {
            $.getJSON(baseURL + 'api/Item/GetAll?campaignId=' + self.CurrentCampaign().Id, function (ItemList) {
                self.ItemList.removeAll();

                ItemList.forEach(function (Item) {
                    self.ItemList.push(new ItemViewModel(Item));
                });
            });
        });

        //Binary observable for showing and hiding Item modal
        //If true, show modal, else hide.
        this.showItemModal = ko.observable(false);

        //View model for add/edit Item modal
        this.addEditItemVM = new ItemViewModel(null);

        //Fills VM of the modal, probably a better way to do this
        //Shows modal
        this.editItem = (Item) => {
            this.addEditItemVM.setupToEdit(Item);
            this.showItemModal(true);
        }

        //When modal is closed for whatever reason, clear the VM
        //To ensure clean use on next opening
        //There is probably a better way to do this, but IDK
        $('#addItemModal').on('hidden.bs.modal', function () {
            self.addEditItemVM.clear();
        });
    }

    createItem() {
        this.showItemModal(true);
    }

    saveItem() {
        let self = this;

        if (self.addEditItemVM.Id) {
            $.post(baseURL + 'api/Item', self.addEditItemVM.toJson(), function (returnedData) {
                let indexOfItem = arrayFirstIndexOf(self.ItemList(), function (Item) {
                    return Item.Id === returnedData.Id;
                });

                self.ItemList()[indexOfItem] = new ItemViewModel(returnedData);
                self.ItemList.valueHasMutated();
            });
        } else {
            self.addEditItemVM.CampaignId = self.CurrentCampaign().Id;

            $.post(baseURL + 'api/Item', self.addEditItemVM.toJson(), function (returnedData) {
                self.ItemList.push(new ItemViewModel(returnedData));
            });
        }

        self.showItemModal(false);
    }
}

var vm = new ItemLibraryViewModel();

$.getJSON(baseURL + 'api/Campaign', function (campaigns) {
    campaigns.forEach(function (campaign) {
        vm.CampaignList.push(new Campaign(campaign));
    });

    if (vm.CampaignList && vm.CampaignList().length > 0) {
        vm.CurrentCampaign(vm.CampaignList()[0]);
    }
});

ko.applyBindings(vm);