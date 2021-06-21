package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team teamA = new Team("temaA");
        Team teamB = new Team("temaB");
        entityManager.persist(teamA);
        entityManager.persist(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",20,teamA);
        Member member3 = new Member("member3",30,teamB);
        Member member4 = new Member("member4",40,teamB);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        entityManager.persist(member4);

        entityManager.flush();
        entityManager.clear();

        List<Member> members = entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member.toString());
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    void JpaEventBaseEntity() throws InterruptedException {
        Member member = new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        entityManager.flush();
        entityManager.clear();

        Member member1 = memberRepository.findById(member.getId()).get();
        //System.out.println("member1 = "+member1.getUsername()+":" + member1.getCreateDate());
        //System.out.println("member1 = "+member1.getUsername()+":" + member1.getUpdateDate());
        System.out.println("member1 = " + member1.getCreateTime());
        System.out.println("member1 = " + member1.getCreateBy());
        System.out.println("member1 = " + member1.getLastModifiedDate());
        System.out.println("member1 = " + member1.getLastModifiedBy());
    }
}