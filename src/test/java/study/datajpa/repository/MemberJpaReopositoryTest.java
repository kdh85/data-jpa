package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
 class MemberJpaReopositoryTest {

    @Autowired MemberJpaReopository memberJpaReopository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member save = memberJpaReopository.save(member);
        Member findMember = memberJpaReopository.find(save.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(save).isEqualTo(findMember);
    }

    @Test
    void basicCRUD() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaReopository.save(member1);
        memberJpaReopository.save(member2);

        Member findMember1 = memberJpaReopository.findById(member1.getId()).get();
        Member findMember2 = memberJpaReopository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJpaReopository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaReopository.count();
        assertThat(count).isEqualTo(2);

        memberJpaReopository.delete(member1);
        memberJpaReopository.delete(member2);

        long deleteCount = memberJpaReopository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberJpaReopository.save(member1);
        memberJpaReopository.save(member2);

        List<Member> result = memberJpaReopository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void paingTest(){
        memberJpaReopository.save(new Member("aaa1",10));
        memberJpaReopository.save(new Member("aaa2",10));
        memberJpaReopository.save(new Member("aaa3",10));
        memberJpaReopository.save(new Member("aaa4",10));
        memberJpaReopository.save(new Member("aaa5",10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> byPage = memberJpaReopository.findByPage(age, offset, limit);
        long totalCount = memberJpaReopository.totalCount(age);

        assertThat(byPage.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkUpdate(){
        memberJpaReopository.save(new Member("aaa1",10));
        memberJpaReopository.save(new Member("aaa2",19));
        memberJpaReopository.save(new Member("aaa3",20));
        memberJpaReopository.save(new Member("aaa4",21));
        memberJpaReopository.save(new Member("aaa5",40));

        int resultAge = memberJpaReopository.bulkAgePlus(20);
        assertThat(resultAge).isEqualTo(3);
    }
}