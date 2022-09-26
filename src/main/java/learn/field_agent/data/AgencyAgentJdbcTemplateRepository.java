package learn.field_agent.data;

import learn.field_agent.data.mappers.AgencyAgentMapper;
import learn.field_agent.models.AgencyAgent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AgencyAgentJdbcTemplateRepository implements AgencyAgentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AgencyAgentJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean add(AgencyAgent agencyAgent) {

        final String sql = "insert into agency_agent (agency_id, agent_id, identifier, security_clearance_id "
                + "activation_date, is_active) values "
                + "(?,?,?,?,?,?);";

        return jdbcTemplate.update(sql,
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId(),
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive()) > 0;
    }

    @Override
    public boolean update(AgencyAgent agencyAgent) {

        final String sql = "update agency_agent set "
                + "identifier = ?, "
                + "security_clearance_id = ?, "
                + "activation_date = ?, "
                + "is_active = ? "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql,
                agencyAgent.getIdentifier(),
                agencyAgent.getSecurityClearance().getSecurityClearanceId(),
                agencyAgent.getActivationDate(),
                agencyAgent.isActive(),
                agencyAgent.getAgencyId(),
                agencyAgent.getAgent().getAgentId()) > 0;

    }

    @Override
    public boolean deleteByKey(int agencyId, int agentId) {

        final String sql = "delete from agency_agent "
                + "where agency_id = ? and agent_id = ?;";

        return jdbcTemplate.update(sql, agencyId, agentId) > 0;
    }

public List<AgencyAgent> findAll(){
        final String sql = "select agency_id, agent_id, identifier, security_clearance_id, name security_clearance_name, activation_date, is_active from agency_agent limit 1000;";
        return jdbcTemplate.query(sql, new AgencyAgentMapper());
}

//public AgencyAgent containsSecurityClearancesById(int securityClearanceId){
//        final String sql = "select count(security_clearance_id) from agency " +
//                "where security_clearance_id = ?";
//        return jdbcTemplate.query(sql, new AgencyAgentMapper(),securityClearanceId).stream().findFirst().orElse(null);
//}

//public List<AgencyAgent> findSecurityClearanceId(int securityClearanceId){
//        final String sql = " select agency_id, agent_id, identifier, security_clearance_id, name security_clearance_name, activation_date, is_active "
//            +"from agency_agent "
//            + "where security_clearance_id = ? ; " ;
//        return jdbcTemplate.query(sql, new AgencyAgentMapper(), securityClearanceId);
//}

public boolean countContainsSecurityId(int securityClearanceId){
        final String sql = " select count(security_clearance_id) from agency_agent where security_clearance_id=? ";

        return jdbcTemplate.queryForObject(sql, Integer.class,securityClearanceId).intValue()>0;
}
}
