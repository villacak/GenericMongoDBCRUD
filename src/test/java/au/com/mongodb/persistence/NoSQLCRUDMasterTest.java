//package au.com.mongodb.persistence;
//
//import au.com.mongodb.HelperTest;
//import au.com.mongodb.enums.EventSearchField;
//import au.com.mongodb.persistence.entities.Event;
//import au.com.mongodb.persistence.entities.SystemMetadata;
//import au.com.mongodb.services.v1.health.HealthHelper;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//public class NoSQLCRUDMasterTest {
//
//    private Event event;
//    private Event saved;
//    private Event update;
//    private SystemMetadata systemMetadata;
//    private NoSQLCRUDMaster crud;
//
//
//    @Before
//    public void startup() throws Exception {
//        final HealthHelper healthHelper = new HealthHelper();
//        boolean isRunning = healthHelper.checkifMongoIsRunning();
//
//        if (isRunning) {
//            final HelperTest helperTest = new HelperTest();
//            event = helperTest.getEventEntityForTest();
//            systemMetadata = event.getMedatadata();
//            crud = new NoSQLCRUDMaster();
//        } else {
//            throw new Exception("Local MongoDB not Running");
//        }
//    }
//
//
//    @After
//    public void cleanup() {
//        if (update == null) {
//            crud.delete(saved);
//        } else {
//            crud.delete(update);
//        }
//
//        event = null;
//        update = null;
//        systemMetadata = null;
//        crud = null;
//    }
//
//
//    @Test
//    public void testSuccessSaveSearchThenDelete() {
//        saved = crud.saveData(event);
//        assertNotNull(saved);
//
//        List<Event> search = crud.search(EventSearchField.ACCOUNT_ID, "accountID");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        Event fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//    }
//
//
//    @Test
//    public void testSuccessSaveSearchUpdateThenDelete() {
//        saved = crud.saveData(event);
//        assertNotNull(saved);
//
//        List<Event> search = crud.search(EventSearchField.ACCOUNT_ID, "accountID");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        Event fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//
//        fromSearch.setAccountId("UpdatedAccountId");
//        update = crud.mergeData(fromSearch);
//
//        search = crud.search(EventSearchField.ACCOUNT_ID, "UpdatedAccountId");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        fromSearch = search.get(0);
//        assertEquals(fromSearch, update);
//    }
//
//
//    @Test
//    public void testSuccessSaveSearchsThenDelete() {
//        saved = crud.saveData(event);
//        assertNotNull(saved);
//
//        List<Event> search = crud.search(EventSearchField.ACCOUNT_ID, "accountID");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        Event fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//
//        search = crud.search(EventSearchField.ACCOUNT_NUMBER, "accountNumber");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//
//        search = crud.search(EventSearchField.REFERENCE_NUMBER, "referenceNumber");
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//
//
//        search = crud.search(EventSearchField.ID, saved.get_id());
//        assertNotNull(search);
//        assertTrue(search.size() > 0);
//
//        fromSearch = search.get(0);
//        assertEquals(fromSearch, saved);
//    }
//}
