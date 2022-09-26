package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.Alias;
import learn.field_agent.models.Alias;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @Autowired
    AliasService service;

    @MockBean
    AliasRepository repository;

    @Test
    void shouldFindYesterday() {
        Alias expected = makeAlias();
        when(repository.findById(1)).thenReturn(expected);
        Alias actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }
    @Test
    void shouldNotAddWhenInvalid() {
        Alias alias = makeAlias();
        Result<Alias> result = service.add(alias);
        assertEquals(ResultType.INVALID, result.getType());

        alias.setAliasId(0);
        alias.setName(null);
        alias.setAgentId(0);
        result = service.add(alias);
        assertEquals(ResultType.INVALID, result.getType());
    }

    Alias makeAlias(){
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("Yesterday");
        alias.setPersona("You will only know they've arrived once they have already gone");
        alias.setAgentId(2);
        return alias;
    }
}