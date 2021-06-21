package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {


    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id")Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/member-domain-convert/{id}")
    public String findMember(@PathVariable("id")Member member){
        Member findeMember = memberRepository.findById(member.getId()).get();
        return findeMember.getUsername();
    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){

        //default page =20으로 되어있음.
        //global setting으로 변경할시,
        // data:
        //    web:
        //      pageable:
        //        default-page-size: 10
        //        max-page-size: 2000
        //메소드 단위 설정시,
        //@PageableDefault(size = 5)
        //둘다 설정할 시엔 메소드단위 설정이 우선순위를 가짐.
        //한페이지에 두가지 이상의 페이징 처리가 필요할 시엔
        //@Qualifier("member")Pageable memberPagealbe 이런식으로 표현 가능

        return memberRepository.findAll(pageable).map(MemberDto::new);
        //return memberRepository.findAll(pageable).map(member -> new MemberDto(member.getId(),member.getUsername(),null));
        //return memberRepository.findAll(pageable);
    }

    //@PostConstruct
    public void init(){
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
