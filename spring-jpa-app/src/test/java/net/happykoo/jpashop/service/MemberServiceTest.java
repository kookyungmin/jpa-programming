package net.happykoo.jpashop.service;

import net.happykoo.jpashop.domain.Member;
import net.happykoo.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional //테스트 종료 시 자동 롤백
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트 :: 성공했을 때")
    public void test1() {
        Member member = Member.builder()
                .name("happykoo")
                .build();

        Long saveId = memberService.join(member);
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    @DisplayName("회원가입 테스트 :: 예외 발생")
    public void test2() {
        Member member1 = Member.builder()
                .name("happykoo")
                .build();

        Member member2 = Member.builder()
                .name("happykoo")
                .build();

        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            //flush 가 호출됨
            memberService.join(member2);
        });
    }
}
