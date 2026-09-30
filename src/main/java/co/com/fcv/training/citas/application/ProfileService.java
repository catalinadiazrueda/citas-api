package co.com.fcv.training.citas.application;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProfileService {
    private final JdbcTemplate jdbc;
    public ProfileService(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public Map<String,Object> profile(Long user) { return jdbc.queryForMap("select id,first_name as firstName,last_name as lastName,document_type as documentType,document_number as documentNumber,email,phone from users where id=? and active=true",user); }
    @Transactional public Map<String,Object> update(Long user,String first,String last,String email,String phone) {
        Map<String,Object> old=profile(user); String nextEmail=email==null?String.valueOf(old.get("email")):email.trim().toLowerCase(Locale.ROOT);
        if(jdbc.queryForObject("select count(*) from users where email=? and id<>?",Integer.class,nextEmail,user)>0) throw new DuplicateIdentity();
        jdbc.update("update users set first_name=?,last_name=?,email=?,phone=? where id=?",first==null?old.get("firstName"):first.trim(),last==null?old.get("lastName"):last.trim(),nextEmail,phone==null?old.get("phone"):phone.trim(),user);
        return profile(user);
    }
    public Map<String,Object> affiliation(Long user) { return jdbc.query("select a.id,a.plan_id as planId,a.membership_number as membershipNumber,p.name as planName,e.name as epsName,r.name as regimeName from user_insurance_affiliations a join eps_plans p on p.id=a.plan_id join eps e on e.id=p.eps_id join insurance_regimes r on r.id=p.regime_id where a.user_id=? and a.is_current=true order by a.id desc",(rs,n)->map(rs),user).stream().findFirst().orElse(Map.of()); }
    @Transactional public Map<String,Object> updateAffiliation(Long user,Long plan,String membership) {
        if(jdbc.queryForObject("select count(*) from eps_plans where id=? and active=true",Integer.class,plan)==0) throw new IllegalArgumentException("Plan activo inválido");
        if(membership==null||membership.isBlank()) throw new IllegalArgumentException("Número de afiliación requerido");
        jdbc.update("update user_insurance_affiliations set is_current=false where user_id=? and is_current=true",user);
        jdbc.update("insert into user_insurance_affiliations(user_id,plan_id,membership_number,is_current) values (?,?,?,true)",user,plan,membership.trim()); return affiliation(user);
    }
    public List<Map<String,Object>> eps(){return jdbc.queryForList("select id,code,name,active from eps order by name");}
    @Transactional public Map<String,Object> saveEps(Long id,String code,String name,boolean active){if(id==null){jdbc.update("insert into eps(code,name,active) values (?,?,?)",code.trim(),name.trim(),active);id=jdbc.queryForObject("select id from eps where code=?",Long.class,code.trim());}else jdbc.update("update eps set code=?,name=?,active=? where id=?",code.trim(),name.trim(),active,id);return jdbc.queryForMap("select id,code,name,active from eps where id=?",id);}
    public List<Map<String,Object>> plans(){return jdbc.queryForList("select p.id,p.eps_id as epsId,p.regime_id as regimeId,p.code,p.name,p.active from eps_plans p order by p.name");}
    @Transactional public Map<String,Object> savePlan(Long id,Long eps,Long regime,String code,String name,boolean active){if(jdbc.queryForObject("select count(*) from eps where id=?",Integer.class,eps)==0||jdbc.queryForObject("select count(*) from insurance_regimes where id=?",Integer.class,regime)==0)throw new IllegalArgumentException("EPS o régimen inválido");if(id==null){jdbc.update("insert into eps_plans(eps_id,regime_id,code,name,active) values (?,?,?,?,?)",eps,regime,code.trim(),name.trim(),active);id=jdbc.queryForObject("select id from eps_plans where eps_id=? and code=?",Long.class,eps,code.trim());}else jdbc.update("update eps_plans set eps_id=?,regime_id=?,code=?,name=?,active=? where id=?",eps,regime,code.trim(),name.trim(),active,id);return jdbc.queryForMap("select id,eps_id as epsId,regime_id as regimeId,code,name,active from eps_plans where id=?",id);}
    private Map<String,Object> map(java.sql.ResultSet rs){try{Map<String,Object> m=new LinkedHashMap<>();for(int i=1;i<=rs.getMetaData().getColumnCount();i++)m.put(rs.getMetaData().getColumnLabel(i),rs.getObject(i));return m;}catch(Exception e){throw new IllegalStateException(e);}}
}
