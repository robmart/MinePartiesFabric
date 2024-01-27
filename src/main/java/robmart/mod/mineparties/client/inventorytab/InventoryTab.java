package robmart.mod.mineparties.client.inventorytab;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class InventoryTab {
    private static final List<InventoryTab> tabs = new ArrayList<>();
    private boolean shouldDisplay;

    public static ImmutableList<InventoryTab> getAllTabs() {
        return ImmutableList.copyOf(tabs);
    }

    public static List<InventoryTab> tabsToDisplay() {
        return getAllTabs().stream().filter(InventoryTab::shouldDisplay).toList();
    }

    public static InventoryTab getDefaultTab() {
        return null;
    }

    public boolean shouldDisplay() {
        return shouldDisplay;
    }

    public void setShouldDisplay(boolean shouldDisplay) {
        this.shouldDisplay = shouldDisplay;
    }
}
