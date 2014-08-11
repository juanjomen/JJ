package org.familysearch.standards.place.ws.model;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.familysearch.standards.place.ws.helper.POJOTestUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TokenMatchTest {

    TokenMatch model;


    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        model = new TokenMatch();
    }


    @Test(groups = { "unit" })
    public void testToken() {
        assertNotNull(model);
    }

    @Test(groups = { "unit" })
    public void testTokenStringInteger() {
        model = new TokenMatch();

        model.setId( 111 );
        model.setToken( "token" );
        assertEquals(model.getId(), Integer.valueOf(111));
        assertEquals(model.getToken(), "token");
    }

    @Test(groups = { "unit" })
    public void testPojo() throws Exception {
        POJOTestUtil pojoTest = new POJOTestUtil(TokenMatch.class);
        pojoTest.test();
    }

}
