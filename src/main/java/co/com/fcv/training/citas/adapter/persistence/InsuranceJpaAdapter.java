package co.com.fcv.training.citas.adapter.persistence;

import co.com.fcv.training.citas.application.Ports;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
class InsuranceJpaAdapter implements Ports.Affiliations {
    private final JdbcTemplate jdbc;
    InsuranceJpaAdapter(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public void createCurrent(Long userId, Long planId) {
        Integer count = jdbc.queryForObject("select count(*) from eps_plans where id=? and active=true", Integer.class, planId);
        if (count == null || count == 0) throw new IllegalArgumentException("Plan de afiliación inválido");
        jdbc.update("insert into user_insurance_affiliations(user_id,plan_id,membership_number,is_current) values (?,?,?,true)", userId, planId, "AUTO-" + userId + "-" + planId);
    }
}
