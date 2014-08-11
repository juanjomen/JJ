package org.familysearch.standards.place.ws.mapping;

import static org.testng.Assert.*;

import org.familysearch.standards.place.ws.model.NameModel;
import org.familysearch.standards.place.ws.model.PlaceRepresentationModel;
import org.testng.annotations.Test;

public class TestDisplayNameComparator {

    @Test(groups = {"unit"})
    public void compare() {
        DisplayNameComparator comparator = new DisplayNameComparator();

        NameModel dispName01 = new NameModel();
        dispName01.setLocale("en");
        dispName01.setName("PLUTO");

        PlaceRepresentationModel prModel01 = new PlaceRepresentationModel();
        prModel01.setDisplayName(dispName01);

        NameModel dispName02 = new NameModel();
        dispName02.setLocale("en");
        dispName02.setName("provo");

        PlaceRepresentationModel prModel02 = new PlaceRepresentationModel();
        prModel02.setDisplayName(dispName02);

        assertTrue(comparator.compare(prModel01, prModel02) < 0);
        assertTrue(comparator.compare(prModel02, prModel01) > 0);
        assertTrue(comparator.compare(prModel02, prModel02) == 0);
    }
}
