package book.shop.service;

import book.shop.domain.Member;
import book.shop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest // 스프링 부트를 띄운 상태로 테스트.. 없으면 autowired 실패.. 스프링 컨테이너 안에서 테스트 실행
@Transactional // for test, default roll back .. db에 insert 쿼리 안나가고 영속성 컨텍스트에만 유지 후 테스트 종료
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void 회원가입() throws Exception {

        Member member = new Member();
        member.setName("song");

        Long savedId = memberService.join(member);
        //System.out.println( memberRepository.findOne(savedId).getClass());

        em.flush(); // but i want to see insert query
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // not use try catch
    public void 중복_회원_예외() throws Exception {

        Member member1 = new Member();
        member1.setName("song");

        Member member2 = new Member();
        member2.setName("song");

        memberService.join(member1);
        memberService.join(member2); // exception !!

        fail("예외가 발생해야 한다."); //


    }
}