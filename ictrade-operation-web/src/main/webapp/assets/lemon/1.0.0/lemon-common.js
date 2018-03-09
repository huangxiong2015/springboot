/**
 * Created by yansha on 2017/4/12.
 */
var common={
    oneOf (value, validList) {
        for (let i = 0; i < validList.length; i++) {
            if (value === validList[i]) {
                return true;
            }
        }
        return false;
    },
    blurInput (self) {
        if (!self.mousedownState) {
            self.searchText = ''
            self.closeOptions()
        }
    },
    closeOptions (self) {
        self.showMenu = false;
    },
    // up arrow key
    prevItem (self) {
        const selectedItemIndex = self.filteredOptions.findIndex(item => {
            return item.selected === true
        })
        if (selectedItemIndex === -1) {
            self.filteredOptions[0].selected = true
        } else if (selectedItemIndex === 0) {
            // nothing todo
        } else {
            self.filteredOptions[selectedItemIndex].selected = false
            self.filteredOptions[selectedItemIndex - 1].selected = true
        }
    },
    // down arrow key
    nextItem (self) {
        const selectedItemIndex = self.filteredOptions.findIndex(item => {
            return item.selected === true
        })
        if (selectedItemIndex === -1) {
            self.filteredOptions[0].selected = true
        } else if (selectedItemIndex === self.filteredOptions.length - 1) {
            // nothing todo
        } else {
            self.filteredOptions[selectedItemIndex].selected = false
            self.filteredOptions[selectedItemIndex + 1].selected = true
        }
    },
    enterItem (self) {
        // selected = true
        const selectedItem = self.filteredOptions.find(item => {
            return item.selected === true
        })
        if (selectedItem) {
            self.selectItem(selectedItem)
        }
    },
    mousedownItem (self) {
        self.mousedownState = true
    }
}