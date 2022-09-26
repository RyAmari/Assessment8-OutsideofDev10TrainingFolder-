package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    final static int NEXT_ID = 3;

    @Autowired
    AliasJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        Alias testName = new Alias(1, "Test Alias",  "Test Persona", 1);
        Alias fakeName = new Alias(2, "Fake Alias",  "Fake Persona", 2);

        Alias actual = repository.findById(1);
        assertEquals(testName, actual);

        actual = repository.findById(2);
        assertEquals(fakeName, actual);

        actual = repository.findById(99);
        assertEquals(null, actual);
    }

    @Test
    void shouldFindAll() {
        List<Alias> aliasList = repository.findAll();
        assertNotNull(aliasList);

    }

    @Test
    void shouldAdd() {
        Alias alias = makeAlias();
        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertEquals(NEXT_ID,actual.getAliasId());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAlias();
        alias.setAliasId(2);
        assertTrue(repository.update(alias));
        alias.setAliasId(10);
        assertFalse(repository.update(alias));
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(2));
        assertFalse(repository.deleteById(2));
    }

    private Alias makeAlias(){
        Alias alias = new Alias();
        alias.setName("Faulty Test Alias");
        alias.setAgentId(3);
        alias.setPersona("Faux Persona");
        return alias;
    }
}