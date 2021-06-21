package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member save = memberRepository.save(member);
        Member findMember = memberRepository.findById(save.getId()).get();
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(save).isEqualTo(findMember);
    }

    @Test
    void basicCRUD() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);

    }

    @Test
    void findByUsernameTest() {
        Member member1 = new Member("member1");
        memberRepository.save(member1);
        memberRepository.flush();

        //Member memberA = memberRepository.findByUsername("member1");

        //assertThat(member1).isEqualTo(memberA);
    }

    @Test
    void findByUsernameAndAgeGreaterThan() {
        Member member1 = new Member("AAA", 10);
        Member member2 = new Member("AAA", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    void namedQueryTest() {

        Member member1 = new Member("aaa", 10);
        memberRepository.save(member1);

        List<Member> aaa = memberRepository.findByUsername("aaa");
        assertThat(aaa.get(0).getUsername()).isEqualTo(member1.getUsername());
    }

    @Test
    void interfaceQueryTest() {

        Member member1 = new Member("aaa", 10);
        memberRepository.save(member1);

        List<Member> matchUser = memberRepository.findUser("aaa", 10);

        assertThat(matchUser.get(0)).isEqualTo(member1);
    }

    @Test
    void findMemberDto() {
        Team team = new Team("team1");
        teamRepository.save(team);

        Member member = new Member("aaa",10);
        member.setTeam(team);
        memberRepository.save(member);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    void findByNames(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findbyNames(Arrays.asList("member1", "member2"));
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void returnTypeTest(){
        Member member1 = new Member("member1");
        memberRepository.save(member1);

        List<Member> listByUsername = memberRepository.findListByUsername("member1");
        for (Member member : listByUsername) {
            System.out.println("member = " + member.toString());
        }

        Member memberByUsername = memberRepository.findMemberByUsername("member1");
        System.out.println("memberByUsername = " + memberByUsername);

        Optional<Member> optionalMemberByUsername = memberRepository.findOptionalMemberByUsername("member1");
        System.out.println("optionalMemberByUsername = " + optionalMemberByUsername.get());
    }

    @Test
    void paingTest(){
        memberRepository.save(new Member("aaa1",10));
        memberRepository.save(new Member("aaa2",10));
        memberRepository.save(new Member("aaa3",10));
        memberRepository.save(new Member("aaa4",10));
        memberRepository.save(new Member("aaa5",10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
        Page<Member> byPage = memberRepository.findByAge(10, pageRequest);
        Page<MemberDto> map = byPage.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        //member entity -> memberDto로 바인딩.api에 사용가능.

        assertThat(byPage.getContent().size()).isEqualTo(3);
        assertThat(byPage.getTotalElements()).isEqualTo(5);
        assertThat(byPage.getNumber()).isEqualTo(0);
        assertThat(byPage.getTotalPages()).isEqualTo(2);
        assertThat(byPage.isFirst()).isTrue();
        assertThat(byPage.hasNext()).isTrue();
    }

    @Test
    void sliceTest(){
        memberRepository.save(new Member("aaa1",10));
        memberRepository.save(new Member("aaa2",10));
        memberRepository.save(new Member("aaa3",10));
        memberRepository.save(new Member("aaa4",10));
        memberRepository.save(new Member("aaa5",10));

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
//        Slice<Member> byPage = memberRepository.findByAge(10, pageRequest);
//
//        assertThat(byPage.getContent().size()).isEqualTo(3);
//        assertThat(byPage.getNumber()).isEqualTo(0);
//        assertThat(byPage.isFirst()).isTrue();
//        assertThat(byPage.hasNext()).isTrue();
    }
    @Test
    void bulkUpdate(){
        memberRepository.save(new Member("aaa1",10));
        memberRepository.save(new Member("aaa2",19));
        memberRepository.save(new Member("aaa3",20));
        memberRepository.save(new Member("aaa4",21));
        memberRepository.save(new Member("aaa5",40));
        /*
            bulk연산은 영속성 컨테이너를 건들지 않고 직접 DB에 값을 날린다.
            위에 save를 통해서 영속성 컨테이너에 저장된 값은 bulk연산이 반영되지 않은 상태의 값이기 때문에
            flush, clear를 통해서 영속성 컨테이너를 한번 비워주고
            추가로 조회쿼리를 수행시켜서 새로이 영속성 컨테이너를 받아야 한다.
        */
        int resultAge = memberRepository.bulkAgePlus(20);

        //bulkAgePlus을 수행해서 aaa5의 나이가 41이 되었으나,
        //영속성 컨테이너의 값은 아직 40인 상태이므로 40이 나온다. (DB값을 가져온게 아니기 때문)
        //이러한 위험성 때문에 bulk를 수행후에는 반드시 flush, clear를 통해서 영속성 컨테이너를 비우고 새롭게 가져오도록 구현해야함.
        List<Member> aaa5 = memberRepository.findByUsername("aaa5");
        int age = aaa5.get(0).getAge();
        System.out.println("age = " + age);//디비값 41/ 기대값 41 /실제 조회되는 값 40(영속성컨테이너값)


        assertThat(resultAge).isEqualTo(3);

    }

    @Test
    void findMemberLazy() {

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        Member member3 = new Member("member1",10,teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> all = memberRepository.findAll();//override하기전 기준.
        for (Member member : all) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam().getClass() = " + member.getTeam().getClass());
            if(member.getTeam() !=null){//Member entity에서 team이 null인지 아닌지 구분을 하며 null이 아닌 경우, lazy인 경우는 프록시객체로 대체되있음.
                System.out.println("member.team = " + member.getTeam().getName());
            }
        }

        System.out.println("----------------------2-------------------------");

        List<Member> allFetch = memberRepository.findMemberFetchJoin();
        for (Member member : allFetch) {
            System.out.println("member = " + member);
            if(member.getTeam() !=null){
                System.out.println("member.team = " + member.getTeam().getName());
            }
        }

        System.out.println("----------------------3--------------------------");

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member);
            if(member.getTeam() !=null){
                System.out.println("member.team = " + member.getTeam().getName());
            }
        }

        System.out.println("----------------------4--------------------------");

        List<Member> members2 = memberRepository.findNamedEntityGraphByUsername("member1");
        for (Member member : members2) {
            System.out.println("member = " + member);
            if(member.getTeam() !=null){
                System.out.println("member.team = " + member.getTeam().getName());
            }
        }

    }

    @Test
    void queryHint() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        Member member = memberRepository.findReadOnlyByUsername("member1");//hibernate에 query hint를 적용. readonly로 만들어서
        //내부에서 스냅샷을 생성안해서 영속성컨테이너에서 변경감지할 비교대상 객체 생성을 안해서 성능 향상을 할 수 있음.(이점이 크지 않음.)
        member.setUsername("member2");

        em.flush();

    }

    @Test
    void queryLock() {
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        List<Member> lock = memberRepository.findLockByUsername("member1");
        em.flush();

    }

    @Test
    void callCustom() {
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    void memberSpec() {
        Team team = new Team("Ateam");
        em.persist(team);

        Member m1 = new Member("m1",10,team);
        Member m2 = new Member("m2",10,team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Specification<Member> specification = Memberspec.username("m1").and(Memberspec.teamName("Ateam"));
        List<Member> all = memberRepository.findAll(specification);

        assertThat(all.size()).isEqualTo(1);

    }

    @Test
    void projections() {
        Team team = new Team("Ateam");
        em.persist(team);

        Member m1 = new Member("m1",10,team);
        Member m2 = new Member("m2",10,team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

//        List<UsernameOnly> m11 = memberRepository.findProjectionByUsername("m1");
//
//        for (UsernameOnly member : m11) {
//            System.out.println("member = " + member);
//        }

        List<UsernameOnlyDto> m11 = memberRepository.findProjectionByUsername("m1", UsernameOnlyDto.class);

        for (UsernameOnlyDto member : m11) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void nativeQuery() {
        Team team = new Team("Ateam");
        em.persist(team);

        Member m1 = new Member("m1",10,team);
        Member m2 = new Member("m2",10,team);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Member m11 = memberRepository.findByNativeQuery("m1");

        Page<MemberProjection> byNativeProjection = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = byNativeProjection.getContent();
        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection = " + memberProjection);
        }
    }
}