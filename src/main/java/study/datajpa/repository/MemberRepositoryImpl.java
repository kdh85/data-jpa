package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;
/*
    기존 jpa interface repository에 커스텀할 interface repossitory를 상속하고
    커스텀 repository에 Impl을 붙여서 구현체 클래스를 생성한다.
    JPA가 interface간의 상속을 약속된 클래스 네이밍법에 의거해서 해준다.
    Impl을 변경하고 싶으면 설정을 해야함.
    @EnableJpaRepositories(basePakages="study.datajpa.repository",repositoryImplementationPostfix ="Impl")
    에서 repositoryImplementationPostfix을 원하는 값으로 변경.
*/
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager entityManager;

    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery("select m from Member m")
                .getResultList();
    }
}
