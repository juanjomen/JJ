package org.familysearch.standards.place.ws.model;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;


public class TypeModelTest {

    TypeModel model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new TypeModel();
    }


    @Test(groups = { "unit" })
    public void testTypeModel() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(TypeModel.class);
        pojoTest.test();
    }

    @Test(groups = {"unit"})
    public void testComparatorType() {
        TypeModel type01 = new TypeModel();
        TypeModel type02 = new TypeModel();

        List<LocalizedNameDescModel> nameDescList = new ArrayList<LocalizedNameDescModel>();
        nameDescList.add(mock(LocalizedNameDescModel.class));
        when(nameDescList.get(0).getLocale()).thenReturn("en");
        when(nameDescList.get(0).getName()).thenReturn("aaa");

        // TypeName1 is null ...
        type01.setName(null);
        type02.setName(null);
        assertEquals(-1, type01.compareTo(type02));

        // TypeName2 is null
        type01.setName(nameDescList);
        type02.setName(null);
        assertEquals(1, type01.compareTo(type02));

        // TypeName1 is empty ...
        type01.setName(new ArrayList<LocalizedNameDescModel>());
        type02.setName(nameDescList);
        assertEquals(-1, type01.compareTo(type02));

        // TypeName2 is empty ...
        type01.setName(nameDescList);
        type02.setName(new ArrayList<LocalizedNameDescModel>());
        assertEquals(1, type01.compareTo(type02));
    }

    @Test(groups = {"unit"})
    public void testComparatorEnglishName() {
        TypeModel type01 = new TypeModel();
        TypeModel type02 = new TypeModel();

        List<LocalizedNameDescModel> nameDescList01 = new ArrayList<LocalizedNameDescModel>();
        nameDescList01.add(mock(LocalizedNameDescModel.class));

        List<LocalizedNameDescModel> nameDescList02 = new ArrayList<LocalizedNameDescModel>();
        nameDescList02.add(mock(LocalizedNameDescModel.class));

        type01.setName(nameDescList01);
        type02.setName(nameDescList02);

        // Regular comparison
        when(nameDescList01.get(0).getLocale()).thenReturn("en");
        when(nameDescList01.get(0).getName()).thenReturn("aaa");
        when(nameDescList02.get(0).getLocale()).thenReturn("en-US");
        when(nameDescList02.get(0).getName()).thenReturn("bbb");
        assertTrue(type01.compareTo(type02) < 0);

        // TypeName01 has null locale
        when(nameDescList01.get(0).getLocale()).thenReturn(null);
        when(nameDescList01.get(0).getName()).thenReturn("aaa");
        when(nameDescList02.get(0).getLocale()).thenReturn("en");
        when(nameDescList02.get(0).getName()).thenReturn("bbb");
        assertTrue(type01.compareTo(type02) < 0);

        // no "en" locale
        when(nameDescList01.get(0).getLocale()).thenReturn("de");
        when(nameDescList01.get(0).getName()).thenReturn("aaa");
        when(nameDescList02.get(0).getLocale()).thenReturn("de");
        when(nameDescList02.get(0).getName()).thenReturn("bbb");
        assertTrue(type01.compareTo(type02) < 0);
    }
}
